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

import java.util.ArrayList;
import java.util.List;

public class DefinedOrderByClause {

	private List<DefinedOrderExpression> expressions = new ArrayList<DefinedOrderExpression>();
	
	
	public void addOrderExpression(DefinedOrderExpression e) {
		expressions.add(e);
	}
	
	public void copyIn(DefinedOrderByClause clauseIn) {
		expressions.addAll(clauseIn.getExpressions());
	}

	public boolean hasExpressions() {
		return expressions.isEmpty() == false;
	}

	public List<DefinedOrderExpression> getExpressions() {
		return expressions;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder( "ORDER BY ");
		
		DefinedOrderExpression expression = expressions.get(0);
		
		builder.append(expression.toString());
		
		for (int i=1; i < expressions.size(); i++) {
			builder.append(", ");
			
			builder.append(
					expressions.get(i).toString());
		}
		
		return builder.toString();
	}
	
}
