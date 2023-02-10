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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores a collection of business keys for a complex domain object. The primary domain object is identified by the queryEntityName and
 * businessKey and the child business keys are stored in the childBusinessKeyMap.
 * @author canmxf
 *
 */
public class EntityIdMap {
    
    private String entityName;
    private EntityId entityId;
    
    private Map<String, List<EntityId>> childBusinessKeyMap = new HashMap<String, List<EntityId>>();

    public EntityIdMap(String entityName) {
        this.entityName = entityName;
    }
    
    public EntityIdMap(String entityName, EntityId key) {
        this.entityName = entityName;
        this.entityId = key;
    }
    
    public void addChildBusinessKeys(String childEntityName, List<EntityId> childKeys) {
        childBusinessKeyMap.put(childEntityName, childKeys);
    }
    
    public List<EntityId> getChildBusinessKeys(String childEntityName) {
        return childBusinessKeyMap.get(childEntityName);
    }
    
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public EntityId getEntityId() {
        return entityId;
    }

    public void setEntityId(EntityId businessKey) {
        this.entityId = businessKey;
    }

    public Map<String, List<EntityId>> getChildBusinessKeyMap() {
        return childBusinessKeyMap;
    }

    public void setChildBusinessKeyMap(Map<String, List<EntityId>> childBusinessKeyMap) {
        this.childBusinessKeyMap = childBusinessKeyMap;
    }

    
    
}
