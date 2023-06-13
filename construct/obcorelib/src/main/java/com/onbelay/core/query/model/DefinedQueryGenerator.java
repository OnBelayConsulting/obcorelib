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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.exception.OBRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.onbelay.core.query.enums.ColumnDataType;
import com.onbelay.core.query.exception.DefinedQueryException;
import com.onbelay.core.query.snapshot.DefinedOrderByClause;
import com.onbelay.core.query.snapshot.DefinedOrderExpression;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.DefinedWhereClause;
import com.onbelay.core.query.snapshot.DefinedWhereExpression;
import com.onbelay.core.query.snapshot.ExpressionElement;

/**
 * Generates a query in string form that can be consumed by the base entity.
 * Query column names and values are converted by a provide ColumnsDefinitions that will return a ColumnDefinition describing the hibernate path and column type.
 * <br>
 * For example if name is stored in a detail object within the domain object 'Dommy' and the query is to find the Dommy with name = fred
 * Then the following HQL query is generated: FROM Dommy e WHERE e.detail.name = 'fred'
 *  
 * @author lefeu
 *
 */
public class DefinedQueryGenerator {
	private static final Logger logger = LogManager.getLogger();
	private static final String ENTITY_NAME_PLACEHOLDER = "e";
	private ColumnDefinitions columnDefinitions;
	private DefinedQuery definedQuery;
	
	public DefinedQueryGenerator(
			DefinedQuery definedQuery,
			ColumnDefinitions columnDefinitions) {
		
		this.columnDefinitions = columnDefinitions;
		this.definedQuery = definedQuery;
	}
	
	/**
	 * Get a parameter map converted to the proper objects
	 * @return
	 */
	public Map<String, Object> getParameterMap() {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		
		for (DefinedWhereExpression expression	: definedQuery.getWhereClause().getExpressions()) {
			ColumnDefinition definition = columnDefinitions.get(expression.getColumnName());
			if (definition == null) {
				logger.error("illegal column name: " + expression.getColumnName());
				throw new DefinedQueryException("illegal column name: " + expression.getColumnName());
			}
			
			Object valueIn =  expression.getValue();
			
			if (valueIn == null)
				continue;
			
			Object convertedValue;
			
			if (valueIn instanceof List) {
				
				List objectList = (List) valueIn;
				ArrayList<Object> convertedList = new ArrayList<Object>();
				
				for (int i=0; i < objectList.size(); i++) {
					Object value = objectList.get(i);
					convertedList.add(
							convertValue(definition.getDataType(), value));
				}
				
				parameters.put(expression.getParameterReference(), convertedList);
				
			} else {
				convertedValue = convertValue(definition.getDataType(), valueIn);
				parameters.put(expression.getParameterReference(), convertedValue);
			}
			
		}
		return parameters;
	}
	
	private Object convertValue(ColumnDataType dataType, Object valueIn) {
		
		switch (dataType) {
		
		case STRING :
			if (valueIn instanceof String)
				return valueIn;
			else
				return valueIn.toString();
		
		case BOOLEAN :
			if (valueIn instanceof Boolean)
				return valueIn;
			else 
				return new Boolean(valueIn.toString());
			
		case BIG_DECIMAL :
			if (valueIn instanceof BigDecimal)
				return valueIn;
			else if (valueIn instanceof Integer)
				return BigDecimal.valueOf((Integer) valueIn);
			else if (valueIn instanceof Long)
				return BigDecimal.valueOf((Long) valueIn);
			else if (valueIn instanceof Float)
				return BigDecimal.valueOf((Float) valueIn);
			else if (valueIn instanceof Double)
				return BigDecimal.valueOf((Double) valueIn);
			else
				return new BigDecimal(valueIn.toString());
			
		case INTEGER :
			if (valueIn instanceof Integer) {
				return valueIn;
			} else if (valueIn instanceof Number) {
				return new Integer( ((Number)valueIn).intValue());
			} else if (valueIn instanceof BigDecimal) {
				return new Integer(valueIn.toString());
			} else
				return new Integer(valueIn.toString());
			
		case LONG :
			if (valueIn instanceof Long)
				return valueIn;
			else if (valueIn instanceof Number)
				return new Long( ((Number)valueIn).longValue());
			else 
				return new Long(valueIn.toString());
		
		case DATE :
			if (valueIn instanceof LocalDate) {
				return valueIn;
			} else if (valueIn instanceof LocalDateTime) {
				LocalDateTime ldt = (LocalDateTime) valueIn;
				return ldt.toLocalDate();
			} else {
				return parseDateOrDateTimeToDate(valueIn.toString());
			}
			
			
		case DATE_TIME :
			if (valueIn instanceof LocalDateTime) {
				return valueIn;
			} else if(valueIn instanceof  LocalDate) {
				LocalDate ld = (LocalDate) valueIn;
				return LocalDateTime.of(ld, LocalTime.MIDNIGHT);
			} else {
				return parseDateOrDateTimeToDateTime(valueIn.toString());
			}
				
		default :
			logger.error("Mapping failed to convert a " + dataType + " from " + valueIn.toString());
			throw new DefinedQueryException("Mapping failed to convert a " + dataType + " from " + valueIn.toString());
			
		
		}
		
	}

