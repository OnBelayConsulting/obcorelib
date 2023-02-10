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
package com.onbelay.core.query.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InListHolder extends ExpressionToken {
	
	private List<ExpressionToken> items = new ArrayList<ExpressionToken>();

	public InListHolder(List<ExpressionToken> items) {
		super();
		this.items = items;
	}

	public List<ExpressionToken> getItems() {
		return items;
	}
	
	public Object getValue() {
		return
			items
				.stream()
				.map(ExpressionToken::getValue)
				.collect(Collectors.toList());
	}

}
