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
package com.onbelay.testfixture.model;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.core.query.model.ColumnDefinition;
import com.onbelay.testfixture.repository.MyLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.onbelay.core.entity.repository.BaseRepository;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;

@Repository (value="myLocationRepository")
@Transactional
public class MyLocationRepositoryBean extends BaseRepository<MyLocation> implements MyLocationRepository {
	public static final String FIND_MY_LOCATION_BY_NAME = "FIND_MY_LOCATION_BY_NAME";

	private static final String LOCATION_NO_SEQUENCE_NAME = "LOCATIONNO_SQ";

	@Autowired
	private MyLocationColumnDefinitions myLocationColumnDefinitions;

	public MyLocation findByName(String name) {
		return  executeSingleResultQuery(FIND_MY_LOCATION_BY_NAME, "name", name);
	}


	public Integer nextLocationNo() {
		return getNextSequenceValue(LOCATION_NO_SEQUENCE_NAME);
	}

	@Override
	public MyLocation load(EntityId entityId) {
		if (entityId.isNull())
			return null;

		if (entityId.isInvalid())
			throw new OBRuntimeException(CoreTransactionErrorCode.ENTITY_DELETE_FAIL.getCode());

		if (entityId.isSet())
			return (MyLocation) find(MyLocation.class, entityId.getId());
		else if (entityId.getCode() != null)
			return findByName(entityId.getCode());
		else
			return null;
	}

	@Override
	public List<Integer> findMyLocationIds(DefinedQuery definedQuery) {
		return executeDefinedQueryForIds(
				myLocationColumnDefinitions, 
				definedQuery);
	}

	@Override
	public List<EntityId> fetchLocationList(DefinedQuery definedQuery) {
		return executeDefinedQueryForList(
				myLocationColumnDefinitions,
				definedQuery);
	}

	@Override
	public List<MyLocation> fetchByIds(QuerySelectedPage selectedPage) {
		return fetchEntitiesById(
				myLocationColumnDefinitions,
				"MyLocation",
				selectedPage);
	}

	@Override
	public List<EntityId> fetchLocationListByIds(QuerySelectedPage selectedPage) {

		ArrayList<ColumnDefinition> properties = new ArrayList<>();
		properties.add(MyLocationColumnDefinitions.id);
		properties.add(MyLocationColumnDefinitions.name);
		properties.add(MyLocationColumnDefinitions.description);
		properties.add(MyLocationColumnDefinitions.locationNo);
		properties.add(MyLocationColumnDefinitions.IS_EXPIRED);

		List<Object[]> items = executePropertiesQueryFromIds(
				myLocationColumnDefinitions,
				properties,
				"MyLocation",
				selectedPage);

		ArrayList<EntityId> entityIds = new ArrayList<>();

		for ( Object[] props : items) {
			Integer id = 				(Integer) props[0];
			String name = 	             (String) props[1];
			String description = 	     (String) props[2];
			Integer locationNo = 	    (Integer) props[3];
			Boolean isExpired = 		(Boolean) props[4];

			entityIds.add(
					new EntityId(
						id,
						name,
						composeDescription(name, description, locationNo),
						isExpired));
		}

		return entityIds;
	}

	private String composeDescription(String name, String description, Integer locationNo) {
		return name + "-" + locationNo + "-" + description;
	}

}
