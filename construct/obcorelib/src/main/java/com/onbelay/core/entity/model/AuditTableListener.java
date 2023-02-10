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

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;


/**
 * Listens to changes in the entity object state.
 */
public class AuditTableListener {

	/**
	 * Called just before the audit object is persisted.
	 * @param history - a domain object that represents audit.
	 */
	@PrePersist
	public void prePersist(AuditAbstractEntity history) {
	    if (history.isRecentHistory())
	        return;
    
		TemporalAbstractEntity parent = history.getParent();

		history.setIsDeleted(parent.getIsDeleted());

		history.copyFrom(parent);
		history.setEntityVersion(parent.getVersion());
	}
	
	/**
	 * Called each time the object is flushed. This method is called as the entity object is updated
	 * before the transaction commits.
	 * @param history
	 */
	@PreUpdate
	public void preUpdate(AuditAbstractEntity history) {
        if (history.isRecentHistory())
            return;
		TemporalAbstractEntity parent = history.getParent();
		
		if (parent == null)
		    history.setIsDeleted(true);
		else {
		    history.setIsDeleted(parent.getIsDeleted());
		    history.copyFrom(parent);
		    history.setEntityVersion(parent.getVersion());
		}
	}
	

}
