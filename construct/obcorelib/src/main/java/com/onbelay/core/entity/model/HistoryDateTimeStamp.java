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

import javax.persistence.Column;

import com.onbelay.core.utils.DateUtils;

/**
 *
 */
public class HistoryDateTimeStamp {
	protected LocalDateTime validFromDateTime = LocalDateTime.now();
	protected LocalDateTime validToDateTime = LocalDateTime.of(1, 1, 1, 0, 0);
	protected String lastUserid = "System";
	
	public HistoryDateTimeStamp() { }
	
	/**
	 * Copy Constructor
	 * @param copy - copy  to create from
	 */
	public HistoryDateTimeStamp(HistoryDateTimeStamp copy) {
		this.validFromDateTime = copy.validFromDateTime;
		this.validToDateTime = copy.validToDateTime;
	}
	
	@Column (name="VALID_FROM_DATETIME")
	public LocalDateTime getValidFromDateTime() {
		return validFromDateTime;
	}
	public void setValidFromDateTime(LocalDateTime validFromDateTime) {
		this.validFromDateTime = validFromDateTime;
	}
	@Column (name="VALID_TO_DATETIME")
	public LocalDateTime getValidToDateTime() {
		return validToDateTime;
	}
	public void setValidToDateTime(LocalDateTime validToDateTime) {
		this.validToDateTime = validToDateTime;
	}
	/**
	 * Initialize this history timestamp
	 */
	public void initialize(LocalDateTime validFromDateTime) {
		this.validFromDateTime = validFromDateTime;
		this.validToDateTime = DateUtils.getValidToDateTime();
	}
	
	@Column(name="UPDATED_BY")
	public String getLastUserid() {
        return lastUserid;
    }

    public void setLastUserid(String lastUserid) {
        this.lastUserid = lastUserid;
    }

    public String toString() {
		return " From " + validFromDateTime + " to " + validToDateTime;
	}

	/**
	 * Is valid or within range if equal to or after the valid from but before the valid to dates.
	 * @param histDate
	 * @return
	 */
	public boolean isValidFor(LocalDateTime histDate) {
		
		if (histDate.isBefore(validFromDateTime)) 
			return false;
		
		return  (histDate.isBefore(validToDateTime));
	}

	
	public void version(LocalDateTime validFromDateTime) {
		this.validFromDateTime = validFromDateTime;
	}
	
	public void stamp(LocalDateTime validFromDateTime, LocalDateTime validToDateTime) {
		this.validFromDateTime = validFromDateTime;
		this.validToDateTime = validToDateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((validFromDateTime == null) ? 0 : validFromDateTime
						.hashCode());
		result = prime * result
				+ ((validToDateTime == null) ? 0 : validToDateTime.hashCode());
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
		HistoryDateTimeStamp other = (HistoryDateTimeStamp) obj;
		if (validFromDateTime == null) {
			if (other.validFromDateTime != null)
				return false;
		} else if (!validFromDateTime.equals(other.validFromDateTime))
			return false;
		if (validToDateTime == null) {
			if (other.validToDateTime != null)
				return false;
		} else if (!validToDateTime.equals(other.validToDateTime))
			return false;
		return true;
	}

}
