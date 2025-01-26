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

import com.onbelay.core.entity.assembler.EntityAssembler;
import com.onbelay.testfixture.model.MyCustomer;
import com.onbelay.testfixture.model.MyOrganization;
import com.onbelay.testfixture.snapshot.MyCustomerSnapshot;
import com.onbelay.testfixture.snapshot.MyOrganizationSnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class MyOrganizationSnapshotAssembler extends EntityAssembler {

	public MyOrganizationSnapshot assemble(MyOrganization organization) {

		MyOrganizationSnapshot snapshot;
		if (organization instanceof MyCustomer customer) {
			snapshot = new MyCustomerSnapshot();
			((MyCustomerSnapshot)snapshot).setType(customer.getOrganizationType());
		} else {
			snapshot = new MyOrganizationSnapshot();
		}
		super.setEntityAttributes(organization, snapshot);
		snapshot.setName(organization.getName());
		snapshot.setDescription(organization.getDescription());
		return snapshot;
	}
	
	public List<MyOrganizationSnapshot> assemble(List<MyOrganization> organizations) {
		return organizations
			.stream()
			.map( c -> assemble(c))
			.collect(Collectors.toList());
	}
	
}
