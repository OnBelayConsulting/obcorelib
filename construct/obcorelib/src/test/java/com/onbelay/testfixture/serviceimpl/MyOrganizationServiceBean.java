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
package com.onbelay.testfixture.serviceimpl;

import com.onbelay.core.assemblers.MyOrganizationSnapshotAssembler;
import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.enums.CoreErrorCode;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.core.query.parsing.DefinedQueryBuilder;
import com.onbelay.core.query.snapshot.DefinedOrderExpression;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.testfixture.model.MyCustomer;
import com.onbelay.testfixture.model.MyOrganization;
import com.onbelay.testfixture.repository.MyOrganizationRepository;
import com.onbelay.testfixture.service.MyOrganizationService;
import com.onbelay.testfixture.snapshot.MyCustomerSnapshot;
import com.onbelay.testfixture.snapshot.MyOrganizationSnapshot;
import com.onbelay.testfixture.snapshot.MyOrganizationSnapshotCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(value="myOrganizationService")
@Transactional
public class MyOrganizationServiceBean extends CoreTestServiceBean implements MyOrganizationService {
	private static Logger logger =LogManager.getLogger();
	
	@Autowired
	private MyOrganizationRepository myOrganizationRepository;
	
	@Override
	public MyOrganizationSnapshotCollection find(
			String queryText,
			int start,
			int limit) {

		initializeSession();
		
		DefinedQuery definedQuery;
		
		if (queryText.equals("default")) {
			definedQuery = new DefinedQuery("MyOrganization");
			definedQuery.getOrderByClause()
				.addOrderExpression(
					new DefinedOrderExpression("name"));
		} else {
			DefinedQueryBuilder builder = new DefinedQueryBuilder("MyOrganization", queryText);
			definedQuery = builder.build();
			
			if (definedQuery.getOrderByClause().hasExpressions() == false) {
				definedQuery.getOrderByClause()
				.addOrderExpression(
					new DefinedOrderExpression("name"));
			}
		}
		
		logger.debug("DefinedQuery: {}", definedQuery.toString());
		
		List<Integer> totalIds = myOrganizationRepository.findMyOrganizationIds(definedQuery);
		
		int toIndex =  start + limit;
		
		if (toIndex > totalIds.size())
			toIndex = totalIds.size();
		
		int fromIndex = start;
		
		if (fromIndex > toIndex)
			return new MyOrganizationSnapshotCollection(
					start,
					limit,
					totalIds.size());

		List<Integer> selectedIds =  totalIds.subList((int)fromIndex, (int)toIndex);
		
		QuerySelectedPage querySelectedPage = new QuerySelectedPage(
				definedQuery.getEntityName(),
				selectedIds,
				definedQuery.getOrderByClause());

		
		List<MyOrganization> myOrganizations = myOrganizationRepository.fetchByIds(querySelectedPage);
		
		MyOrganizationSnapshotAssembler assembler = new MyOrganizationSnapshotAssembler();
		
		List<MyOrganizationSnapshot> snapshots = assembler.assemble(myOrganizations);
		
		return new MyOrganizationSnapshotCollection(
				start,
				limit,
				totalIds.size(),
				snapshots);
		
	}



	@Override
	public TransactionResult save(MyOrganizationSnapshot snapshot) {
		initializeSession();

		MyOrganization myOrganization;
		
		if (snapshot.getEntityState() == EntityState.NEW) {
			if (snapshot instanceof MyCustomerSnapshot customerSnapshot) {
				myOrganization = new MyCustomer();
			} else {
				myOrganization = new MyOrganization();
			}
			 myOrganization.createWith(snapshot);
		} else {
			 myOrganization = myOrganizationRepository.load(snapshot.getEntityId());
			 if (myOrganization == null) 
				 throw new OBRuntimeException(CoreErrorCode.MISSING_MY_LOCATION.getCode());
			 myOrganization.updateWith(snapshot);
		}
		
		return new TransactionResult(myOrganization.getId());
	}
	
	@Override
	public MyOrganizationSnapshot load(EntityId entityId) {
		initializeSession();

		MyOrganization myOrganization = myOrganizationRepository.load(entityId);
		if (myOrganization == null)
			throw new OBRuntimeException(CoreErrorCode.MISSING_MY_LOCATION.getCode(), "" + entityId.getId());
		
		MyOrganizationSnapshotAssembler assembler = new MyOrganizationSnapshotAssembler();
		
		return assembler.assemble(myOrganization);
	}

}