	private LocalDate parseDateOrDateTimeToDate(String dateString) {
		try {
			return LocalDate.parse(dateString);
		} catch (DateTimeParseException e) {
			LocalDateTime ldt = LocalDateTime.parse(dateString);
			LocalDate ld = ldt.toLocalDate();
			return ld;
		}
	}


	private LocalDateTime parseDateOrDateTimeToDateTime(String dateString) {
		try {
			return LocalDateTime.parse(dateString);
		} catch (DateTimeParseException e) {
			LocalDate ldt = LocalDate.parse(dateString);
			return LocalDateTime.of(ldt, LocalTime.MIDNIGHT);
		}
	}

	/**
	 * Generate a query in string format that may be consumed by the Base Repository.
	 * @return
	 */
	public String generateQuery() {
		
		StringBuilder buffer = new StringBuilder("SELECT ");
		buffer.append(ENTITY_NAME_PLACEHOLDER);
		buffer.append(" FROM ");
		buffer.append(definedQuery.getEntityName());
		buffer.append(" ");
		buffer.append(ENTITY_NAME_PLACEHOLDER);
		
		if (definedQuery.getWhereClause().hasExpressions()) {
			addWhereClause(buffer, definedQuery.getWhereClause());
		}
		
		if (definedQuery.getOrderByClause().hasExpressions()) {
			addOrderByClause(buffer, definedQuery.getOrderByClause());
		}

		return buffer.toString();
	}

	public String generateListQuery() {

		StringBuilder buffer = new StringBuilder("SELECT new com.onbelay.core.entity.snapshot.EntityId(");
		buffer.append(ENTITY_NAME_PLACEHOLDER);
		buffer.append(".id,");
		buffer.append(ENTITY_NAME_PLACEHOLDER);
		buffer.append(".");
		buffer.append(columnDefinitions.getCodeName());
		buffer.append("," );
		buffer.append(ENTITY_NAME_PLACEHOLDER);
		buffer.append(".");
		buffer.append(columnDefinitions.getDescriptionName());
		buffer.append("," );
		buffer.append(ENTITY_NAME_PLACEHOLDER);
		buffer.append(".");
		buffer.append("isExpired)" );
		buffer.append(" FROM ");
		buffer.append(definedQuery.getEntityName());
		buffer.append(" ");
		buffer.append(ENTITY_NAME_PLACEHOLDER);

		if (definedQuery.getWhereClause().hasExpressions()) {
			addWhereClause(buffer, definedQuery.getWhereClause());
		}

		if (definedQuery.getOrderByClause().hasExpressions()) {
			addOrderByClause(buffer, definedQuery.getOrderByClause());
		}

		return buffer.toString();
	}

