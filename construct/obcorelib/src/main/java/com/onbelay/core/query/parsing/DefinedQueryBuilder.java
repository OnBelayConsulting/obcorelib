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
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.onbelay.core.query.enums.ExpressionOperator;
import com.onbelay.core.query.enums.ExpressionOrder;
import com.onbelay.core.query.exception.DefinedQueryException;
import com.onbelay.core.query.snapshot.DefinedOrderByClause;
import com.onbelay.core.query.snapshot.DefinedOrderExpression;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.DefinedWhereClause;
import com.onbelay.core.query.snapshot.DefinedWhereExpression;
import com.onbelay.core.query.snapshot.ExpressionElement;
import com.onbelay.core.query.snapshot.UnknownExpressionElement;

/**
 * Builds a DefinedQuery from a query expressed in text in the form of:
 * 
 * WHERE ... 
 * 
 * WHERE ... ORDER BY ...
 * 
 * ORDER BY ...
 * 
 * The Where clause examples:
 * 
 * WHERE columnName = 'value'
 * 
 * Order By examples :
 * 
 * ORDER BY columnName1 desc, columnName2 asc
 * 
 * NOTE
 * Date and dateTime values are handled as strings and then converted when needed.
 * Date should be in the ISO date format YYYY-MM-DD
 * 
 * Date and Time should be in the ISO datetime format YYYY-MM-DDThh:mm:ss 
 * 
 * @author lefeu
 *
 */
public class DefinedQueryBuilder {
	private static final Logger logger = LogManager.getLogger(DefinedQueryBuilder.class);
	
	private static final String WHERE = "WHERE";
	private static final String ORDERBY = "ORDER BY";
	
	private String expressionText ;
	private String entityName;

	public DefinedQueryBuilder(String entityName, String expressionText) {
		super();
		this.entityName = entityName;
		this.expressionText = expressionText;
	}

	public DefinedQuery build() {

		if (expressionText == null) {
			return new DefinedQuery(entityName);
		}

		expressionText = expressionText.trim();
		
		if (expressionText.contentEquals(""))
			return new DefinedQuery(entityName);

		if (expressionText.equalsIgnoreCase("WHERE"))
			return new DefinedQuery(entityName);

		boolean hasWhereClause = true;
		boolean hasOrderClause = true;
		
		int indexOfWhereClause = expressionText.indexOf(WHERE);
		if (indexOfWhereClause < 0)
			indexOfWhereClause = expressionText.indexOf(WHERE.toLowerCase());
		if (indexOfWhereClause <0)
			hasWhereClause = false;
		
		int indexOfOrderByClause = expressionText.indexOf(ORDERBY);
		if (indexOfOrderByClause < 0)
			indexOfOrderByClause = expressionText.indexOf(ORDERBY.toLowerCase());
		if (indexOfOrderByClause <0)
			hasOrderClause = false;
		
		DefinedWhereClause whereClause = new DefinedWhereClause();
		if (hasWhereClause) {
			
			int endOfWhereExpression = expressionText.length();
			
			if (hasOrderClause)
				endOfWhereExpression = indexOfOrderByClause - 1;
			
			String whereExpressionText = expressionText.substring(
					indexOfWhereClause + 6, 
					endOfWhereExpression);
			
			QueryAsTextTokenizer tokenizer = new QueryAsTextTokenizer(whereExpressionText);
			List<ExpressionToken> tokens = tokenizer.tokenize();
			
			whereClause = buildWhereClause(tokens);
			
		}
		
		DefinedOrderByClause orderByClause = new DefinedOrderByClause();
		if (hasOrderClause) {
			
			orderByClause = buildOrderByClause(
					expressionText.substring(
							indexOfOrderByClause + 9, 
							expressionText.length()));
		}
		
		return new DefinedQuery(
				entityName, 
				whereClause, 
				orderByClause);
		
	}
	
	private DefinedOrderByClause buildOrderByClause(String orderByText) {
		
		String[] orderExpressions = orderByText.split(",");
		
		DefinedOrderByClause orderByClause = new DefinedOrderByClause();
		
		for (String s : orderExpressions)  {
			
			String token = s.trim();
			String[] parts = token.split(" ");
			
			if (parts.length == 1) {
				orderByClause.addOrderExpression(
						new DefinedOrderExpression(parts[0]));
				
			} else {
				ExpressionOrder order = ExpressionOrder.lookUpByToken(parts[1]);
				
				if (order == null) {
					logger.error("unknown order operator: " + parts[1]);
					throw new DefinedQueryException("unknown order operator: " + parts[1] );
				}
				
				orderByClause.addOrderExpression(
						new DefinedOrderExpression(parts[0], order));
			}
			
		}
		return orderByClause;
	}
	
