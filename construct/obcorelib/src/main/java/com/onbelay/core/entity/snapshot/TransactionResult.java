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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onbelay.core.utils.ErrorMessageFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Contains the EntityIds of the domain objects in the processed transaction.
 */
public class TransactionResult extends ErrorHoldingSnapshot {
    private static final long serialVersionUID = 1L;


    private List<EntityId> entityIds = new ArrayList<EntityId>();

	public TransactionResult() { }

	public TransactionResult(String errorMessage) {
		super(errorMessage);
	}

	public TransactionResult(String errorCode, boolean isPermissionException) {
		super(errorCode, isPermissionException);
	}

	public TransactionResult(String errorMessage, List<String> parameters) {
		super(errorMessage, parameters);
	}

	public TransactionResult(EntityId entityId) {
	    this.entityIds.add(entityId);
	}
	
	public TransactionResult(Integer id) {
	    this.entityIds.add(new EntityId(id));
	}
	
	
	public TransactionResult(List<EntityId> entityIds) {
        this.entityIds = entityIds;
    }

	@JsonIgnore
    public EntityId getEntityId() {
	    if (entityIds.size() >0)
	        return entityIds.get(0);
	    else
	        return null;
	}
	
	public void addEntityId(EntityId entityId) {
		this.entityIds.add(entityId);
	}
	
	public void addEntityId(Integer id) {
		this.entityIds.add(new EntityId(id));
	}
	
	public void addEntityIds(List<EntityId> entityIdsIn) {
	    entityIds.addAll(entityIdsIn);
	}

    public List<EntityId> getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(List<EntityId> entityIds) {
        this.entityIds = entityIds;
    }

}
