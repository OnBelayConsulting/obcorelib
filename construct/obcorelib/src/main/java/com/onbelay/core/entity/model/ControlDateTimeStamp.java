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

import jakarta.persistence.Column;

import java.time.LocalDateTime;

/**
 * Component used to capture Audit stamp information.
 *
 */

public class ControlDateTimeStamp {
	private LocalDateTime modifiedDate = LocalDateTime.now();
	private String modifiedBy = "oradev";
	private LocalDateTime createdDate = LocalDateTime.now();
	private String createdBy = "oradev";

	
	public ControlDateTimeStamp() { }
	
	/**
	 * Copy Constructor
	 * @param copy - copy to create from
	 */
	public ControlDateTimeStamp(ControlDateTimeStamp copy) {
		modifiedBy = copy.modifiedBy;
		modifiedDate = copy.modifiedDate;
		createdBy = copy.createdBy;
		createdDate = copy.createdDate;
	}
	
	
	/**
	 * Set this audit from the given audit
	 * @param copy
	 */
	public void setFrom(ControlDateTimeStamp copy) {
		modifiedBy = copy.modifiedBy;
		modifiedDate = copy.modifiedDate;
		createdBy = copy.createdBy;
		createdDate = copy.createdDate;
	}
	
	@Column (name="modify_dttm")
	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@Column (name="modify_user")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column (name="create_dttm")
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column (name="create_user")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * Initialize the Audit stamp to the appropriate create and update dates.
	 */
	public void initialize() {
		createdDate = LocalDateTime.now();
		modifiedDate = createdDate;
	}
	/**
	 * 
	 */
	public void update() {
		modifiedDate = LocalDateTime.now();
	}
	
	public String toString() {
		return createdBy + " on " + createdDate + " | " + modifiedBy + " on " + modifiedDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result
				+ ((modifiedDate == null) ? 0 : modifiedDate.hashCode());
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
		ControlDateTimeStamp other = (ControlDateTimeStamp) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (modifiedBy == null) {
			if (other.modifiedBy != null)
				return false;
		} else if (!modifiedBy.equals(other.modifiedBy))
			return false;
		if (modifiedDate == null) {
			if (other.modifiedDate != null)
				return false;
		} else if (!modifiedDate.equals(other.modifiedDate))
			return false;
		return true;
	}


}