	private DefinedWhereClause buildWhereClause(List<ExpressionToken> tokens) {
		
		checkForMatchedBrackets(tokens);
		
		convertToHigherOrderOperators(tokens);
		convertInLists(tokens);
		
		List<ExpressionToken> convertedTokens 
			= tokens
				.stream()
				.filter(ExpressionToken::isNotProcessed)
				.collect(Collectors.toList());
		
		createWhereExpressions(convertedTokens);
		
		List<ExpressionElement> elements 
			= convertedTokens
				.stream()
				.filter(ExpressionToken::isNotProcessed)
				.map(this::mapToWhereClauseElements)
				.collect(Collectors.toList());
		
		return new DefinedWhereClause(elements);
	}

	
	private ExpressionElement mapToWhereClauseElements(ExpressionToken token) {
		
		if (token instanceof WhereExpressionHolder)
			return ((WhereExpressionHolder)token).getWhereExpression();
		
		if (token instanceof ConnectorHolder) {
			return ((ConnectorHolder)token).getConnector();
		}
		
		if (token instanceof BracketHolder) {
			return ((BracketHolder)token).getBracket();
		}
		
		logger.error("unknown token " + token.getValue().toString());
		return new UnknownExpressionElement(token.getValue().toString());
	}
	
	private void createWhereExpressions(List<ExpressionToken> tokens) {
		
		for (int i=0; i < tokens.size(); i++) {
			
			ExpressionToken token = tokens.get(i);
			if (token.isProcessed())
				continue;
			
			if (token instanceof ColumnNameHolder) {
				tokens.add(i, createExpression(
						i, 
						tokens));
			}
			
		}
		
	}
	
	
	private WhereExpressionHolder createExpression(int i, List<ExpressionToken> tokens) {

		ExpressionToken token = tokens.get(i);

		token.setProcessed(true);
		
		ColumnNameHolder column = (ColumnNameHolder) token;
		
		ExpressionToken nextToken = nextUnprocessedToken( i + 1, tokens);
		
		if (nextToken == null ) {
			logger.error("Column name must be followed by an operator");
			throw new DefinedQueryException("Column name must be followed by an operator");
		}
		
		nextToken.setProcessed(true);
		
		if (nextToken instanceof OperatorHolder == false) {
			logger.error("Column name must be followed by an operator");
			throw new DefinedQueryException("Column name must be followed by an operator");
		}

		ExpressionOperator operator = ((OperatorHolder)nextToken).getOperator();
		
		Object value = null;

		if (! (operator == ExpressionOperator.IS_NULL || operator == ExpressionOperator.IS_NOT_NULL) ) {
			ExpressionToken valueToken = nextUnprocessedToken(i + 2, tokens);
			
			if (valueToken == null) {
				logger.error("Operator: " + operator + " must have a value");
				throw new DefinedQueryException("Operator: " + operator + " must have a value");
			}
			
			valueToken.setProcessed(true);
			
			value = valueToken.getValue();
		}
		
		return new WhereExpressionHolder(
				new DefinedWhereExpression(
				column.getColumnName(), 
				operator, 
				value));

	}
	
	
	private ExpressionToken nextUnprocessedToken(int startingIndex, List<ExpressionToken> tokens) {

		if (startingIndex >= tokens.size())
			return null;
		
		for (int i = startingIndex; i <tokens.size(); i++) {
			ExpressionToken token = tokens.get(i);
			
			if (token.isNotProcessed())
				return token;
		}
		return null;
	}
	
	
	private void convertInLists(List<ExpressionToken> tokens) {
		
		for (int i=0; i < tokens.size(); i++) {
			
			ExpressionToken token = tokens.get(i);
			
			if (token.isProcessed())
				continue;

			if (token instanceof INTokenHolder) {
				InListHolder inList = handleINList(i, tokens);
				tokens.add(i, new OperatorHolder(ExpressionOperator.IN));
				tokens.add(i +1, inList);
			}
		}
			
		
	}

	private InListHolder handleINList(int i, List<ExpressionToken> tokens) {
		ExpressionToken token = tokens.get(i);
		token.setProcessed(true);
		
		ExpressionToken nextToken = nextUnprocessedToken(i + 1, tokens);
		if (nextToken == null) {
			logger.error("IN must be followed by a list (..)");
			throw new DefinedQueryException("IN must be followed by a list (..)");
		}
		
		if (nextToken instanceof OpenBracketHolder == false) {
			logger.error("IN must be followed by a list (..)");
			throw new DefinedQueryException("IN must be followed by a list (..)");
		}
		nextToken.setProcessed(true);

		if ( (i+2) > tokens.size()) {
			throw new DefinedQueryException("IN must be followed by a list with at least one item (..)");
		}

		ExpressionToken nextNextToken = nextUnprocessedToken(i + 2, tokens);
		if (nextNextToken == null) {
			logger.error("IN must be followed by a list (..)");
			throw new DefinedQueryException("IN must be followed by a list (..)");
		}

		
		// find next bracket
		ArrayList<ExpressionToken> items = new ArrayList<ExpressionToken>();
		int indexOfCloseBracket = -1;
		for (int j = i+2; i < tokens.size(); j++) {
			ExpressionToken itemToken = tokens.get(j);
			
			if (itemToken.isProcessed())
				continue;
			
			itemToken.setProcessed(true);
			if (itemToken instanceof CloseBracketHolder) {
				indexOfCloseBracket = j;
				break;
			} else {
				items.add(itemToken);
			}
		}
		
		if (indexOfCloseBracket < 0 ) {
			logger.error("Missing ')' in IN list");
			throw new DefinedQueryException("Missing ')' in IN list");
		}
		
		if (items.isEmpty()) {
			logger.error("IN Lists must have at least one element");
			throw new DefinedQueryException("IN Lists must have at least one element");
		}
		
		return new InListHolder(items);

	}
	
