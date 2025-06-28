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
package com.onbelay.core.query.snapshot;

import com.onbelay.core.query.enums.ExpressionOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DefinedWhereExpression implements ExpressionElement {

	private String columnName;
	
	private ExpressionOperator operator;
	
	private Object value;
	
	private String parameterReference;

	public DefinedWhereExpression(String columnName, ExpressionOperator operator, Object value) {
		super();
		this.columnName = columnName;
		this.operator = operator;
		this.value = value;
	}

	public DefinedWhereExpression(String columnName, ExpressionOperator operator) {
		super();
		this.columnName = columnName;
		this.operator = operator;
	}

	public String getColumnName() {
		return columnName;
	}

	public ExpressionOperator getOperator() {
		return operator;
	}

	public Object getValue() {
		return value;
	}
	
	public boolean hasValue() {
		return value != null;
	}

	public String getParameterReference() {
		return parameterReference;
	}

	public void setParameterReference(String parameterReference) {
		this.parameterReference = parameterReference;
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder(columnName);
		
		builder.append(" ");
		builder.append(operator.getCode());

		if (hasValue()) {
			builder.append(" ");

			if (value instanceof List) {
				List list = (List) value;
				builder.append("(");
				
				formatValue(builder, list.get(0));
				
				for (int i=1; i < list.size(); i++) {
					builder.append(",");
					formatValue(builder, list.get(i));
				}
				builder.append(")");
			} else {
				formatValue(builder, value);
			}
			
		}
		
		return builder.toString();
	}
	
	private void formatValue(StringBuilder builder, Object value) {
		if (value instanceof String) {
			builder.append("'");
			builder.append(value);
			builder.append("'");
		} else if (value instanceof LocalDate) {
			builder.append("'");
			builder.append(value.toString());
			builder.append("'");
		} else if (value instanceof LocalDateTime) {
			builder.append("'");
			builder.append(value.toString());
			builder.append("'");
		} else {
			builder.append(value.toString());
		}
	}
	
}
