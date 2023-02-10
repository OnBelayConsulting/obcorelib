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
public class JSRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	private List<String> parms = new ArrayList<String>();
	
	public JSRuntimeException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

    public JSRuntimeException(String errorCode, String parm) {
        super(errorCode + " " + parm);
        this.errorCode = errorCode;
        this.parms.add(parm);
    }

    public JSRuntimeException(String errorCode, String parm, Throwable t) {
        super(errorCode + " " + parm, t);
        this.errorCode = errorCode;
        this.parms.add(parm);
    }

    public JSRuntimeException(JSValidationException bookrunnerValidationException) {
        super(bookrunnerValidationException.getAggregatedMessage(), bookrunnerValidationException);
        this.errorCode = bookrunnerValidationException.getErrorCode();
        this.parms = bookrunnerValidationException.getParms();
    }
	


    public JSRuntimeException(String errorCode, Throwable t) {
		super(errorCode, t);
		this.errorCode = errorCode;
	}

    
	@Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return getErrorMessage(); 
    }

    public String getErrorCode() {
		return errorCode;
	}
    
    public boolean hasParms() {
    	return parms.isEmpty() != true ;
    }
    
	
	public List<String> getParms() {
        return parms;
    }


    public String getErrorMessage() {
	    StringBuffer buffer = new StringBuffer(errorCode);
	    
        
        if (hasParms()) {
            buffer.append(" ");
            buffer.append(parms);
        }
        
        return buffer.toString();
	}
}