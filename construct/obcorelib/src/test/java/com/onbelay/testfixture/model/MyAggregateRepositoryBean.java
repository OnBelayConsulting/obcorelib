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
import com.onbelay.testfixture.repository.MyAggregateRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository (value="myAggregateRepository")
@Transactional
public class MyAggregateRepositoryBean extends BaseRepository<MyAggregate> implements MyAggregateRepository {

	private static String UPDATE_QUERY = "UPDATE MyAggregate set detail.name = :name WHERE detail.name = :previousName";

	@Override
	public boolean updateName(
			String name,
			String previousName) {

		String[] names = {"name", "previousName"};
		Object[] parms = {name, previousName};
		int result = executeUpdate(UPDATE_QUERY, names, parms);
		return result > 0;
	}

	@Override
	public MyAggregate load(EntityId entityId) {
		if (entityId.isNull())
			return null;

		if (entityId.isInvalid())
			throw new OBRuntimeException(CoreTransactionErrorCode.ENTITY_DELETE_FAIL.getCode());

		if (entityId.isSet())
			return (MyAggregate) find(MyAggregate.class, entityId.getId());
		else
			return null;
	}

}
