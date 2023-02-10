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
package com.onbelay.core.test.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onbelay.core.assemblers.MyLocationSnapshotAssembler;
import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.enums.CoreErrorCode;
import com.onbelay.core.exception.JSRuntimeException;
import com.onbelay.core.query.parsing.DefinedQueryBuilder;
import com.onbelay.core.query.snapshot.DefinedOrderExpression;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.core.test.model.MyLocation;
import com.onbelay.core.test.repository.MyLocationRepository;
import com.onbelay.core.test.service.MyLocationService;
import com.onbelay.core.test.snapshot.MyLocationSnapshot;
import com.onbelay.core.test.snapshot.MyLocationSnapshotCollection;


@Service(value="myLocationService")
@Transactional
public class MyLocationServiceBean extends CoreTestServiceBean implements MyLocationService {
	private static Logger logger =LogManager.getLogger();
	
	@Autowired
	private MyLocationRepository myLocationRepository;
	
	@Override
	public MyLocationSnapshotCollection find(
			String queryText,
			long start,
			int limit) {

		initializeSession();
		
		DefinedQuery definedQuery;
		
		if (queryText.equals("default")) {
			definedQuery = new DefinedQuery("MyLocation");
			definedQuery.getOrderByClause()
				.addOrderExpression(
					new DefinedOrderExpression("name"));
		} else {
			DefinedQueryBuilder builder = new DefinedQueryBuilder("MyLocation", queryText);
			definedQuery = builder.build();
			
			if (definedQuery.getOrderByClause().hasExpressions() == false) {
				definedQuery.getOrderByClause()
				.addOrderExpression(
					new DefinedOrderExpression("name"));
			}
		}
		
		logger.debug("DefinedQuery: {}", definedQuery.toString());
		
		List<Long> totalIds = myLocationRepository.findMyLocationIds(definedQuery);
		
		long toIndex =  start + limit;
		
		if (toIndex > totalIds.size())
			toIndex = totalIds.size();
		
		long fromIndex = start;
		
		if (fromIndex > toIndex)
			return new MyLocationSnapshotCollection(totalIds.size());

		List<Long> selectedIds =  totalIds.subList((int)fromIndex, (int)toIndex);
		
		QuerySelectedPage querySelectedPage = new QuerySelectedPage(
				selectedIds,
				definedQuery.getOrderByClause());

		
		List<MyLocation> myLocations = myLocationRepository.fetchByIds(querySelectedPage);
		
		MyLocationSnapshotAssembler assembler = new MyLocationSnapshotAssembler();
		
		List<MyLocationSnapshot> snapshots = assembler.assemble(myLocations);
		
		return new MyLocationSnapshotCollection(snapshots, totalIds.size());
		
	}
	
	
	
	@Override
	public TransactionResult createOrUpdateMyLocations(List<MyLocationSnapshot> snapshots) {
		initializeSession();

		ArrayList<EntityId> ids = new ArrayList<EntityId>();
		
		for (MyLocationSnapshot snapshot: snapshots) {

			MyLocation myLocation;
			
			if (snapshot.getEntityState() == EntityState.NEW) {
				 myLocation = MyLocation.create(snapshot);
			} else {
				 myLocation = myLocationRepository.load(snapshot.getEntityId());
				 if (myLocation == null) {
					 logger.error( "MyLocation id: {} is missing", snapshot.getEntityId());
					 throw new JSRuntimeException(CoreErrorCode.MISSING_MY_LOCATION.getCode());
				 }
				 logger.debug( "Update myLocation # ", myLocation.getDetail().getName());
				 myLocation.updateWith(snapshot);
			}
			ids.add(myLocation.getEntityId());
		}
		
		return new TransactionResult(ids);
	}



	@Override
	public TransactionResult createOrUpdate(MyLocationSnapshot snapshot) {
		initializeSession();

		MyLocation myLocation;
		
		if (snapshot.getEntityState() == EntityState.NEW) {
			 myLocation = MyLocation.create(snapshot);
		} else {
			 myLocation = myLocationRepository.load(snapshot.getEntityId());
			 if (myLocation == null) 
				 throw new JSRuntimeException(CoreErrorCode.MISSING_MY_LOCATION.getCode());
			 myLocation.updateWith(snapshot);
		}
		
		return new TransactionResult(myLocation.getEntityId());
	}
	
	@Override
	public MyLocationSnapshot load(EntityId entityId) {
		initializeSession();

		MyLocation myLocation = myLocationRepository.load(entityId);
		if (myLocation == null)
			throw new JSRuntimeException(CoreErrorCode.MISSING_MY_LOCATION.getCode(), "" + entityId.getId());
		
		MyLocationSnapshotAssembler assembler = new MyLocationSnapshotAssembler();
		
		return assembler.assemble(myLocation);
	}

	
	@Override
	public MyLocationSnapshot findByName(String name) {
		initializeSession();

		MyLocation myLocation = myLocationRepository.findByName(name);
		if (myLocation == null)
			throw new JSRuntimeException(CoreErrorCode.MISSING_MY_LOCATION.getCode(), name);
		
		MyLocationSnapshotAssembler assembler = new MyLocationSnapshotAssembler();
		
		return assembler.assemble(myLocation);
	}


}
