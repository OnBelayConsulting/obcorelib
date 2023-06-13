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
package com.onbelay.core.codes.model;

import com.onbelay.core.query.enums.ColumnDataType;
import com.onbelay.core.query.model.BaseColumnDefinitions;
import com.onbelay.core.query.model.ColumnDefinition;
import com.onbelay.core.query.model.ColumnDefinitions;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component(value = "codeColumnDefinitions")
public class CodeColumnDefinitions  implements ColumnDefinitions{
	protected Map<String, ColumnDefinition> definitionsMap = new HashMap<String, ColumnDefinition>();

	public static final ColumnDefinition code = new ColumnDefinition("code", ColumnDataType.STRING, "code");
	public static final ColumnDefinition label = new ColumnDefinition("label", ColumnDataType.STRING, "label");
	public static final ColumnDefinition displayOrderNo = new ColumnDefinition("displayOrderNo", ColumnDataType.INTEGER, "displayOrderNo");

	public CodeColumnDefinitions() {
		add(code);
		add(label);
		add(displayOrderNo);
	}

	@Override
	public String getCodeName() {
		return code.getPath();
	}

	@Override
	public String getDescriptionName() {
		return label.getPath();
	}

	public ColumnDefinition get(String name) {
		return definitionsMap.get(name);
	}

	protected void add(ColumnDefinition definition) {
		definitionsMap.put(definition.getName(), definition);
	}


}
