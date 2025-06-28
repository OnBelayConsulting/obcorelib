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

import com.onbelay.core.query.enums.ExpressionBracket;
import com.onbelay.core.query.enums.ExpressionConnector;

import java.util.ArrayList;
import java.util.List;

public class DefinedWhereClause {
	private static final String PARM_PREFIX = "p";
	private List<ExpressionElement> elements = new ArrayList<ExpressionElement>();
	private int lastParameterCounter = 1;
	private boolean hasParameters = false;
	public DefinedWhereClause() {
		
	}
	
	public DefinedWhereClause(List<ExpressionElement> elements) {
		super();
		this.elements = elements;
		
		for (ExpressionElement e : elements) {
			if (e instanceof DefinedWhereExpression)
				addParameter((DefinedWhereExpression) e);
		}
		
	}

	public void surroundWithBrackets() {
		if (hasExpressions() == false)
			return;
		elements.add(0, ExpressionBracket.OPEN);

		elements.add(ExpressionBracket.CLOSE);
	}

	public void addBracket(ExpressionBracket bracket) {
		elements.add(bracket);
	}
	
	public void addConnector(ExpressionConnector connector) {
		elements.add(connector);
	}
	
	public void addExpression(DefinedWhereExpression expression) {
		
		elements.add(expression);
		
		addParameter(expression);
		
	}
	
	private void addParameter(DefinedWhereExpression expression) {
		if (expression.hasValue()) {
			String reference = nextParameterReference();
			hasParameters = true;
			expression.setParameterReference(reference);
		}
		
	}
	
	public List<ExpressionElement> getElements() {
		return elements;
	}
	
	public List<DefinedWhereExpression> getExpressions() {
		ArrayList<DefinedWhereExpression> expressions = new ArrayList<DefinedWhereExpression>();
		for (ExpressionElement e : elements) {
			if (e instanceof DefinedWhereExpression)
				expressions.add((DefinedWhereExpression) e);
		}
		
		return expressions;
	}
	
	private String nextParameterReference() {
		String p =  PARM_PREFIX + lastParameterCounter;
		lastParameterCounter++;
		return p;
	}
	
	public boolean hasExpressions() {
		return elements.isEmpty() == false;
	}
	
	public String toString() {
		
		if (elements.isEmpty())
			return " ";
		
		StringBuffer buffer = new StringBuffer("WHERE ");
		ExpressionElement element = elements.get(0);
		buffer.append(element.toString());
		
		for (int i=1; i < elements.size(); i++) {
			
			if (element instanceof ExpressionBracket) {
				ExpressionBracket bracket = (ExpressionBracket) element;
				if (bracket != ExpressionBracket.OPEN) {
					buffer.append(" ");
				}
			} else {
				buffer.append(" ");
			}
			
			element = elements.get(i);
			
			if (element instanceof ExpressionBracket) {
				ExpressionBracket bracket = (ExpressionBracket) element;
				if (bracket == ExpressionBracket.CLOSE) {
					int last = buffer.length() - 1;
					buffer.deleteCharAt(last);
				}
			}
			buffer.append(element.toString());
		}
		
		return buffer.toString();
	}

	public boolean hasParameters() {
		return hasParameters;
	}
	
}
