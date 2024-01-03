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
package com.onbelay.core.entity.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.query.model.ColumnDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.onbelay.core.appsetting.model.ApplicationSetting;
import com.onbelay.core.entity.component.ApplicationContextFactory;
import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.entity.model.AbstractEntity;
import com.onbelay.core.entity.model.AuditAbstractEntity;
import com.onbelay.core.entity.model.TemporalAbstractEntity;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.core.query.enums.ExpressionOperator;
import com.onbelay.core.query.model.ColumnDefinitions;
import com.onbelay.core.query.model.DefinedQueryGenerator;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.DefinedWhereExpression;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.core.utils.SubLister;

/**
 * Base class for all repositories.
 */
@Transactional
public class BaseRepository<T> implements EntityRepository<T> {
    private static final Logger logger = LogManager.getLogger();
    public static final int MAX_SIZE_FOR_IN = 1000;
    
    @PersistenceContext
    protected EntityManager entityManager;


    @Value("${isSqlServer:false}")
    private boolean isSqlServer;

    protected Integer getNextSequenceValue(String sequenceName) {
        String query;

        if (isSqlServer) {
            query = "SELECT NEXT VALUE FOR " + sequenceName;
        } else {
            query = "SELECT NEXTVAL('" + sequenceName + "')";
        }
        return executeNativeScalarQuery(query);
    }


    public <E> E loadChild(Class<E> claz, Object id) {
        return  entityManager.find(claz, id);
    }
    
    
    public T loadNonEntity(Class<T> claz, Object id) {
        return (T) entityManager.find(claz, id);
    }
    
    /**
     * Find a domain object by its primary key
     * @param claz - class of domain object
     * @param id - primary key (may be a composite key object)
     * @return null if not found or the domain object.
     */
    public T find(Class<T> claz, Integer id) {
        return entityManager.find(claz, id);
    }
    

    
    /**
     * Execute a single result query with a single parameter
     * @param queryName - name of query
     * @param parmName - name of parameter
     * @param parm - parameter value
     * @return An AbstractEntity or null if not found
     */
    public T executeSingleResultQuery(String queryName, String parmName, Object parm) {
        Query query = entityManager.createNamedQuery(queryName);
        query.setParameter(parmName, parm);
        List<T> results = query.getResultList();
        if (results.size() == 1)
            return  results.get(0);
        else if (results.size() == 0)
            return null;
        else
            throw new OBRuntimeException(CoreTransactionErrorCode.QUERY_MORE_THAN_ONE_RESULT.getCode());
    }
    
    /**
     * Execute a single result query with multiple parameters
     * @param queryName - name of query
     * @param paramNames - An array of parameter names
     * @param parms - An array of parameter values
     * @return An AbstractEntity or null if not found
     * @throws RuntimeException if more than one object is returned
     */
    public T executeSingleResultQuery(String queryName, String[] paramNames, Object[] parms) {
        HashMap<String, Object> parmMap = new HashMap<String, Object>();
        if (paramNames.length != parms.length)
            throw new RuntimeException("paramNames and parms arrays are not same size");
        Query query = entityManager.createNamedQuery(queryName);
        for (int i=0; i<paramNames.length; i++)
            query.setParameter(paramNames[i], parms[i]);
        List<T> results = query.getResultList();
        if (results.size() == 1)
            return  results.get(0);
        else if (results.size() == 0)
            return null;
        else
            throw new OBRuntimeException(CoreTransactionErrorCode.QUERY_MORE_THAN_ONE_RESULT.getCode());
    }
    
    /**
     * Execute a query that returns a list of domain objects.
     * @param queryName - name of query that does not take any parameters
     * @return a List of domain objects
     */
    @SuppressWarnings("unchecked")
    public List<T> executeQuery(String queryName) {
        Query query = entityManager.createNamedQuery(queryName);
        return query.getResultList();
    }


