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
package com.onbelay.core.query.enums;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author lefeuvrem
 *
 */
public enum ColumnDataType {
	STRING  ("STRING", String.class),
    INTEGER ("INTEGER", Integer.class),
    BIG_DECIMAL ("BIG_DECIMAL", BigDecimal.class),
    LONG ("LONG", Long.class),
    DATE ("DATE", LocalDate.class),
    DATE_TIME ("DATE_TIME", LocalDateTime.class),
	BOOLEAN ("BOOLEAN", Boolean.class);

	private final String code;
	private final Class claz;

    private static final Map<String,ColumnDataType> lookup 
    	= new HashMap<String,ColumnDataType>();

    static {
    	for(ColumnDataType c : EnumSet.allOf(ColumnDataType.class))
         lookup.put(c.code, c);
    }
    
	private ColumnDataType(String code, Class claz) {
		this.code = code;
		this.claz = claz;
	}
	
	public String getCode() {
		return code;
	}
	
	public static ColumnDataType lookUp(String code) {
		return lookup.get(code);
	}
    

}
