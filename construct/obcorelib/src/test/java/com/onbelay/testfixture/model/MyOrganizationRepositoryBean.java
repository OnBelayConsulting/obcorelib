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

import com.onbelay.core.entity.repository.BaseRepository;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.core.query.model.ColumnDefinitions;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.testfixture.repository.MyOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository (value="myOrganizationRepository")
@Transactional
public class MyOrganizationRepositoryBean extends BaseRepository<MyOrganization> implements MyOrganizationRepository {

	@Autowired
	private OrganizationColumnDefinitionsMap myOrganizationColumnDefinitionsMap;


	@Override
	public MyOrganization load(EntityId entityId) {
		if (entityId.isNull())
			return null;

		if (entityId.isInvalid())
			throw new OBRuntimeException(CoreTransactionErrorCode.ENTITY_DELETE_FAIL.getCode());

		if (entityId.isSet())
			return (MyOrganization) find(MyOrganization.class, entityId.getId());
		else
			return null;
	}

	@Override
	public List<Integer> findMyOrganizationIds(DefinedQuery definedQuery) {
		ColumnDefinitions columnDefinitions = myOrganizationColumnDefinitionsMap.getColumnDefinitions(definedQuery.getEntityName());
		return executeDefinedQueryForIds(
				columnDefinitions,
				definedQuery);
	}


	@Override
	public List<MyOrganization> fetchByIds(QuerySelectedPage selectedPage) {
		String entityName = "MyOrganization";
		if (selectedPage.getEntityName() != null)
			entityName = selectedPage.getEntityName();

		ColumnDefinitions columnDefinitions = myOrganizationColumnDefinitionsMap.getColumnDefinitions(entityName);
		return fetchEntitiesById(
				columnDefinitions,
				entityName,
				selectedPage);
	}
}