    /**
     * Execute a JPA query that will return a list of domain objects. 
     * @param queryName - a query with a single parameter
     * @param parmName - parameter name
     * @param parm - parameter value that is for the parameter name above
     * @return a list of domain objects.
     */
    @SuppressWarnings("unchecked")
    public List<T> executeQuery(String queryName, String parmName, Object parm) {
        Query query = entityManager.createNamedQuery(queryName);
        query.setParameter(parmName, parm);
        return query.getResultList();
    }
    
    /**
     * Execute a JPA query that will return a list of value objects. 
     * @param queryName - a query with a single parameter
     * @return a list of report objects.
     */
    @SuppressWarnings("unchecked")
    public List<?> executeReportQuery(String queryName) {
        Query query = entityManager.createNamedQuery(queryName);
        return query.getResultList();
    }

    /**
     * Execute a JPA query that will return a list of value objects. 
     * @param queryName - a query with a single parameter
     * @param maxResults - maximum number of results to be returned
     * @return a list of report objects.
     */
    @SuppressWarnings("unchecked")
    public List<?> executeReportQuery(String queryName, int maxResults) {
        Query query = entityManager.createNamedQuery(queryName);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    public <T> List<T> executeDefinedQuery(
    		ColumnDefinitions columnDefinitions, 
    		DefinedQuery definedQuery) {
    	
    	DefinedQueryGenerator generator = new DefinedQueryGenerator(
    			definedQuery,
    			columnDefinitions);
    	
    	String queryString = generator.generateQuery();
    	logger.debug("Generated query: " + queryString);
    	
    	Query jpaQuery = entityManager.createQuery(queryString);
    	if (definedQuery.getWhereClause().hasParameters()) {
    		for (Map.Entry<String, ?> entry : generator.getParameterMap().entrySet()) {
    			logger.debug("Generated Query parameter key : " + entry.getKey() + " value: " + entry.getValue());
    			jpaQuery.setParameter(entry.getKey(), entry.getValue());
    		}
    	}
    	return jpaQuery.getResultList();
    }


    public List<EntityId> executeDefinedQueryForList(
            ColumnDefinitions columnDefinitions,
            DefinedQuery definedQuery) {

        DefinedQueryGenerator generator = new DefinedQueryGenerator(
                definedQuery,
                columnDefinitions);

        String queryString = generator.generateListQuery();
        logger.debug("Generated query: " + queryString);

        Query jpaQuery = entityManager.createQuery(queryString);
        if (definedQuery.getWhereClause().hasParameters()) {
            for (Map.Entry<String, ?> entry : generator.getParameterMap().entrySet()) {
                logger.debug("Generated Query parameter key : " + entry.getKey() + " value: " + entry.getValue());
                jpaQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return jpaQuery.getResultList();
    }


    public List<Integer> executeDefinedQueryForIds(
    		ColumnDefinitions columnDefinitions, 
    		DefinedQuery definedQuery) {
    	
    	DefinedQueryGenerator generator = new DefinedQueryGenerator(
    			definedQuery,
    			columnDefinitions);
    	
    	String queryString = generator.generateQueryForIds();
    	logger.debug("Generated query: " + queryString);
    	
    	Query jpaQuery = entityManager.createQuery(queryString);
    	if (definedQuery.getWhereClause().hasParameters()) {
    		for (Map.Entry<String, ?> entry : generator.getParameterMap().entrySet()) {
    			logger.debug("Generated Query parameter key : " + entry.getKey() + " value: " + entry.getValue());
    			jpaQuery.setParameter(entry.getKey(), entry.getValue());
    		}
    	}
    	return jpaQuery.getResultList();
    }
    
    public <T> List<T> fetchEntitiesById(
    		ColumnDefinitions columnDefinitions, 
    		String entityName, 
    		QuerySelectedPage selectedPage) {
    	
    	if (selectedPage.getIds().isEmpty())
    		return new ArrayList<T>();	

    	
    	DefinedQuery query = new DefinedQuery(entityName);

    	if (selectedPage.getIds().size() <= MAX_SIZE_FOR_IN) {

	    	query.getWhereClause().addExpression(
	    			new DefinedWhereExpression(
	    					"id",
	    					ExpressionOperator.IN,
	    					selectedPage.getIds()));
	    			
	    	if (selectedPage.hasOrderClause()) {
	    		query.getOrderByClause().copyIn(selectedPage.getOrderByClause());
	    	}
	    	
	    	return executeDefinedQuery(columnDefinitions, query);
	    	
    	} else {
    	
    		SubLister<Integer> subLister = new SubLister<>(
    				selectedPage.getIds(), 
    				MAX_SIZE_FOR_IN);
    		
    		ArrayList<T> items = new ArrayList<>();
    		
    		while (subLister.moreElements()) {
    			
    			List<Integer> subList = subLister.nextList();
    	    	query = new DefinedQuery(entityName);
    	    	
    	    	query.getWhereClause().addExpression(
    	    			new DefinedWhereExpression(
    	    					"id",
    	    					ExpressionOperator.IN,
    	    					subList));
    	    			
    	    	if (selectedPage.hasOrderClause()) {
    	    		query.getOrderByClause().copyIn(selectedPage.getOrderByClause());
    	    	}
    			
    	    	List<T> results = executeDefinedQuery(columnDefinitions, query);
    			items.addAll(results);
    		}
    		
    		return items;
    	}
    }

    public List<EntityId> fetchEntityIdsById(
            ColumnDefinitions columnDefinitions,
            String entityName,
            QuerySelectedPage selectedPage) {

        if (selectedPage.getIds().isEmpty())
            return new ArrayList<EntityId>();


        DefinedQuery query = new DefinedQuery(entityName);

        if (selectedPage.getIds().size() <= MAX_SIZE_FOR_IN) {

            query.getWhereClause().addExpression(
                    new DefinedWhereExpression(
                            "id",
                            ExpressionOperator.IN,
                            selectedPage.getIds()));

            if (selectedPage.hasOrderClause()) {
                query.getOrderByClause().copyIn(selectedPage.getOrderByClause());
            }

            return executeDefinedQueryForList(columnDefinitions, query);

        } else {

            SubLister<Integer> subLister = new SubLister<>(
                    selectedPage.getIds(),
                    MAX_SIZE_FOR_IN);

            ArrayList<EntityId> items = new ArrayList<>();

            while (subLister.moreElements()) {

                List<Integer> subList = subLister.nextList();
                query = new DefinedQuery(entityName);

                query.getWhereClause().addExpression(
                        new DefinedWhereExpression(
                                "id",
                                ExpressionOperator.IN,
                                subList));

                if (selectedPage.hasOrderClause()) {
                    query.getOrderByClause().copyIn(selectedPage.getOrderByClause());
                }

                List<EntityId> results = executeDefinedQueryForList(columnDefinitions, query);
                items.addAll(results);
            }

            return items;
        }
    }

    /**
     * Execute a JPA query that will return a list of value objects. 
     * @param queryName - a query with a single parameter
     * @param parmName - parameter name
     * @param parm - parameter value that is for the parameter name above
     * @return a list of report objects.
     */
    @SuppressWarnings("unchecked")
    public List<?> executeReportQuery(String queryName, String parmName, Object parm) {
        Query query = entityManager.createNamedQuery(queryName);
        query.setParameter(parmName, parm);
        return query.getResultList();
    }

    /**
     * Execute a JPA query that will return a list of value objects up to max results. 
     * @param queryName - a query with a single parameter
     * @param maxResults - maximum rows to return.
     * @param parmName - parameter name
     * @param parm - parameter value that is for the parameter name above
     * @return a list of report objects.
     */
    @SuppressWarnings("unchecked")
    public List<?> executeReportQuery(
    		String queryName,
    		int maxResults,
    		String parmName, 
    		Object parm) {
        Query query = entityManager.createNamedQuery(queryName);
        query.setMaxResults(maxResults);
        query.setParameter(parmName, parm);
        return query.getResultList();
    }

    /**
     * Execute a JPA query that will return a value object. 
     * @param queryName - a query with a single parameter
     * @param parmName - parameter name
     * @param parm - parameter value that is for the parameter name above
     * @return a value object
     */
    @SuppressWarnings("unchecked")
    public Object executeSingleResultReportQuery(String queryName, String parmName, Object parm) {
        Query query = entityManager.createNamedQuery(queryName);
        query.setParameter(parmName, parm);
        List items =  query.getResultList();
        if (items == null)
            return null;
        if (items.isEmpty())
            return null;
        
        if (items.size() > 1)
            throw new OBRuntimeException(CoreTransactionErrorCode.QUERY_MORE_THAN_ONE_RESULT.getCode());
        
        return items.get(0);
    }

    public Object executeSingleResultReportQuery(String queryName) {
        Query query = entityManager.createNamedQuery(queryName);
        List items =  query.getResultList();
        if (items == null)
            return null;
        if (items.isEmpty())
            return null;
        
        if (items.size() > 1)
            throw new OBRuntimeException(CoreTransactionErrorCode.QUERY_MORE_THAN_ONE_RESULT.getCode());
        
        return items.get(0);
    }
    /**
     * Execute a JPA query that will return a value object. 
     * @param queryName - a query with a single parameter
     * @param paramNames - array of parameter names
     * @param parms - array of parms that match paramNames
     * @return a value object
     */
    @SuppressWarnings("unchecked")
    public Object executeSingleResultReportQuery(String queryName, String[] paramNames, Object[] parms) {
        Query query = entityManager.createNamedQuery(queryName);
        if (paramNames.length != parms.length)
            throw new RuntimeException("paramNames and parms arrays are not same size");
        for (int i=0; i<paramNames.length; i++)
            query.setParameter(paramNames[i], parms[i]);
        List items =  query.getResultList();
        if (items == null)
            return null;
        if (items.isEmpty())
            return null;
        
        if (items.size() > 1)
            throw new OBRuntimeException(CoreTransactionErrorCode.QUERY_MORE_THAN_ONE_RESULT.getCode());
        
        return items.get(0);
    }

    /**
     * Execute a JPA query that will return a value object. 
     * @param queryName - a query with a single parameter
     * @return a value object
     */
    @SuppressWarnings("unchecked")
    public Object executeSingleResultReportQueryWithNoParams(String queryName) {
        Query query = entityManager.createNamedQuery(queryName);
        List items =  query.getResultList();
        if (items == null)
            return null;
        if (items.isEmpty())
            return null;
        
        if (items.size() > 1)
            throw new OBRuntimeException(CoreTransactionErrorCode.QUERY_MORE_THAN_ONE_RESULT.getCode());
        
        return items.get(0);
    }

    /**
     * Execute a JPA query that will return a list of value objects. 
     * @param queryName - a query with multiple parameters
     * @param paramNames - a list of parameter names
     * @param parms -  a list of parameter values that is for the parameter names above
     * @return a list of value objects
     */
    @SuppressWarnings("unchecked")
    public List<?>
    executeReportQuery(String queryName, String[] paramNames, Object[] parms) {
        Query query = entityManager.createNamedQuery(queryName);
        if (paramNames.length != parms.length)
            throw new RuntimeException("paramNames and parms arrays are not same size");
        for (int i=0; i<paramNames.length; i++)
            query.setParameter(paramNames[i], parms[i]);
        return query.getResultList();
    }

    public List executePropertiesQueryFromIds(
        ColumnDefinitions columnDefinitions,
        List<ColumnDefinition> properties,
        String entityName,
        QuerySelectedPage selectedPage) {

        if (selectedPage.getIds().isEmpty())
            return new ArrayList();


        DefinedQuery query = new DefinedQuery(entityName);

        if (selectedPage.getIds().size() <= MAX_SIZE_FOR_IN) {

            query.getWhereClause().addExpression(
                    new DefinedWhereExpression(
                            "id",
                            ExpressionOperator.IN,
                            selectedPage.getIds()));

            if (selectedPage.hasOrderClause()) {
                query.getOrderByClause().copyIn(selectedPage.getOrderByClause());
            }
            DefinedQueryGenerator generator = new DefinedQueryGenerator(
                    query,
                    columnDefinitions);

            String queryString = generator.generatePropertiesQuery(properties);
            logger.debug("Generated query: " + queryString);

            Query jpaQuery = entityManager.createQuery(queryString);
            if (query.getWhereClause().hasParameters()) {
                for (Map.Entry<String, ?> entry : generator.getParameterMap().entrySet()) {
                    logger.debug("Generated Query parameter key : " + entry.getKey() + " value: " + entry.getValue());
                    jpaQuery.setParameter(entry.getKey(), entry.getValue());
                }
            }
            return jpaQuery.getResultList();

        } else {

            SubLister<Integer> subLister = new SubLister<>(
                    selectedPage.getIds(),
                    MAX_SIZE_FOR_IN);

            ArrayList items = new ArrayList();

            while (subLister.moreElements()) {

                List<Integer> subList = subLister.nextList();
                query = new DefinedQuery(entityName);

                query.getWhereClause().addExpression(
                        new DefinedWhereExpression(
                                "id",
                                ExpressionOperator.IN,
                                subList));

                if (selectedPage.hasOrderClause()) {
                    query.getOrderByClause().copyIn(selectedPage.getOrderByClause());
                }

                DefinedQueryGenerator generator = new DefinedQueryGenerator(
                        query,
                        columnDefinitions);

                String queryString = generator.generatePropertiesQuery(properties);
                logger.debug("Generated query: " + queryString);

                Query jpaQuery = entityManager.createQuery(queryString);
                if (query.getWhereClause().hasParameters()) {
                    for (Map.Entry<String, ?> entry : generator.getParameterMap().entrySet()) {
                        logger.debug("Generated Query parameter key : " + entry.getKey() + " value: " + entry.getValue());
                        jpaQuery.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                List subItems = jpaQuery.getResultList();
                items.addAll(subItems);
            }

            return items;
        }

    }

    /**
     * Execute a JPA query that will return a list of value objects. 
     * @param queryName - a query with multiple parameters
     * @param paramNames - a list of parameter names
     * @param parms -  a list of parameter values that is for the parameter names above
     * @return a list of value objects
     */
    @SuppressWarnings("unchecked")
    public List<?> executeReportQuery(
    		String queryName,
    		int maxResults,
    		String[] paramNames, 
    		Object[] parms) {
    	
        if (paramNames.length != parms.length)
            throw new RuntimeException("paramNames and parms arrays are not same size");
    	
        Query query = entityManager.createNamedQuery(queryName);
        query.setMaxResults(maxResults);
        
        for (int i=0; i<paramNames.length; i++)
            query.setParameter(paramNames[i], parms[i]);
        return query.getResultList();
    }
    
    /**
     * Execute a JPA query that will return a list of domain objects. 
     * @param queryName - a query will multiple parameters
     * @param paramNames - an array of parameter names that match the names in the query
     * @param parms - an array of parameter values that line up with the parameter names above.
     * @return A list of domain objects. The list may be empty.
     */
    @SuppressWarnings("unchecked")
    public List<T> executeQuery(String queryName, String[] paramNames, Object[] parms) {
        Query query = entityManager.createNamedQuery(queryName);
        if (paramNames.length != parms.length)
            throw new RuntimeException("paramNames and parms arrays are not same size");
        for (int i=0; i<paramNames.length; i++)
            query.setParameter(paramNames[i], parms[i]);
        return query.getResultList();
    }

    /**
     * Execute a JPA query that will return a list of domain objects. 
     * @param queryName - a query will multiple parameters
     * @param paramNames - an array of parameter names that match the names in the query
     * @param parms - an array of parameter values that line up with the parameter names above.
     * @return A single domain object.
     */
    @SuppressWarnings("unchecked")
    public T executeLimitedSingleQuery(String queryName, String[] paramNames, Object[] parms) {
        Query query = entityManager.createNamedQuery(queryName);
        if (paramNames.length != parms.length)
            throw new RuntimeException("paramNames and parms arrays are not same size");
        query.setMaxResults(1);
        for (int i=0; i<paramNames.length; i++)
            query.setParameter(paramNames[i], parms[i]);
        List<T> results  = query.getResultList();
        if (results.size() == 0)
        	return null;
        else
        	return results.get(0);
    }

    /**
     * Execute a query that will return a list of SimpleListItems. See SimpleListItem.  
     * @param queryName - name of query that is defined to return a list of SimpleListItems.
     * @return A List of SimpleListItems.
     */
    @SuppressWarnings("unchecked")
    public List<EntityId> executeSimpleListQuery(String queryName) {
        Query query = entityManager.createNamedQuery(queryName);
        return query.getResultList();
    }


    /**
     * Execute a query that will return a list of SimpleListItems based on a single query parameter.  
     * @param queryName - name of query that is defined to return a list of SimpleListItems.
     * @param paramName - name of the parameter
     * @param parm - value of the parameter
     * @return A List of SimpleListItems.
     */
    @SuppressWarnings("unchecked")
    public List<EntityId> executeSimpleListQuery(String queryName, String paramName, Object parm) {
        Query query = entityManager.createNamedQuery(queryName);
        query.setParameter(paramName, parm);
        return query.getResultList();
    }
    
    /**
     * Execute a query that will return a list of SimpleListItems based on multiple query parameters.  
     * @param queryName - a query with multiple parameters
     * @param paramNames - an array of parameter names that match the names in the query
     * @param parms - an array of parameter values that line up with the parameter names above.
     * @return A List of SimpleListItems.
     */
    @SuppressWarnings("unchecked")
    public List<EntityId> executeSimpleListQuery(String queryName, String[] paramNames, Object[] parms) {
        Query query = entityManager.createNamedQuery(queryName);
        if (paramNames.length != parms.length)
            throw new RuntimeException("paramNames and parms arrays are not same size");
        for (int i=0; i<paramNames.length; i++)
            query.setParameter(paramNames[i], parms[i]);
        return query.getResultList();
    }
    
    
    /**
     * Execute a query that will a count   
     * @param queryName - name of query that is defined to return a list of SimpleListItems.
     * @param paramNames - names of the parameters
     * @param parms - values of the parameters
     * @return An Integer
     */
    @SuppressWarnings("unchecked")
    public Long executeNamedCountQuery(String queryName, String[] paramNames, Object[] parms) {
        Query query = entityManager.createNamedQuery(queryName);
        if (paramNames.length != parms.length)
            throw new RuntimeException("paramNames and parms arrays are not same size");
        for (int i=0; i<paramNames.length; i++)
            query.setParameter(paramNames[i], parms[i]);
        List scalers = query.getResultList();
        return (Long) scalers.get(0);
    }
    
    /**
     * Execute a query that will return a sum
     * @param queryName - name of query that is defined to return a list of SimpleListItems.
     * @param paramNames - names of the parameters
     * @param parms - values of the parameters
     * @return A BigDecimal
     */
    @SuppressWarnings("unchecked")
    public BigDecimal executeNamedSumQuery(String queryName, String[] paramNames, Object[] parms) {
        Query query = entityManager.createNamedQuery(queryName);
        if (paramNames.length != parms.length)
            throw new RuntimeException("paramNames and parms arrays are not same size");
        for (int i=0; i<paramNames.length; i++)
            query.setParameter(paramNames[i], parms[i]);
        List scalers = query.getResultList();
        return (BigDecimal) scalers.get(0);
    }
    
    /**
     * Execute a query that will a count   
     * @param queryName - name of query that is defined to return a count of records.
     * @return A Long
     */
    @SuppressWarnings("unchecked")
    public Long executeNamedCountQueryWithNoParams(String queryName) {
        Query query = entityManager.createNamedQuery(queryName);
        List scalers = query.getResultList();
        return (Long) scalers.get(0);
    }

    /**
     * Dynamically execute the JPA query provided.
     * @param queryString - JPA query
     * @return a list of zero to many SimpleListItems.
     */
    @SuppressWarnings("unchecked")
    public List<EntityId> executeListQueryString(String queryString) {
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }
    
    /**
     * Dynamically execute the JPA query provided.
     * @param queryString - JPA query
     * @return a list of zero to many objects
     */
    @SuppressWarnings("unchecked")
    public List executeReportQueryString(String queryString) {
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }
    
    /**
     * Dynamically execute the JPA query provided.
     * @param queryString - JPA query
     * @return a list of zero to many objects
     */
    @SuppressWarnings("unchecked")
    public List<T> executeReportQueryString(String queryString, String[] paramNames, Object[] parms) {
        Query query = entityManager.createQuery(queryString);
        if (paramNames.length != parms.length)
            throw new RuntimeException("paramNames and parms arrays are not same size");
        for (int i=0; i<paramNames.length; i++)
            query.setParameter(paramNames[i], parms[i]);

        return query.getResultList();
    }
    
    /**
     * Dynamically execute the JPA query provided.
     * @param queryString - JPA query
     * @return a list of zero to many objects.
     */
    @SuppressWarnings("unchecked")
    public List<T> executeReportQueryStringWithNamedParms(String queryString, Map<String, Object> parms) {
        Query query = entityManager.createQuery(queryString);
        for (Map.Entry<String, ?> entry : parms.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }
    
    
    
    
    /**
     * Dynamically execute the query string
     * @param queryString - JPA query
     * @return a list of zero to many abstract entities.
     */
    @SuppressWarnings("unchecked")
    public List<T> executeEntityQueryString(String queryString) {
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }
    
    /**
     * Dynamically execute the query string with a single parameter
     * @param queryString - JPA query
     * @return a list of zero to many abstract entities.
     */
    @SuppressWarnings("unchecked")
    public List<T> executeEntityQueryString(String queryString, String parmName, Object parm) {
        Query query = entityManager.createQuery(queryString);
        query.setParameter(parmName, parm);
        return query.getResultList();
    }
    
    
    /**
     * Dynamically execute the JPA query provided.
     * @param queryString - JPA query
     * @return a list of zero to many SimpleListItems.
     */
    @SuppressWarnings("unchecked")
    public List<EntityId> executeListQueryStringWithNamedParms(String queryString, Map<String, Object> parms) {
        Query query = entityManager.createQuery(queryString);
        for (Map.Entry<String, ?> entry : parms.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }
    
    /**
     * Execute a native sql select that returns a list of scaler values
     * @param nativeQueryString
     * @return a list of Object[]s representing the scalar values.
     */
    public List executeNativeQuery(final String nativeQueryString) {
        Query query = entityManager.createNativeQuery(nativeQueryString);
        return query.getResultList();
    }


    @Override
    public Integer executeNativeScalarQuery(String nativeQueryString) {
        Query query = entityManager.createNativeQuery(nativeQueryString);
        List items = query.getResultList();
        if ( items.isEmpty())
            return null;
        Long bg = (Long) items.get(0);
        return bg.intValue();
    }

    /**
     * Execute a query with 
     * @param queryName - named query name
     * @param maxResults - maximum results returned
     * @return a List of AbstractEntities
     */
    @SuppressWarnings("unchecked")
    public List<? extends AbstractEntity> executeQueryWithMaxResults(final String queryName, final String parmName, final String parmValue, final int maxResults) {
        Query query = entityManager.createNamedQuery(queryName);
        query.setParameter(parmName, parmValue);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }
    

    /**
     * @param claz
     * @param stringId - a primary id as a string
     * @return
     */
    @SuppressWarnings("unchecked")
    public AbstractEntity find(Class claz, String stringId) {
        return (AbstractEntity) entityManager.find(claz, stringId);
    }

    /**
     * @param claz
     * @param stringId - a primary id as a string
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object findNonEntity(Class claz, String stringId) {
        return entityManager.find(claz, stringId);
    }

	/**
	 * Save a list of entities within the same transaction.
	 * @param entities
	 */
	public void save(List<AbstractEntity> entities) {
		Iterator<AbstractEntity> itor = entities.iterator();
		while (itor.hasNext()) {
			itor.next().save();
		}
	}
	
	public void delete(AbstractEntity entity) {
	    entityManager.remove(entity);
	}

	public void remove(Object entity) {
	    entityManager.remove(entity);
	}
	
    public void save(ApplicationSetting appControl) {
        entityManager.persist(appControl);
    }
    
	/**
	 * Mark the provided object as persistent
	 * @param abstractEntity
	 */
	public void save(AbstractEntity abstractEntity) {
	    abstractEntity.preSave();
		entityManager.persist(abstractEntity);
	}
	
	public void recordHistory(AuditAbstractEntity audit) {
        entityManager.persist(audit);
	}

	/**
	 * Mark the provided object and its related history as persistent
	 * @param abstractEntity
	 */
	public void saveWithHistory(TemporalAbstractEntity abstractEntity, AuditAbstractEntity history) {
		if (history != null)
			entityManager.persist(history);
	}

	/**
	 * Reattach a persistent object to the current hibernate session so that the object may be persisted or updated.
	 * @param entity to reattach.
	 * @return Reattached object. Use this object for the remainder of processing.
	 */
	public AbstractEntity reattach(AbstractEntity entity) {
		return entityManager.merge(entity);
	}
	
	/**
	 * Execute a bulk update with no parameters
	 * @param queryText - the query text to be executed
	 * @return number of rows updated
	 */
	public int executeUpdate(String queryText) {
	        Query query = entityManager.createQuery(queryText);
	        return query.executeUpdate();
	}
	
    /**
     * Execute a bulk update with a single parameter
     * @param queryText - the query text to be executed
     * @param parmName parameter name
     * @param parm - parameter value
     * @return number of rows updated
     */
    public int executeUpdate(String queryText, String parmName, Object parm) {
            Query query = entityManager.createQuery(queryText);
            query.setParameter(parmName, parm);
            return query.executeUpdate();
    }
    
    /**
     * Execute a bulk update with multiple parameters
     * @param queryText - the query text to be executed
     * @param parmNames parameter names
     * @param parms - parameter values
     * @return number of rows updated
     */
    public int executeUpdate(String queryText, String[] parmNames, Object[] parms) {
            Query query = entityManager.createQuery(queryText);
            for (int i=0; i < parmNames.length; i++) {
                query.setParameter(parmNames[i], parms[i]);
            }
            return query.executeUpdate();
    }
	
	/**
	 * Execute a native update with no parameters
	 * @param queryText - the query text to be executed
	 * @return number of rows updated
	 */
	public int executeNativeUpdate(String queryText) {
	    Query query = entityManager.createNativeQuery(queryText);
	    return query.executeUpdate();
	}
	
	/**
	 * Removes an object based on a query that returns single parameter
	 * @param queryName - name of query
	 * @param parmName - name of parameter
	 * @param parm - parameter value
	 * @return void
	 * @throws RuntimeException if the remove fails
	 */
	public void executeDelete(String queryName, String parmName, Object parm) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(parmName, parm);
		try {
			entityManager.remove(executeSingleResultQuery(queryName, parmName, parm));
		}
		catch (DataAccessException e) {
			throw new OBRuntimeException(CoreTransactionErrorCode.ENTITY_DELETE_FAIL.getCode(), e);
		}
	}

    public void flush() {
    	entityManager.flush();
    }

    public void refresh(AbstractEntity entity) {
        entityManager.refresh(entity);
    }

}