	public String generatePropertiesQuery(List<ColumnDefinition> properties) {
		StringBuilder buffer = new StringBuilder("SELECT ");
		if (properties.isEmpty())
			throw new OBRuntimeException(CoreTransactionErrorCode.SYSTEM_FAILURE.getCode());
		buffer.append(ENTITY_NAME_PLACEHOLDER);
		buffer.append(".");
		buffer.append(properties.get(0).getPath());
		
		for (int i=1; i < properties.size();i++) {
			buffer.append(",");
			buffer.append(ENTITY_NAME_PLACEHOLDER);
			buffer.append(".");
			buffer.append(properties.get(i).getPath());
			buffer.append(" ");
		}
		buffer.append(" FROM ");
		buffer.append(definedQuery.getEntityName());
		buffer.append(" ");
		buffer.append(ENTITY_NAME_PLACEHOLDER);

		if (definedQuery.getWhereClause().hasExpressions()) {
			addWhereClause(buffer, definedQuery.getWhereClause());
		}

		if (definedQuery.getOrderByClause().hasExpressions()) {
			addOrderByClause(buffer, definedQuery.getOrderByClause());
		}

		return buffer.toString();
	}

	private String formatExpression(DefinedWhereExpression expression) {
		ColumnDefinition definition = columnDefinitions.get(expression.getColumnName());
		if (definition == null) {
			logger.error("Column definition missing for where expression : " + expression.toString());
			throw new DefinedQueryException("Column definition missing for where expression : " + expression.toString());
		}

		if (expression.hasValue()) {
			return ENTITY_NAME_PLACEHOLDER + "." 
				+ definition.getPath()
				+ " "
				+ expression.getOperator().getCode() 
				+ " :"
				+ expression.getParameterReference();
		} else {
			return ENTITY_NAME_PLACEHOLDER + "." 
					+ definition.getPath()
					+ " "
					+ expression.getOperator().getCode(); 
			
		}
		
		
		
	}

	/**
	 * Return a query that will fetch all the ids for domain objects based on the optional where and order by clauses.
	 * @return a list of zero to many ids in order if an order by clause is provided.
	 */
	public String generateQueryForIds() {
		StringBuilder buffer = new StringBuilder("SELECT ");
		buffer.append(ENTITY_NAME_PLACEHOLDER);
		buffer.append(".id");
		buffer.append(" FROM ");
		buffer.append(definedQuery.getEntityName());
		buffer.append(" ");
		buffer.append(ENTITY_NAME_PLACEHOLDER);
		
		if (definedQuery.getWhereClause().hasExpressions()) {
			addWhereClause(buffer, definedQuery.getWhereClause());
		}
		
		if (definedQuery.getOrderByClause().hasExpressions()) {
			addOrderByClause(buffer, definedQuery.getOrderByClause());
		}
		
		return buffer.toString();
	}
	
	
	private void addWhereClause(StringBuilder buffer, DefinedWhereClause whereClause) {
		buffer.append(" WHERE ");
		ExpressionElement element = whereClause.getElements().get(0);
		
		if (element instanceof DefinedWhereExpression) {
			DefinedWhereExpression expression = (DefinedWhereExpression) element;
			buffer.append(formatExpression(expression));
		} else {
			buffer.append(element.toString());
		}

		
		for (int i=1; i < whereClause.getElements().size(); i++) {
			buffer.append(" ");
			
			element = whereClause.getElements().get(i);
			
			if (element instanceof DefinedWhereExpression) {
				DefinedWhereExpression expression = (DefinedWhereExpression) element;
				buffer.append(formatExpression(expression));
			} else {
				buffer.append(element.toString());
			}

		}
	}
	
	
	private void addOrderByClause(StringBuilder buffer, DefinedOrderByClause orderByClause) {
		buffer.append(" ORDER BY ");
		DefinedOrderExpression expression = orderByClause.getExpressions().get(0);
		buffer.append(lookUpOrderName(expression));
		
		for (int i =1; i < orderByClause.getExpressions().size(); i++) {
			buffer.append(",");
			expression = orderByClause.getExpressions().get(i);
			buffer.append(lookUpOrderName(expression));
		}
		
	}
	
	private String lookUpOrderName(DefinedOrderExpression orderExpression) {
		ColumnDefinition definition = columnDefinitions.get(orderExpression.getColumnName());
		if (definition == null) {
			logger.error("Column definition missing for order expression : " + orderExpression.toString());
			throw new DefinedQueryException("Column definition missing for order expression : " + orderExpression.toString());
		}
		
		return ENTITY_NAME_PLACEHOLDER + "."  + definition.getPath();
		

	}
}
