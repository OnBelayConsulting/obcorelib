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

import java.util.ArrayList;
import java.util.List;


/**
 * Contains the EntityIds of the domain objects in the processed transaction.
 */
public class TransactionResult extends ErrorHoldingSnapshot {
    private static final long serialVersionUID = 1L;


    private List<Integer> ids = new ArrayList<>();

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

	public TransactionResult(Integer id) {
	    this.ids.add(id);
	}
	
	
	public TransactionResult(List<Integer> entityIds) {
        this.ids.addAll(entityIds);
    }

	@JsonIgnore
	public Integer getId() {
		if (ids.size() > 0)
			return ids.get(0);
		return null;
	}

	public void setId(Integer id) {
		ids.add(id);
	}

	@JsonIgnore
	public EntityId getEntityId() {
		if (ids.size() > 0)
			return new EntityId(ids.get(0));
		return null;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> idsIn) {
		ids.addAll(idsIn);
	}
}
