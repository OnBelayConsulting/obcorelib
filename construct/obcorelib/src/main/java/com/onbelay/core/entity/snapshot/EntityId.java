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

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onbelay.core.entity.enums.EntityIdStatus;

/**
 * Encapsulates an EntityId that may be:
 * 
 *   <ul>
 *   <li> VALID - has a valid id
 *   <li> IS_NULL - the business key is set to null signifying that the midtier should set the corresponding reference to null
 *   <li> IS_INVALID - the business key is set to invalid. In other words a serializer has determined that the passed in reference was
 *   invalid. An invalid business key may have an additional component that contains the initial invalid reference.
 *   </ul>
 *                  
 *
 */
public class EntityId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String code;
    private String description;
    private boolean isDeleted;

    private EntityIdStatus status = EntityIdStatus.VALID;
    
    public EntityId() { }
    
    
    public EntityId(Integer id) {
    	this.id = id;
    }

    public EntityId(String code) {
        this.code = code;
    }

    public EntityId(Integer id, String code, String description, boolean isDeleted) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.isDeleted = isDeleted;
    }

    public static EntityId makeNullEntityId() {
        EntityId id = new EntityId();
        id.status = EntityIdStatus.IS_NULL;
        return id;
    }

    public static EntityId makeInvalidEntityId() {
        EntityId id = new EntityId();
        id.status = EntityIdStatus.INVALID;
        return id;
    }

    @JsonIgnore
    public boolean isSet() {
        return id != null;
    }

    @JsonIgnore
    public boolean isValid() {
        return status == EntityIdStatus.VALID;
    }

    @JsonIgnore
    public boolean isNotNull() {
        return status != EntityIdStatus.IS_NULL;
    }
    
    @JsonIgnore
    public boolean isNull() {
        return status == EntityIdStatus.IS_NULL;
    }

    @JsonIgnore
    public boolean isInvalid() {
        return status != EntityIdStatus.VALID;
    }
    
    public Integer getId() {
    	return id;
    }

    public void setId(Integer id) {
    	this.id = id;
    }

    public EntityIdStatus getStatus() {
		return status;
	}
    
    public void setStatus(EntityIdStatus status) {
    	this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    /**
     * Create a key using an existing key
     * @param key
     */
    public EntityId(EntityId key) {
        this.id = key.id;
        this.code = key.code;
        this.status = key.status;
        this.isDeleted = key.isDeleted;
    }

    
    public String toString() {
        return id.toString();
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityId other = (EntityId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (status != other.status)
			return false;
		return true;
	}


}
