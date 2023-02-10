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

import com.onbelay.core.query.enums.ExpressionConnector;

public class ConnectorHolder extends ExpressionToken {

	private ExpressionConnector connector;

	public ConnectorHolder(ExpressionConnector connector) {
		super();
		this.connector = connector;
	}

	@Override
	public Object getValue() {
		return connector.getCode();
	}

	public ExpressionConnector getConnector() {
		return connector;
	}

	public void setConnector(ExpressionConnector connector) {
		this.connector = connector;
	}
	
	
}
