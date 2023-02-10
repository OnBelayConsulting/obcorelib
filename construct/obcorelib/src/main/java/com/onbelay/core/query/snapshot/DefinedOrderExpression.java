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

import com.onbelay.core.query.enums.ExpressionOrder;

public class DefinedOrderExpression {

	private String columnName;
	
	private ExpressionOrder order = ExpressionOrder.ASCENDING;

	
	
	public DefinedOrderExpression(String columnName) {
		super();
		this.columnName = columnName;
	}

	public DefinedOrderExpression(String columnName, ExpressionOrder order) {
		super();
		this.columnName = columnName;
		this.order = order;
	}

	public String getColumnName() {
		return columnName;
	}

	public ExpressionOrder getOrder() {
		return order;
	}
	
	public String toString() {
		return columnName + " " + order.getCode();
	}
	
}
