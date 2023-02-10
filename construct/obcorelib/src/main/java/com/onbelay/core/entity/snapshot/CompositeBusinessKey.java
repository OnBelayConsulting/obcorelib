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

/**
 * Contains both a primary business key and any child business keys created 
 * as a result of an association.
 *
 */
public class CompositeBusinessKey {
    
    private EntityId businessKey;
    
    private ArrayList<EntityId> childKeys = new ArrayList<EntityId>();

    public EntityId getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(EntityId businessKey) {
        this.businessKey = businessKey;
    }

    public void addChildBusinessKey(EntityId childKey) {
        childKeys.add(childKey);
    }
    
    public ArrayList<EntityId> getChildKeys() {
        return childKeys;
    }

    public void setChildKeys(ArrayList<EntityId> childKeys) {
        this.childKeys = childKeys;
    }

}
