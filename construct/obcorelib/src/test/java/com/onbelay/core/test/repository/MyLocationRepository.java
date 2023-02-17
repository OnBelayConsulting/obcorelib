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
package com.onbelay.core.test.repository;

import java.util.List;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.EntityListItem;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.core.test.model.MyLocation;

public interface MyLocationRepository {
	public static final String BEAN_NAME = "myLocationRepository";
	
	public MyLocation load(EntityId id);
	
	public MyLocation findByName(String name);

	public List<Integer> findMyLocationIds(DefinedQuery definedQuery);

	public List<EntityListItem> fetchLocationList(DefinedQuery definedQuery);

	public List<EntityListItem> fetchLocationListByIds(QuerySelectedPage selectedPage);


	public List<MyLocation> fetchByIds(QuerySelectedPage selectedPage);

	
}
