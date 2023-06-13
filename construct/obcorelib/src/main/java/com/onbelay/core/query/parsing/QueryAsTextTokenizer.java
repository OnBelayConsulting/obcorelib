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
package com.onbelay.core.query.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import com.onbelay.core.query.enums.ExpressionConnector;
import com.onbelay.core.query.enums.ExpressionOperator;
import com.onbelay.core.query.enums.TextExpressionOperator;

/**
 * Tokenizes a string into tokens based on basic matches and a stringTokenMap to map reserved words.
 * @author lefeuvrem
 *
 */
public class QueryAsTextTokenizer {
	
	private StringBuilder expressionText;

	public QueryAsTextTokenizer(String expressionText) {
		super();
		this.expressionText = new StringBuilder(expressionText);
	}
	
	public QueryAsTextTokenizer(StringBuilder expressionText) {
		super();
		this.expressionText = expressionText;
	}
	
	private static HashMap<String, Supplier<? extends ExpressionToken>> stringTokenMap = new HashMap<String, Supplier<? extends ExpressionToken>>(); 
	
	
	static {
		stringTokenMap.put(
				"NOT", 
				() -> new NotTokenHolder());
		stringTokenMap.put(
				"IS", 
				() -> new IsTokenHolder());
		stringTokenMap.put(
				"NULL", 
				() -> new NullTokenHolder());
		stringTokenMap.put(
				"TRUE", 
				() -> new BooleanHolder(Boolean.TRUE));
		stringTokenMap.put(
				"FALSE", 
				() -> new BooleanHolder(Boolean.FALSE));
		stringTokenMap.put(
				"AND", 
				() -> new ConnectorHolder(ExpressionConnector.AND));
		stringTokenMap.put(
				"OR", 
				() -> new ConnectorHolder(ExpressionConnector.OR));
		stringTokenMap.put(
				"IN", 
				() -> new INTokenHolder());
		stringTokenMap.put(
				"LIKE", 
				() -> new OperatorHolder(ExpressionOperator.LIKE));
		stringTokenMap.put(
				"EQ",
				() -> new OperatorHolder(ExpressionOperator.EQUALS));
		stringTokenMap.put(
				"GT",
				() -> new OperatorHolder(ExpressionOperator.GREATER_THAN));
		stringTokenMap.put(
				"GE",
				() -> new OperatorHolder(ExpressionOperator.GREATER_THAN_OR_EQUALS));
		stringTokenMap.put(
				"LT",
				() -> new OperatorHolder(ExpressionOperator.LESS_THAN));
		stringTokenMap.put(
				"LE",
				() -> new OperatorHolder(ExpressionOperator.LESS_THAN_OR_EQUALS));
		stringTokenMap.put(
				"CONTAINS",
				() -> new TextOperatorHolder(TextExpressionOperator.CONTAINS));
		stringTokenMap.put(
				"STARTSWITH",
				() -> new TextOperatorHolder(TextExpressionOperator.STARTS_WITH));

	}
	
	
	public List<ExpressionToken> tokenize() {
		
		QueryAsTextTokenReader reader = new QueryAsTextTokenReader(expressionText);
		
		ArrayList<ExpressionToken> tokens = new ArrayList<ExpressionToken>();
		
		while (reader.hasNext()) {
			
			switch (reader.peekNext()) {
			
			case QueryAsTextTokenReader.IS_WHITE_SPACE :
				reader.read();
				break;
			
			case QueryAsTextTokenReader.IS_EQUALS :
				tokens.add(
						new OperatorHolder(ExpressionOperator.EQUALS));
				reader.read();
				break;
				
			case QueryAsTextTokenReader.IS_GREATER_THAN :
				tokens.add(
						new OperatorHolder(ExpressionOperator.GREATER_THAN));
				reader.read();
				break;
				
			case QueryAsTextTokenReader.IS_LESS_THAN :
				tokens.add(
						new OperatorHolder(ExpressionOperator.LESS_THAN));
				reader.read();
				break;
				
			case QueryAsTextTokenReader.IS_OPEN_BRACKET :
				tokens.add(
						new OpenBracketHolder());
				reader.read();
				break;
				
			case QueryAsTextTokenReader.IS_CLOSE_BRACKET :
				tokens.add(
						new CloseBracketHolder());
				reader.read();
				break;
				
			case QueryAsTextTokenReader.IS_BANG :
				tokens.add(
						new NotTokenHolder());
				reader.read();
				break;
				
				
			case QueryAsTextTokenReader.IS_NUMBER :
				tokens.add(
						new NumberHolder(
								reader.readNumber()));
				break;
				
			case QueryAsTextTokenReader.IS_SINGLE_QUOTE :
				tokens.add(
						new ParameterHolder(
								reader.readQuotedString()));
				break;		
				
			case QueryAsTextTokenReader.IS_LETTER : 	
				tokens.add(
						processString(
								reader.readString()));
				break;
				
			default :
				reader.read();
				break;		
						
			}
			
		}
		
		return tokens;
	}
	

	private ExpressionToken processString(String stringIn) {
		
		if (stringTokenMap.containsKey(stringIn.toUpperCase())) {
			return stringTokenMap
					.get(stringIn.toUpperCase())
					.get();
		} 
		
		return new ColumnNameHolder(stringIn);
	}
	
	
}
