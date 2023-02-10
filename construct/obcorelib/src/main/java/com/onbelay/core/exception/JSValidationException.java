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
package com.onbelay.core.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Thrown to indicate a business validation has failed.
 *
 */
public class JSValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	private List<String> parms = new ArrayList<String>();;
	
	public JSValidationException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

    public JSValidationException(String errorCode, String parm) {
        super(errorCode);
        this.errorCode = errorCode;
        this.parms.add(parm);
    }


    public JSValidationException(String errorCode, Throwable t) {
		super(errorCode, t);
		this.errorCode = errorCode;
	}

    public String getAggregatedMessage() {
        StringBuffer buffer = new StringBuffer(errorCode);
        
        if (this.parms != null) {
            buffer.append(" ");
            buffer.append(parms);
        }
        
        return buffer.toString();
    }

    public String getAggregatedMessageDetails() {
        
    	StringBuffer buffer = new StringBuffer();

                
        return buffer.toString();
    }
        
	public String getErrorCode() {
		return errorCode;
	}

    /**
     * @return
     */
    public List<String> getParms() {
        return this.parms;
    }	
}