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
package com.onbelay.core.entity.snapshot;

import com.onbelay.core.exception.OBValidationException;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public abstract class AbstractDetail implements Serializable {
    private static final long serialVersionUID = 1L;
	
    protected boolean isNull(Object object) {
        return object == null;
    }

    public void validate() throws OBValidationException {
    }
    

    private boolean validateRegex(String identifier, String regex) {
    	
        String invalidCharacters = identifier.replaceAll(regex, "");
        return invalidCharacters.length() == 0;
    }

    public boolean validateValue(String value, int minLength, int maxLength) {
        if (value == null) {
            return true;
        }
        
        int length = value.length();
        if (length > maxLength || length < minLength) {
            return false;
        }
        return validateRegex(value, "[0-9a-zA-Z]+");
    }

    
    protected String filterEmptyStringToNull(String s) {
    	if (s == null)
    		return s;
    	
    	if (s.length() == 0 || s.equals(""))
    		return null;
    	
    	return s;
    }
}
