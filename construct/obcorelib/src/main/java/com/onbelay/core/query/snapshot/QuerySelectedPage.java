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
package com.onbelay.core.query.snapshot;

import java.util.ArrayList;
import java.util.List;

public class QuerySelectedPage {
	
	private List<Long> ids = new ArrayList<Long>();
	
	private DefinedOrderByClause orderByClause;

	public QuerySelectedPage(List<Long> ids, DefinedOrderByClause orderByClause) {
		super();
		this.ids = ids;
		this.orderByClause = orderByClause;
	}
	

	public QuerySelectedPage(List<Long> ids) {
		super();
		this.ids = ids;
	}


	public List<Long> getIds() {
		return ids;
	}


	public DefinedOrderByClause getOrderByClause() {
		return orderByClause;
	}


	public boolean hasOrderClause() {
		return orderByClause != null;
	}
	
	

}
