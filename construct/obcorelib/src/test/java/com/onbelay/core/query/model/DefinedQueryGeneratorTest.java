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
import com.onbelay.core.query.enums.ExpressionOperator;
import com.onbelay.core.query.snapshot.DefinedOrderExpression;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.DefinedWhereExpression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DefinedQueryGeneratorTest {
	private static final Logger logger = LogManager.getLogger();
	
	
	@Test
	public void testWithOrderByClause() {

		ArrayList<Integer> ids = new ArrayList<>();
		ids.add(2);
		
		DefinedQuery query = new DefinedQuery("Organization");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"id",
						ExpressionOperator.IN,
						ids));
		
		query.getOrderByClause().addOrderExpression(new DefinedOrderExpression("name"));
		
		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());
		
		logger.debug(generator.generateQuery());

	}
	
	@Test
	public void testSimpleWhereQueryLong() {
		DefinedQuery query = new DefinedQuery("Deal");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"id",
						ExpressionOperator.EQUALS,
						2l));
		
		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());
		
		String queryText = generator.generateQuery();
		logger.error(queryText);
		
		assertEquals("SELECT e FROM Deal e WHERE e.id = :p1", queryText);
		
		Map<String, Object> valuesMap = generator.getParameterMap();
		
		logger.debug(valuesMap);
		
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		long value = ((Long)valuesMap.get("p1")).longValue();
		assertEquals(2l, value);
	}
	
	public void testSimpleWhereQueryString() {
		DefinedQuery query = new DefinedQuery("Deal");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"name",
						ExpressionOperator.EQUALS,
						"fred"));
		
		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());
		
		String queryText = generator.generateQuery();
		logger.error(queryText);
		
		assertEquals("SELECT e FROM Deal e WHERE e.detail.name = :p1", queryText);
		
		Map<String, Object> valuesMap = generator.getParameterMap();
		
		logger.debug(valuesMap);
		
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		assertEquals("fred", valuesMap.get("p1"));
	}
	
	public void testSimpleWhereQueryBigDecimal() {
		DefinedQuery query = new DefinedQuery("Deal");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"amount",
						ExpressionOperator.EQUALS,
						BigDecimal.valueOf(22.67)));
		
		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());
		
		String queryText = generator.generateQuery();
		logger.error(queryText);
		
		assertEquals("SELECT e FROM Deal e WHERE e.detail.amount = :p1", queryText);
		
		Map<String, Object> valuesMap = generator.getParameterMap();
		
		logger.debug(valuesMap);
		
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		Object parm = valuesMap.get("p1");
		assertTrue(parm instanceof BigDecimal);
		BigDecimal value = (BigDecimal) parm;
		assertEquals(0, value.compareTo(BigDecimal.valueOf(22.67)));
	}
	
	public void testSimpleWhereQueryBigDecimalPassedInInteger() {
		DefinedQuery query = new DefinedQuery("Deal");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"amount",
						ExpressionOperator.EQUALS,
						3));
		
		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());
		
		String queryText = generator.generateQuery();
		logger.error(queryText);
		
		assertEquals("SELECT e FROM Deal e WHERE e.detail.amount = :p1", queryText);
		
		Map<String, Object> valuesMap = generator.getParameterMap();
		
		logger.debug(valuesMap);
		
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		Object parm = valuesMap.get("p1");
		assertTrue(parm instanceof BigDecimal);
		BigDecimal value = (BigDecimal) parm;
		assertEquals(0, value.compareTo(BigDecimal.valueOf(3)));
	}
	
	
	public void testSimpleWhereQueryInteger() {
		DefinedQuery query = new DefinedQuery("Deal");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"times",
						ExpressionOperator.EQUALS,
						3));
		
		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());
		
		String queryText = generator.generateQuery();
		logger.error(queryText);
		
		assertEquals("SELECT e FROM Deal e WHERE e.detail.times = :p1", queryText);
		
		Map<String, Object> valuesMap = generator.getParameterMap();
		
		logger.debug(valuesMap);
		
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		Object parm = valuesMap.get("p1");
		assertTrue(parm instanceof Integer);
		Integer value = (Integer) parm;
		assertEquals(3, value.intValue());
	}
	
	public void testInWhereQueryInteger() {
		DefinedQuery query = new DefinedQuery("Deal");
		List<Integer> values = Arrays.asList(1, 2, 3);
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"times",
						ExpressionOperator.IN,
						values));
		
		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());
		
		String queryText = generator.generateQuery();
		logger.error(queryText);
		
		assertEquals("SELECT e FROM Deal e WHERE e.detail.times IN :p1", queryText);
		
		Map<String, Object> valuesMap = generator.getParameterMap();
		
		logger.debug(valuesMap);
		
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		Object parm = valuesMap.get("p1");
		assertTrue(parm instanceof List);
		List contents = (List) parm;
		assertTrue(contents.get(0) instanceof Integer);
		Integer parmOne = (Integer) contents.get(0);
		assertEquals(1, parmOne.intValue());
	}
	
	public void testSimpleWhereQueryIntegerAsBigDecimal() {
		DefinedQuery query = new DefinedQuery("Deal");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"times",
						ExpressionOperator.EQUALS,
						new BigDecimal(3)));
		
		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());
		
		String queryText = generator.generateQuery();
		logger.error(queryText);
		
		assertEquals("SELECT e FROM Deal e WHERE e.detail.times = :p1", queryText);
		
		Map<String, Object> valuesMap = generator.getParameterMap();
		
		logger.debug(valuesMap);
		
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		Object parm = valuesMap.get("p1");
		assertTrue(parm instanceof Integer);
		Integer value = (Integer) parm;
		assertEquals(3, value.intValue());
	}
	
	
	@Test
	public void testSimpleBuildQueryWithId() {
		
		DefinedQuery query = new DefinedQuery("Organization");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"id",
						ExpressionOperator.NOT_EQUALS,
						3l));
		
		
		
		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());
		
		String queryText = generator.generateQueryForIds();
		
		logger.error(queryText);
		
		assertEquals("SELECT e.id FROM Organization e WHERE e.id != :p1", queryText);
		
		Map<String, Object> valuesMap = generator.getParameterMap();
		
		logger.debug(valuesMap);
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		Object parm = valuesMap.get("p1");
		assertTrue(parm instanceof Long);
		Long value = (Long) parm;
		assertEquals(3l, value.longValue());
		
		
	}


	@Test
	public void testSimpleBuildQueryWithDate() {

		DefinedQuery query = new DefinedQuery("Organization");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"dueDate",
						ExpressionOperator.NOT_EQUALS,
						"2022-01-01"));



		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());

		String queryText = generator.generateQuery();

		logger.error(queryText);

		assertEquals("SELECT e FROM Organization e WHERE e.detail.dueDate != :p1", queryText);

		Map<String, Object> valuesMap = generator.getParameterMap();

		logger.debug(valuesMap);
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		Object parm = valuesMap.get("p1");
		assertTrue(parm instanceof LocalDate);

	}

	@Test
	public void testSimpleBuildQueryWithDateFromDateTime() {

		DefinedQuery query = new DefinedQuery("Organization");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"dueDate",
						ExpressionOperator.NOT_EQUALS,
						LocalDateTime.of(2022, 1, 1, 0, 0)));



		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());

		String queryText = generator.generateQuery();

		logger.error(queryText);

		assertEquals("SELECT e FROM Organization e WHERE e.detail.dueDate != :p1", queryText);

		Map<String, Object> valuesMap = generator.getParameterMap();

		logger.debug(valuesMap);
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		Object parm = valuesMap.get("p1");
		assertTrue(parm instanceof LocalDate);

	}


	@Test
	public void testSimpleBuildQueryWithDateTime() {

		DefinedQuery query = new DefinedQuery("Organization");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"observedDateTime",
						ExpressionOperator.NOT_EQUALS,
						"2022-01-01"));



		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());

		String queryText = generator.generateQuery();

		logger.error(queryText);

		assertEquals("SELECT e FROM Organization e WHERE e.detail.observedDateTime != :p1", queryText);

		Map<String, Object> valuesMap = generator.getParameterMap();

		logger.debug(valuesMap);
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		Object parm = valuesMap.get("p1");
		assertTrue(parm instanceof LocalDateTime);

	}


	@Test
	public void testSimpleBuildQueryWithDateTimeWithDate() {

		DefinedQuery query = new DefinedQuery("Organization");
		query.getWhereClause().addExpression(
				new DefinedWhereExpression(
						"observedDateTime",
						ExpressionOperator.NOT_EQUALS,
						LocalDate.of(2022, 01, 01)));



		DefinedQueryGenerator generator = new DefinedQueryGenerator(
				query,
				new TestColumnDefinitions());

		String queryText = generator.generateQuery();

		logger.error(queryText);

		assertEquals("SELECT e FROM Organization e WHERE e.detail.observedDateTime != :p1", queryText);

		Map<String, Object> valuesMap = generator.getParameterMap();

		logger.debug(valuesMap);
		assertEquals(1, valuesMap.size());
		assertTrue(valuesMap.containsKey("p1"));
		Object parm = valuesMap.get("p1");
		assertTrue(parm instanceof LocalDateTime);

	}


	public static class TestColumnDefinitions implements ColumnDefinitions {

		public Map<String, ColumnDefinition> definitionsMap = new HashMap<String, ColumnDefinition>();
		
		public static final ColumnDefinition id = new ColumnDefinition("id", ColumnDataType.LONG, "id");
		public static final ColumnDefinition name = new ColumnDefinition("name", ColumnDataType.STRING, "detail.name");
		public static final ColumnDefinition dueDate = new ColumnDefinition("dueDate", ColumnDataType.DATE, "detail.dueDate");
		public static final ColumnDefinition observedDateTime = new ColumnDefinition("observedDateTime", ColumnDataType.DATE_TIME, "detail.observedDateTime");
		public static final ColumnDefinition amount = new ColumnDefinition("amount", ColumnDataType.BIG_DECIMAL, "detail.amount");
		public static final ColumnDefinition times = new ColumnDefinition("times", ColumnDataType.INTEGER, "detail.times");
		public static final ColumnDefinition isAlive = new ColumnDefinition("isAlive", ColumnDataType.BOOLEAN, "detail.isAlive");
		
		public TestColumnDefinitions() {
			add(id);
			add(name);
			add(dueDate);
			add(observedDateTime);
			add(amount);
			add(times);
			add(isAlive);
		}

		@Override
		public String getCodeName() {
			return name.getPath();
		}

		@Override
		public String getDescriptionName() {
			return name.getPath();
		}

		public ColumnDefinition get(String name) {
			return definitionsMap.get(name);
		}
		
		private void add(ColumnDefinition definition) {
			definitionsMap.put(definition.getName(), definition);
		}
		
		
	}

}
