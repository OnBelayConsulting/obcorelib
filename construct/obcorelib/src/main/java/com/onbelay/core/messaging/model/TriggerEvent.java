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

import java.time.LocalDateTime;

public class TriggerEvent {
	
	private LocalDateTime sentDateTime;
	
	private String entityName;
	
	private String trigger;

	public LocalDateTime getSentDateTime() {
		return sentDateTime;
	}

	public void setSentDateTime(LocalDateTime sentDateTime) {
		this.sentDateTime = sentDateTime;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	
}
