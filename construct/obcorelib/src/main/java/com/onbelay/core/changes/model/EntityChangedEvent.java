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
package com.onbelay.core.changes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onbelay.core.changes.enums.EventStatus;

import java.util.ArrayList;
import java.util.List;

public class EntityChangedEvent {

	private EventStatus eventStatus;

	private String entityName;
	
	private Integer entityId;

	private List<PropertyChangedEvent> propertyChangedEvents = new ArrayList<>();

	public EntityChangedEvent() { }
	
	public EntityChangedEvent(
			String entityName,
			Integer entityId,
			EventStatus eventStatus,
			String propertyName, 
			String previousValue,
			String newValue) {
		
		super();
		this.entityName = entityName;
		this.entityId = entityId;
		this.eventStatus = eventStatus;

		propertyChangedEvents.add(
				new PropertyChangedEvent(
						propertyName,
						previousValue,
						newValue));

	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public String getEntityName() {
		return entityName;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void addPropertyChangedEvent(PropertyChangedEvent event) {
		propertyChangedEvents.add(event);
	}

	public List<PropertyChangedEvent> getPropertyChangedEvents() {
		return propertyChangedEvents;
	}

	public void setPropertyChangedEvents(List<PropertyChangedEvent> propertyChangedEvents) {
		this.propertyChangedEvents = propertyChangedEvents;
	}
}
