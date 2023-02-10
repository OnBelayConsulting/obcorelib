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
package com.onbelay.core.messaging.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onbelay.core.messaging.enums.EventStatus;

public class EntityChangedEvent {

	private String eventStatusValue;
	
	private String entityName;
	
	private Long entityId;
	
	private String propertyName;
	
	private String previousValue;
	
	private String newValue;

	public EntityChangedEvent() { }
	
	public EntityChangedEvent(
			String entityName,
			Long entityId,
			EventStatus eventStatus, 
			String propertyName, 
			String previousValue,
			String newValue) {
		
		super();
		this.entityName = entityName;
		this.entityId = entityId;
		this.eventStatusValue = eventStatus.getCode();
		this.propertyName = propertyName;
		this.previousValue = previousValue;
		this.newValue = newValue;
	}

	
	
	public EntityChangedEvent(
			String entityName, 
			Long entityId,
			EventStatus eventStatus, 
			String propertyName, 
			String newValue) {
		super();
		this.entityName = entityName;
		this.entityId = entityId;
		this.eventStatusValue = eventStatus.getCode();
		this.propertyName = propertyName;
		this.newValue = newValue;
	}



	@JsonIgnore
	public EventStatus getEventStatus() {
		return EventStatus.lookUp(eventStatusValue);
	}



	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatusValue = eventStatus.getCode();
	}



	public String getEventStatusValue() {
		return eventStatusValue;
	}

	public void setEventStatusValue(String eventStatusValue) {
		this.eventStatusValue = eventStatusValue;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(String previousValue) {
		this.previousValue = previousValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public Long getEntityId() {
		return entityId;
	}



	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}



	@Override
	public String toString() {
		return "EntityChangedEvent [eventStatusValue=" + eventStatusValue + ", entityName=" + entityName
				+ ", propertyName=" + propertyName + ", newValue=" + newValue + "]";
	}
	
	
	
	
}
