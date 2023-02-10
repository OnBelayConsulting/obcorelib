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
package com.onbelay.core.lifecycle.component;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines application lifecycle states
 *
 */
public enum ApplicationLifecycleState {
    INVALID      ("INVALID",       "Invalid state",    -1),
    SHUTTING_DOWN("SHUTTING_DOWN", "Shutting down",    -1),
	ADMIN       ("ADMIN",          "Admin state",     3),
    TESTING     ("TESTING",        "Testing state",   2),
	RUNNING     ("RUNNING",        "Running state",   5);

	private final String code;
    private final String name;
    private final int runLevel;

    private static final Map<String,ApplicationLifecycleState> lookup 
    	= new HashMap<String,ApplicationLifecycleState>();

    private static final Map<String,ApplicationLifecycleState> lookupByName 
        = new HashMap<String,ApplicationLifecycleState>();

    static {
    	for(ApplicationLifecycleState c : EnumSet.allOf(ApplicationLifecycleState.class))
         lookup.put(c.code, c);
        for(ApplicationLifecycleState c : EnumSet.allOf(ApplicationLifecycleState.class))
            lookupByName.put(c.name, c);
    }
    
	private ApplicationLifecycleState(String code, String name, int level) {
		this.code = code;
		this.name = name;
		this.runLevel = level;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
	    return name;
	}
	
	public int getRunLevel() {
        return runLevel;
    }

    public static ApplicationLifecycleState lookUp(String code) {
		return lookup.get(code.toUpperCase());
	}

    public static ApplicationLifecycleState lookUpByName(String name) {
        return lookupByName.get(name);
    }

}
