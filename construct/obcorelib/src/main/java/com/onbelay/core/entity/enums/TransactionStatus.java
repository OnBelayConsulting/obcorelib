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
 * Defines Transaction status
 *
 */
public enum TransactionStatus {
	SUCCESSFUL ("S", "SUCCESS"),
	ERRORS     ("E", "ERRORS"),  
    FAILURE    ("F", "FAILURE");  

	private final String code;
    private final String name;

    private static final Map<String,TransactionStatus> lookup 
    	= new HashMap<String,TransactionStatus>();

    private static final Map<String,TransactionStatus> lookupByName 
        = new HashMap<String,TransactionStatus>();

    static {
    	for(TransactionStatus c : EnumSet.allOf(TransactionStatus.class))
         lookup.put(c.code, c);
        for(TransactionStatus c : EnumSet.allOf(TransactionStatus.class))
            lookupByName.put(c.name, c);
    }
    
	private TransactionStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
	    return name;
	}
	
	public static TransactionStatus lookUp(String code) {
		return lookup.get(code);
	}

    public static TransactionStatus lookUpByName(String name) {
        return lookupByName.get(name);
    }

}
