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

import com.onbelay.core.entity.component.ApplicationContextFactory;
import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.repository.AuditEntityRepository;
import com.onbelay.core.exception.OBValidationException;
import com.onbelay.core.utils.DateUtils;
import jakarta.persistence.*;
import org.hibernate.type.YesNoConverter;


/**
 * Base class for all history tables. Sub-classes must implement two methods:
 * <ul>
 * <li> getParent() - return the parent or the primary object this object is tracking
 * 		history for. </li>
 * <li> copyFrom() - copy from the primary table all non collection attributes. </li>
 * </ul>
 * 
 *
 */
@MappedSuperclass
@EntityListeners(value = {AuditTableListener.class})
public abstract class AuditAbstractEntity extends AbstractEntity implements VersionedAuditEntity {
    private Boolean isDeleted = Boolean.FALSE;
    
	
	private Long auditVersion;
	private Long entityVersion;

	
    @Version
    @Column(name = "AUDIT_LOCK_NO")
    public Long getVersion() {
        return auditVersion;
    }
    
    public void setVersion(Long version) {
        this.auditVersion = version;
    }
    
	
    @Column(name = "OPTIMISTIC_LOCK_NO")
	public Long getEntityVersion() {
		return entityVersion;
	}

	public void setEntityVersion(Long entityVersion) {
		this.entityVersion = entityVersion;
	}


    private boolean isRecentHistory = false;
	protected HistoryDateTimeStamp historyDateTimeStamp = new HistoryDateTimeStamp();
	protected String auditComments;
	
	@Transient
	public abstract TemporalAbstractEntity getParent();
	
	public abstract void copyFrom(TemporalAbstractEntity entity);

    protected void recordHistory() {
        this.setIsRecentHistory(true); // 
        getAuditEntityRepository().recordHistory(this);
    }
    
	@Override
	protected void validate() throws OBValidationException {
	}

	@Transient
    public EntityState getEntityState() {
        return EntityState.UNMODIFIED;
    }
    
    @Transient
    public boolean isRecentHistory() {
        return isRecentHistory;
    }
    
    public void setIsRecentHistory(boolean is) {
        this.isRecentHistory = is;
    }
    
    @Transient
    public String getLastUserid() {
        return getHistoryDateTimeStamp().getLastUserid();
    }
    

    /**
     * Returns true if this audit record is the current record
     * @return
     */
    @Transient
    public boolean isCurrent() {
        return historyDateTimeStamp.getValidToDateTime().equals(DateUtils.getValidToDateTime());
    }

    @Embedded
	public HistoryDateTimeStamp getHistoryDateTimeStamp() {
		return historyDateTimeStamp;
	}

	public void setHistoryDateTimeStamp(HistoryDateTimeStamp historyDateTimeStamp) {
		this.historyDateTimeStamp = historyDateTimeStamp;
	}

    @Column(name = "EXPIRED_FLG")
    @Convert(converter = YesNoConverter.class)
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isExpired) {
        this.isDeleted = isExpired;
    }

    protected static AuditEntityRepository getAuditEntityRepository() {
        return (AuditEntityRepository) ApplicationContextFactory.getBean("auditEntityRepository");
    }

}