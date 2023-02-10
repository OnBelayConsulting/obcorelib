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
package com.onbelay.core.lifecycle.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum LifecycleApplicationSettings {
	APPLICATION_STATUS						("APPLICATION_STATUS", true);

    private final String name;
    private final boolean readAlways;

    private static final Map<String,LifecycleApplicationSettings> lookupByName 
        = new HashMap<String,LifecycleApplicationSettings>();

    static {
        for(LifecycleApplicationSettings c : EnumSet.allOf(LifecycleApplicationSettings.class))
            lookupByName.put(c.name, c);
    }
    
	private LifecycleApplicationSettings(String name, boolean readAlways) {
		this.name = name;
		this.readAlways = readAlways;
	}
	
    public String getName() {
        return name;
    }
    
    public static LifecycleApplicationSettings lookUpByName(String name) {
        return lookupByName.get(name);
    }

	public boolean isReadAlways() {
		return readAlways;
	}


}
