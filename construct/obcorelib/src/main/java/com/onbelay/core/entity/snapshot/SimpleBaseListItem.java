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

/**
 *
 */
public class SimpleBaseListItem implements SimpleListItem {
    private static final long serialVersionUID = 1L;
    
    private String code;
	private String  description;
    private Boolean expired;
	private Integer id;
	private boolean selectable = true;

    public SimpleBaseListItem(Integer id, String code, String description, Boolean expired) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.expired = expired;
    }

    public String getCode() {
        return code;
    }
    
    public String getValue() {
        return code;
    }

    public boolean isSelectable() {
		return selectable;
	}

	public boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getExpired() {
		return expired;
	}
	
	public Integer getId() {
        return id;
    }
	
    public String toString() {
		return code;
	}
}
