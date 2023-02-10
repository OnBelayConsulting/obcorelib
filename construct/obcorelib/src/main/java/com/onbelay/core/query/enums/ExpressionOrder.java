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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author lefeuvrem
 *
 */
public enum ExpressionOrder {
    DESCENDING ("DES", "DESC"),
    ASCENDING ("ASC", "ASC");

	private final String lookupToken;
	private final String code;

    private static final Map<String,ExpressionOrder> lookup 
    	= new HashMap<String,ExpressionOrder>();

    static {
    	for(ExpressionOrder c : EnumSet.allOf(ExpressionOrder.class))
         lookup.put(c.lookupToken, c);
    }
    
	private ExpressionOrder(String lookupToken, String code) {
		this.lookupToken = lookupToken;
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static ExpressionOrder lookUpByToken(String token) {
		String part = token.toUpperCase();
		if (part.length() > 3)
			part = part.substring(0, 3);
		return lookup.get(part);
	}
    
	public String toString() {
		return code;
	}

}
