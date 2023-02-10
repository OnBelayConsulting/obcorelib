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
package com.onbelay.core.appsetting.snapshot;


public class ApplicationSettingSnapshot {
    private String key;
    private String value;

    public ApplicationSettingSnapshot() {
    	
    }
    
    
    
	public ApplicationSettingSnapshot(String keyValue, String value) {
		this.key = keyValue;
		this.value = value;
	}



	@Override
	public String toString() {
		return key + "=" + value;
	}

	
	public String getKey() {
		return key;
	}
	public void setKey(String keyValue) {
		this.key = keyValue;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean hasValue() {
		return (value != null);
	}
	
	public Integer getValueAsInteger() {
		if (hasValue() == false)
			return null;
		return Integer.parseInt(value);
	}
	
	public Long getValueAsLong() {
		if (hasValue() == false)
			return null;
		return Long.parseLong(value);
	}
	
	public boolean getValueAsBoolean() {
		if (hasValue() == false)
			return false;
        if (value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t"))
            return true;
        else
            return false;
	}
	
}
