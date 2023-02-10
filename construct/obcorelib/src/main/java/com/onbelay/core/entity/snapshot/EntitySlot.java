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

import com.onbelay.core.entity.snapshot.EntityId;

public class EntitySlot {

	private EntityId entityId;
	private long version;
	private String code;
	
	public EntitySlot() {
		
	}
	
	public EntitySlot(EntityId entityId, String code) {
		super();
		this.entityId = entityId;
		this.code = code;
	}

	public EntityId getEntityId() {
		return entityId;
	}

	public void setEntityId(EntityId entityId) {
		this.entityId = entityId;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}
