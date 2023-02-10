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
package com.onbelay.core.entity.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the options for finding a business day when a date falls on a weekend 
 * or holiday.
 * 
 * @author canmxf
 *
 */
public enum EntityIdStatus {
	VALID    ("VALID", "VALID"),
    INVALID  ("INVALID", "INVALID"),
	IS_NULL  ("IS_NULL",    "IS_NULL");

	private final String code;
	private final String name;

    private static final Map<String,EntityIdStatus> lookup 
    	= new HashMap<String,EntityIdStatus>();

    private static final Map<String,EntityIdStatus> lookupByName 
        = new HashMap<String,EntityIdStatus>();

    static {
    	for(EntityIdStatus c : EnumSet.allOf(EntityIdStatus.class))
         lookup.put(c.code, c);
        for(EntityIdStatus c : EnumSet.allOf(EntityIdStatus.class))
         lookupByName.put(c.name, c);
    }
    
	private EntityIdStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
    public String getName() {
        return name;
    }
    
	public static EntityIdStatus lookUp(String code) {
		return lookup.get(code);
	}

    public static EntityIdStatus lookUpByName(String name) {
        return lookupByName.get(name);
    }
    
    public static EntityIdStatus lookUpByNameOrCode(String name) {
		EntityIdStatus rule = EntityIdStatus.lookUp(name);
		if (rule != null) {
			return rule; 
		} else {
			return lookupByName.get(name);
		}
    }

}
