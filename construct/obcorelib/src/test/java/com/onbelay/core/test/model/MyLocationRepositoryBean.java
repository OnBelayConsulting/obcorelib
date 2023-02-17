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
package com.onbelay.core.test.model;

import java.util.List;

import javax.transaction.Transactional;

import com.onbelay.core.entity.snapshot.EntityListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.onbelay.core.entity.repository.BaseRepository;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.core.test.repository.MyLocationRepository;

@Repository (value="myLocationRepository")
@Transactional
public class MyLocationRepositoryBean extends BaseRepository<MyLocation> implements MyLocationRepository {
	public static final String FIND_MY_LOCATION_BY_NAME = "FIND_MY_LOCATION_BY_NAME";
	
	@Autowired
	private MyLocationColumnDefinitions myLocationColumnDefinitions;

	public MyLocation findByName(String name) {
		return  executeSingleResultQuery(FIND_MY_LOCATION_BY_NAME, "name", name);
	}

	@Override
	public MyLocation load(EntityId entityId) {
		if (entityId.isSet())
			return (MyLocation) find(MyLocation.class, entityId.getId());
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
	public List<EntityListItem> fetchLocationList(DefinedQuery definedQuery) {
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
	public List<EntityListItem> fetchLocationListByIds(QuerySelectedPage selectedPage) {
		return fetchEntityListItemsById(
				myLocationColumnDefinitions,
				"MyLocation",
				selectedPage);
	}



}
