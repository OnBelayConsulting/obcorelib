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
package com.onbelay.core.entity.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onbelay.core.entity.enums.EntityState;

import java.io.Serializable;
/**
 * Base class for snapshots. extends ResourceSupport to enable HATEOS and handles common elements such as entity id, state, etc.
 * @author lefeu
 *
 */
public abstract class AbstractSnapshot  implements Serializable {
    private static final long serialVersionUID = 1L;
    
	private EntityState entityState = EntityState.NEW;
	private EntityId entityId;
	private Long version = -1l;
	
	public AbstractSnapshot() {
		
	}
	
	public AbstractSnapshot(EntityId entityId) {
		this.entityId = entityId;
	}
	
	public EntityId getEntityId() {
        return entityId;
    }

    public void setEntityId(EntityId key) {
        this.entityId = key;
    }

    /**
	 * Return the current EntityState (see class javadocs)
	 */
	public EntityState getEntityState() {
		return entityState;
	}

	public void setEntityState(EntityState state) {
		this.entityState = state;
	}
	
	@JsonIgnore
	public boolean isVersioned() {
		if (version == null)
			return false;
		
		return version >= 0;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public void setVersion(Long version) {
		this.version = version;
	}

    
    public void shallowCopyFrom(AbstractSnapshot copy) {
        this.entityState = copy.entityState;
        this.entityId = copy.entityId;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractSnapshot other = (AbstractSnapshot) obj;
        if (entityId == null) {
            if (other.entityId != null)
                return false;
        } else if (!entityId.equals(other.entityId))
            return false;
        return true;
    }
    
    /**
     * Use this method to determine if a property on the object
     * has explicitly been sent to null.  For the purpose of
     * sparse update we need to differentiate between a property 
     * that is null because its value was never set and a property
     * that was set to null explicitly.
     * 
     * Where the property is a BusinessKey object there are two ways
     * to set it to null:
     * 1) The value is null.
     * 2) The value contains a "/null/" string.
     * 
     * @param businessKey
     * @return
     */
    protected boolean isNull(EntityId businessKey) {
    	if (businessKey == null ||
    		businessKey.isNull()) {
			return true;
		}
    	
		return isNull((Object) businessKey);
    }

    /**
     * Use this method to determine if a property on the object
     * has explicitly been sent to null.  For the purpose of
     * sparse update we need to differentiate between a property 
     * that is null because its value was never set and a property
     * that was set to null explicitly.
     * 
     * @param object
     * @return
     */
    protected boolean isNull(Object object) {
    	if (object == null) {
    		return true;
    	} else {
    		return false;
    	}
    }

}
