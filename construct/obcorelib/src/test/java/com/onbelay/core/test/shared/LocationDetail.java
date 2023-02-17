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
package com.onbelay.core.test.shared;

import javax.persistence.Column;

import com.onbelay.core.entity.snapshot.AbstractDetail;
import com.onbelay.core.enums.CoreErrorCode;
import com.onbelay.core.exception.OBValidationException;

public class LocationDetail extends AbstractDetail {

	private String name;
	private String description; 
	
	
	public LocationDetail() {
	}
	
	

	public LocationDetail(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}



	public void setDefaults() {
		
	}
			

	public void validate() throws OBValidationException {
		if (name == null)
			throw new OBValidationException(CoreErrorCode.MISSING_MY_LOCATION_NAME.getCode());
	}

	@Column(name = "NAME_TXT")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION_TXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}





	public void copyFrom(LocationDetail copy) {
		
		if (copy.name != null)
			this.name = copy.name;
		
		if (copy.description != null)
			this.description = copy.description;
	}
	
}
