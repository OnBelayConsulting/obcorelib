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
 * 
 * Top-level runtime exception thrown. 
 *
 */
public class OBRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	private List<String> parms = new ArrayList<String>();
	
	public OBRuntimeException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

    public OBRuntimeException(String errorCode, String parm) {
        super(errorCode + " " + parm);
        this.errorCode = errorCode;
        this.parms.add(parm);
    }

    public OBRuntimeException(String errorCode, List<String> parms) {
        super(errorCode + " " + parms.toString());
        this.errorCode = errorCode;
        this.parms = parms;
    }

    public OBRuntimeException(String errorCode, String parm, RuntimeException t) {
        super(errorCode + " " + parm, t);
        this.errorCode = errorCode;
        this.parms.add(parm);
    }

    public OBRuntimeException(String errorCode, RuntimeException t) {
		super(errorCode, t);
		this.errorCode = errorCode;
	}


    public String getErrorCode() {
		return errorCode;
	}
    
    public boolean hasParms() {
    	return parms.isEmpty() != true ;
    }


    public String getAggregatedMessage() {
        StringBuffer buffer = new StringBuffer(errorCode);

        if (this.parms != null) {
            buffer.append(" ");
            buffer.append(parms);
        }

        return buffer.toString();
    }


    public List<String> getParms() {
        return parms;
    }
}