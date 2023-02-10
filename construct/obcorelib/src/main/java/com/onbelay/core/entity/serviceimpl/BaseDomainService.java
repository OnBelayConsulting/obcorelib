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
package com.onbelay.core.entity.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.onbelay.core.appsetting.component.ApplicationSettingCacheManager;
import com.onbelay.core.entity.component.JSAuditManager;
import com.onbelay.core.lifecycle.component.GlobalThreadBeanManager;

public abstract class BaseDomainService  {

	@Autowired
	protected GlobalThreadBeanManager globalThreadBeanManager;
	
	@Autowired
	protected JSAuditManager auditManager;
	
	public ApplicationSettingCacheManager getApplicationSettingCacheManager() {
		return (ApplicationSettingCacheManager) globalThreadBeanManager.getThreadBean(ApplicationSettingCacheManager.BEAN_NAME);
	}
	
	
	protected void initializeSession(String name) {

		auditManager.setCurrentAuditUserName(name);
	}
	
}
