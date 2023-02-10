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
package com.onbelay.core.messaging.pubsubimpl;

import java.util.ArrayList;
import java.util.List;

import com.onbelay.core.messaging.model.EntityChangedEvent;
import com.onbelay.core.messaging.pubsub.EntityEventPublisher;

public class EntityEventPublisherMock implements EntityEventPublisher {

	private List<EntityChangedEvent> events = new ArrayList<EntityChangedEvent>();
	
	@Override
	public void handleEvents(List<EntityChangedEvent> events) {
		this.events.addAll(events);
	}

	@Override
	public void handleEvent(EntityChangedEvent event) {
		this.events.add(event);
	}

	public List<EntityChangedEvent> getEvents() {
		return events;
	}

	public void setEvents(List<EntityChangedEvent> events) {
		this.events = events;
	}
	
	
	

}
