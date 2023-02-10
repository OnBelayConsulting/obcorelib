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

/**
 * Defines application lifecycle states
 *
 */
public enum ApplicationLifecycleAction {
    TERMINATE	("TERMINATE"),
	ADMIN       ("ADMIN"),
    START       ("START"),
	REFRESH     ("REFRESH");

	private final String code;

    private static final Map<String,ApplicationLifecycleAction> lookup 
    	= new HashMap<String,ApplicationLifecycleAction>();

    static {
    	for(ApplicationLifecycleAction c : EnumSet.allOf(ApplicationLifecycleAction.class))
         lookup.put(c.code, c);
    }
    
	private ApplicationLifecycleAction(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
    public static ApplicationLifecycleAction lookUp(String code) {
		return lookup.get(code.toUpperCase());
	}

}
