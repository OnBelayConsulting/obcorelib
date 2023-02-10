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
package com.onbelay.core.test.snapshot;

import com.onbelay.core.entity.snapshot.AbstractSnapshot;
import com.onbelay.core.test.shared.LocationDetail;

public class MyLocationSnapshot extends AbstractSnapshot {

	private LocationDetail detail = new LocationDetail();

	public LocationDetail getDetail() {
		return detail;
	}

	public void setDetail(LocationDetail detail) {
		this.detail = detail;
	}
	
	
	
}
