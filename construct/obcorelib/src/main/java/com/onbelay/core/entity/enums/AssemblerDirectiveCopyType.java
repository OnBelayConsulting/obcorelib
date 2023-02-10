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
 * Defines overall settings for assembler directives
 * @author canmxf
 *
 */
public enum AssemblerDirectiveCopyType {
    SHALLOW_COPY ("S", "SHALLOW_COPY"),
	DEEP_COPY    ("D", "DEEP_COPY"),
	TYPICAL_COPY ("T", "TYPICAL_COPY");

	private final String code;
    private final String name;

    private static final Map<String,AssemblerDirectiveCopyType> lookup 
    	= new HashMap<String,AssemblerDirectiveCopyType>();

    private static final Map<String,AssemblerDirectiveCopyType> lookupByName 
        = new HashMap<String,AssemblerDirectiveCopyType>();

    static {
    	for(AssemblerDirectiveCopyType c : EnumSet.allOf(AssemblerDirectiveCopyType.class))
            lookup.put(c.code, c);
        for(AssemblerDirectiveCopyType c : EnumSet.allOf(AssemblerDirectiveCopyType.class))
            lookupByName.put(c.name, c);
    }
    
	private AssemblerDirectiveCopyType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
    public String getName() {
        return name;
    }
    
	public static AssemblerDirectiveCopyType lookUp(String code) {
		return lookup.get(code);
	}

    public static AssemblerDirectiveCopyType lookUpByName(String name) {
        return lookupByName.get(name);
    }

}
