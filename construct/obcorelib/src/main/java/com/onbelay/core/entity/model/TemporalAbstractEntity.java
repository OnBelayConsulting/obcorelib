/*
 Copyright 2019, OnBelay Consulting Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.  
*/
package com.onbelay.core.entity.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

import com.onbelay.core.entity.snapshot.EntityId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.entity.snapshot.AbstractSnapshot;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.core.exception.OBValidationException;
import com.onbelay.core.utils.DateUtils;


/**
 * extends AbstractEntity class for business objects that are both auditable and maintain
 * historical snapshots in separate history tables.
 * 
 * Domain objects with history need to override the following:
 * <p>
 * <ul>
 * <li> createHistory() - create the history record during the initial persist().
 * <li> updateHistory() - return the most recent history record to be updated
 * 	    and create a new History record.
 * <li> shallowCopyFrom() - copy attributes and references from the prime table to the history
 *      table.
 * <li> fetchRecentHistory() - return the most recent history object.     
 * </ul>
 *
 * The above methods will support the creation of a new history instance and the update of
 * the most recent history reference.
 *
 */
@MappedSuperclass
public abstract class TemporalAbstractEntity extends AbstractEntity implements VersionedEntity {
	private static Logger logger = LogManager.getLogger(TemporalAbstractEntity.class);
	
	private Boolean isExpired = Boolean.FALSE;
    private Long version;

	private AuditAbstractEntity recentHistory;
	private AuditAbstractEntity newHistory;
	
    @Version
    @Column(name = "OPTIMISTIC_LOCK_NO")
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }

	
	@Override
	protected void validate() throws OBValidationException {
	}

	public void createWith(AbstractSnapshot snapshot) {
	        
	}
	
	public void updateWith(AbstractSnapshot valueObject) {
        if (valueObject.isVersioned() && this.getVersion() != null) {
        	if (this.getVersion().longValue() != valueObject.getVersion().longValue()) {
        		logger.error("Entity version: " + this.getVersion() + " is not equal to update version: " + valueObject.getVersion());
        		throw new OBRuntimeException(CoreTransactionErrorCode.ENTITY_VERSION_FAIL.getCode());
        	}
        }
	}
	
	/**
	 * Sub-classes should override this to handle events after the creation.
	 */
	public void handleCreateUpdateWith(AbstractSnapshot valueObject) {
	    
	}

	@Transient
	@Override
	public EntityId generateEntityId() {
		return new EntityId(getId(), "", "", isExpired);
	}

	/**
	 * Overrides the default save to save with history
	 */
	public void save() {
		validate();
        LocalDateTime historyDate = LocalDateTime.now();
		getEntityRepository().save(this);
        
        if (newHistory == null)
			newHistory = createHistory();
		if (newHistory != null)
			setHistoryAudit(newHistory, historyDate);
		getEntityRepository().saveWithHistory(this, newHistory);
	}

    @Transient
	public EntityState getEntityState() {
	    if (recentHistory != null)
	        return EntityState.MODIFIED;
	    
	    return EntityState.UNMODIFIED;
	}

	@Column(name="EXPIRED_FLG")
	@org.hibernate.annotations.Type(type="yes_no")
    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

    
    public void delete() {
    	if (this.isExpired) {
    		// Don't try to expire an already expired entity.
    		return;
    	}
    	
    	
        this.isExpired = true;
        generateHistory();
		getEntityRepository().delete(this);
    }
    

	/**
	 * Triggers the update mechanism which includes validation and the creation of history. 
	 */
	public void update() {
		validate();
		super.update();
		generateHistory();
	}
	
    /**
     * Triggers the update mechanism which includes validation and the creation of history. 
     */
    private void generateHistory() {
    	LocalDateTime historyDate = LocalDateTime.now();

        if (recentHistory == null) {
            recentHistory = fetchRecentHistory();
        }
        if (recentHistory != null) {
            recentHistory.setIsRecentHistory(true);
            recentHistory.getHistoryDateTimeStamp().setValidToDateTime(historyDate);
            getEntityRepository().flush();
        }
        if (newHistory == null) {
            newHistory = createHistory();
            if (newHistory != null) {
				getEntityRepository().save(newHistory);

            }
        }
        if (newHistory != null) {
            setHistoryAudit(newHistory, historyDate);
        }
    }
    
	private void setHistoryAudit(AuditAbstractEntity entity, LocalDateTime historyDate) {
	    String userName = getAuditManager().getCurrentAuditUserName();
	    entity.getHistoryDateTimeStamp().setLastUserid(userName);
		entity.getHistoryDateTimeStamp().setValidFromDateTime(historyDate);
		entity.getHistoryDateTimeStamp().setValidToDateTime(DateUtils.getValidToDateTime());

	}
	
	public void clearRecentHistory() {
		recentHistory = null;
		newHistory = null;
	}
	
	/**
	 * Override the default null operation to create a history record that will
	 * be included in the create transaction.
	 */
	protected abstract AuditAbstractEntity createHistory();
	
	/**
	 * Domain classes override this method to return the most recent history record.
	 * @return
	 */
	public abstract AuditAbstractEntity fetchRecentHistory();
	
	@Transient
	public LocalDateTime getValidFromDateTime() {
	    return fetchRecentHistory().getHistoryDateTimeStamp().getValidFromDateTime();
	}
	
    @Transient
    public LocalDateTime getValidToDateTime() {
        return fetchRecentHistory().getHistoryDateTimeStamp().getValidToDateTime();
    }
    
    @Transient
    public String getLastUserid() {
        return fetchRecentHistory().getLastUserid();
    }
    
	
}
