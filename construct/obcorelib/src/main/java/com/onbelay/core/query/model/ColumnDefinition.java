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
package com.onbelay.core.query.model;

import com.onbelay.core.query.enums.ColumnDataType;

public class ColumnDefinition {

	private String name;
	
	private ColumnDataType dataType = ColumnDataType.STRING;
	
	private String path;

	

	public ColumnDefinition(String name, ColumnDataType dataType, String path) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public ColumnDataType getDataType() {
		return dataType;
	}

	public String getPath() {
		return path;
	}
	
	
}
