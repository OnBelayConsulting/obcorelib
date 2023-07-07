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
package com.onbelay.testfixture.snapshot;

import com.onbelay.core.entity.snapshot.AbstractSnapshot;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.testfixture.shared.LocationDetail;
import com.onbelay.testfixture.shared.MyAggregateDetail;

import java.util.List;

public class MyAggregateSnapshot extends AbstractSnapshot {

	private EntityId locationId;

	private MyAggregateDetail detail = new MyAggregateDetail();

	public MyAggregateSnapshot() {
	}

	public MyAggregateSnapshot(EntityId entityId) {
		super(entityId);
	}

	public MyAggregateSnapshot(String errorCode) {
		super(errorCode);
	}

	public MyAggregateSnapshot(String errorCode, boolean isPermissionException) {
		super(errorCode, isPermissionException);
	}

	public MyAggregateSnapshot(String errorCode, List<String> parameters) {
		super(errorCode, parameters);
	}

	public EntityId getLocationId() {
		return locationId;
	}

	public void setLocationId(EntityId locationId) {
		this.locationId = locationId;
	}

	public MyAggregateDetail getDetail() {
		return detail;
	}

	public void setDetail(MyAggregateDetail detail) {
		this.detail = detail;
	}
}
