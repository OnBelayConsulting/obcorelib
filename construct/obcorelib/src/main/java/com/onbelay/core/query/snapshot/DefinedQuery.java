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

public class DefinedQuery {

	private String entityName;
	
	private DefinedWhereClause whereClause = new DefinedWhereClause();
	
	private DefinedOrderByClause orderByClause = new DefinedOrderByClause();

	public DefinedQuery(String entityName) {
		super();
		this.entityName = entityName;
	}

	public DefinedQuery(String entityName, DefinedWhereClause whereClause) {
		super();
		this.entityName = entityName;
		this.whereClause = whereClause;
	}

	public DefinedQuery(String entityName, DefinedWhereClause whereClause, DefinedOrderByClause orderByClause) {
		super();
		this.entityName = entityName;
		this.whereClause = whereClause;
		this.orderByClause = orderByClause;
	}

	public String getEntityName() {
		return entityName;
	}

	public DefinedWhereClause getWhereClause() {
		return whereClause;
	}
	
	

	public DefinedOrderByClause getOrderByClause() {
		return orderByClause;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder("FROM " + entityName);
		builder.append(" ");
		
		if (whereClause.hasExpressions())
			builder.append(whereClause.toString());
		
		if (whereClause.hasExpressions() && orderByClause.hasExpressions())
			builder.append(" ");
		
		if (orderByClause.hasExpressions())
			builder.append(orderByClause.toString());
		
		return builder.toString();
	}
	
}