	private void convertToHigherOrderOperators(List<ExpressionToken> tokens) {
		
		for (int i = 0; i < tokens.size(); i++) {
			
			ExpressionToken token = tokens.get(i);
			
			if (token.isProcessed())
				continue;
			
			if (token instanceof IsTokenHolder) {
				tokens.add(
						i, 
						handleISOperator(i, tokens));

			} else if (token instanceof NotTokenHolder) {
				tokens.add(
						i,
						handleNOTOperator(i, tokens));
			}
			
		}
		
	}
	
	
	private ExpressionToken handleNOTOperator(int i, List<ExpressionToken> tokens) {
		ExpressionToken token = tokens.get(i);
		token.setProcessed(true);
		
		ExpressionToken nextToken = nextUnprocessedToken(i + 1, tokens);
		
		if (nextToken == null) {
			logger.error("NOT must be followed by an '=', '<', etc. ");
			throw new DefinedQueryException("NOT must be followed by an '=', '<', etc. ");
		}
		
		if (nextToken instanceof OperatorHolder == false) {
			logger.error("NOT must be followed by an '=', '<', etc. ");
			throw new DefinedQueryException("NOT must be followed by an '=', '<', etc. ");
		}

		nextToken.setProcessed(true);
		
		ExpressionOperator operator = ((OperatorHolder)nextToken).getOperator();
		
		switch (operator) {
		
			case EQUALS :
				return new OperatorHolder(ExpressionOperator.NOT_EQUALS);
			
			case IN :
				return new NotINTokenHolder();
				
			case LIKE :
				return new OperatorHolder(ExpressionOperator.NOT_LIKE);
				
			default :
				logger.error("NOT Must be followed by an operator");
				throw new DefinedQueryException("NOT Must be followed by an operator");
		}
		
	}
	
	
	private OperatorHolder handleISOperator(int i, List<ExpressionToken> tokens) {
		ExpressionToken token = tokens.get(i);
		token.setProcessed(true);
		
		ExpressionToken nextToken = nextUnprocessedToken(i + 1, tokens);
		
		if (nextToken == null) {
			logger.error("IS must be followed by NOT or NULL");
			throw new DefinedQueryException("IS must be followed by NOT or NULL");
		}
		
		if (nextToken instanceof NullTokenHolder) {
			nextToken.setProcessed(true);
			return new OperatorHolder(ExpressionOperator.IS_NULL);
			
		} else if (nextToken instanceof NotTokenHolder) {
			nextToken.setProcessed(true);
			
			// check for null
			if ( (i + 2) > tokens.size()) {
				logger.error("IS NOT must be followed by a NULL");
				throw new DefinedQueryException("IS NOT must be followed by a NULL");
			}
			
			ExpressionToken nextNextToken = tokens.get(i+2);
			if (nextNextToken instanceof NullTokenHolder) {
				nextNextToken.setProcessed(true);
				return new OperatorHolder(ExpressionOperator.IS_NOT_NULL);
			} else {
				logger.error("IS NOT must be followed by a NULL");
				throw new DefinedQueryException("IS NOT must be followed by a NULL");
			}
			
		} else {
			logger.error("IS must be followed by a NULL or NOT");
			throw new DefinedQueryException("IS must be followed by a NULL or NOT");
		}

	}
	
	
	private void checkForMatchedBrackets(List<ExpressionToken> tokens) {
		
		
		int totalOpenBrackets = 0;
		int totalCloseBrackets = 0;
		for (ExpressionToken token : tokens) {
			if (token.isProcessed()) 
				continue;
			
			if (token instanceof CloseBracketHolder) {
				totalCloseBrackets++;
			}
			if (token instanceof OpenBracketHolder) {
				totalOpenBrackets++;
			}
		}
			
				
		if (totalCloseBrackets != totalOpenBrackets) {
			logger.error("Mismatched brackets. Total Open: " + totalOpenBrackets + " Total Close: " + totalCloseBrackets);
			throw new DefinedQueryException("Mismatched brackets. Total Open: " + totalOpenBrackets + " Total Close: " + totalCloseBrackets);
		}
		
	}
	
	
	
}
