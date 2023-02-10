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
public enum ExpressionOperator {
	EQUALS  ("="),
	NOT_EQUALS  ("!="),
	IS_NULL  ("IS NULL"),
	IS_NOT_NULL  ("IS NOT NULL"),
	GREATER_THAN  (">"),
	LESS_THAN  ("<"),
	IN  ("IN"),
	NOT_IN  ("NOT IN"),
    LIKE ("LIKE"),
    NOT_LIKE ("NOT LIKE");

	private final String code;

    private static final Map<String,ExpressionOperator> lookup 
    	= new HashMap<String,ExpressionOperator>();

    static {
    	for(ExpressionOperator c : EnumSet.allOf(ExpressionOperator.class))
         lookup.put(c.code, c);
    }
    
	private ExpressionOperator(String code) {
		this.code = code;
	}
	
	/**
	 * Returns true if this operator compares to a value such as equals. False if it doesn't like is null or is not null.
	 * @return
	 */
	public boolean comparesToValue() {
		return ! (this == ExpressionOperator.IS_NULL ||this ==  ExpressionOperator.IS_NOT_NULL);
	}
	
	public String getCode() {
		return code;
	}
	
	public static ExpressionOperator lookUp(String code) {
		return lookup.get(code);
	}
    
	public String toString() {
		return code;
	}

}
