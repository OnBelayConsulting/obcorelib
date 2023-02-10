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
import java.util.Map;


public abstract class ComplexBaseListItem implements ComplexListItem {
    private static final long serialVersionUID = 1L;
	
	protected Map<String, String> values = new HashMap<String, String>();

	public String valueAt(String key) {
        return values.get(key);
    }

    public Map<String, String> getListValues() {
        return values;
    }

    public void setListValues(Map<String, String> values) {
		this.values = values;
	}


}
