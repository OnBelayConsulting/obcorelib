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
package com.onbelay.core.messaging.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EventStatus {
	CREATED  ("C", "Created"),
    MODIFIED ("M", "Modified"),
	DELETED  ("D", "Deleted");

	private final String code;
	private final String name;

    private static final Map<String,EventStatus> lookup 
    	= new HashMap<String,EventStatus>();

    private static final Map<String,EventStatus> lookupByName 
        = new HashMap<String,EventStatus>();

    static {
    	for(EventStatus c : EnumSet.allOf(EventStatus.class))
         lookup.put(c.code, c);
        for(EventStatus c : EnumSet.allOf(EventStatus.class))
         lookupByName.put(c.name, c);
    }
    
	private EventStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
    public String getName() {
        return name;
    }
    
	public static EventStatus lookUp(String code) {
		return lookup.get(code);
	}

    public static EventStatus lookUpByName(String name) {
        return lookupByName.get(name);
    }
    
    public static EventStatus lookUpByNameOrCode(String name) {
    	EventStatus rule = EventStatus.lookUp(name);
		if (rule != null) {
			return rule; 
		} else {
			return lookupByName.get(name);
		}
    }


}
