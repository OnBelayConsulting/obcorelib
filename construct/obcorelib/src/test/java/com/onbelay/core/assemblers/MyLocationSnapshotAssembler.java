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
package com.onbelay.core.assemblers;

import java.util.List;
import java.util.stream.Collectors;

import com.onbelay.core.entity.assembler.EntityAssembler;
import com.onbelay.core.test.model.MyLocation;
import com.onbelay.core.test.snapshot.MyLocationSnapshot;

public class MyLocationSnapshotAssembler extends EntityAssembler {

	public MyLocationSnapshot assemble(MyLocation location) {
		
		MyLocationSnapshot snapshot = new MyLocationSnapshot();
		super.setEntityAttributes(location, snapshot);
		snapshot.getDetail().copyFrom(location.getDetail());
		return snapshot;
	}
	
	public List<MyLocationSnapshot> assemble(List<MyLocation> locations) {
		return locations
			.stream()
			.map( c -> assemble(c))
			.collect(Collectors.toList());
	}
	
}
