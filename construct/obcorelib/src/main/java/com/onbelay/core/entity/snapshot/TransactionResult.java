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

import java.util.ArrayList;
import java.util.List;


/**
 * Contains the EntityIds of the domain objects in the processed transaction. In some cases one or more EntityIdMaps are returned
 * as well. EntityIdMaps contain a set of primary and child entity keys.
 *
 */
public class TransactionResult extends BaseTransactionResult {
    private static final long serialVersionUID = 1L;
    
    private List<EntityId> keys = new ArrayList<EntityId>();
	private List<EntityIdMap> entityIdMaps = new ArrayList<EntityIdMap>(); 
	
	public TransactionResult() { }
	
	public TransactionResult(EntityId key) {
	    this.keys.add(key);
	}
	
	public TransactionResult(Long id) {
	    this.keys.add(new EntityId(id));
	}
	
	
	public TransactionResult(List<EntityId> keys) {
        this.keys = keys;
    }

    public EntityId getKey() {
	    if (keys.size() >0)
	        return keys.get(0);
	    else
	        return null;
	}
	
	public void addKey(EntityId key) {
		this.keys.add(key);
	}
	
	public void addKey(Long id) {
		this.keys.add(new EntityId(id));
	}
	
	public void addKeys(List<EntityId> keysIn) {
	    keys.addAll(keysIn);
	}

    public List<EntityId> getKeys() {
        return keys;
    }

    public void setKeys(List<EntityId> keys) {
        this.keys = keys;
    }
    
    public boolean hasEntityIdMap() {
        return entityIdMaps.size() > 0;
    }

    public List<EntityIdMap> getEntityIdMaps() {
        return entityIdMaps;
    }

	public void setEntityIdMaps(List<EntityIdMap> businessKeyMapsIn) {
        this.entityIdMaps = businessKeyMapsIn;
        for (int i=0; i < entityIdMaps.size(); i++) {
            EntityIdMap businessKeyMap = entityIdMaps.get(i);
            keys.add(businessKeyMap.getEntityId());
        }
    }

    public EntityIdMap getEntityIdMap() {
        if (entityIdMaps.size() > 0)
            return entityIdMaps.get(0);
        else
            return null;
    }

    public void addEntityIdMap(EntityIdMap businessKeyMap) {
        this.entityIdMaps.add(businessKeyMap);
        this.keys.add(businessKeyMap.getEntityId());
    }
    
}
