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
package com.onbelay.core.appsetting.component;

import com.onbelay.core.appsetting.model.ApplicationSetting;
import com.onbelay.core.appsetting.repository.ApplicationSettingRepository;
import com.onbelay.core.entity.component.ApplicationContextFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApplicationSettingCacheManagerImpl implements ApplicationSettingCacheManager {
	
	private ConcurrentHashMap<String, ApplicationSettingWrapper> ApplicationSettings = new ConcurrentHashMap<>();

	@Override
	public ApplicationSetting getValue(String key) {
		
		
		ApplicationSettingWrapper appControlWrapper = ApplicationSettings.get(key); 
		
		if (appControlWrapper != null) {
			return appControlWrapper.getControl();
		}
		
		ApplicationSetting appControl = getApplicationSettingRepository().load(key);
		
		ApplicationSettings.put(key, new ApplicationSettingWrapper(appControl));
		
		return appControl;
	}
	
	

	@Override
	public void forget() {
		ApplicationSettings.clear(); 
	}

	private ApplicationSettingRepository getApplicationSettingRepository() {
		return (ApplicationSettingRepository) ApplicationContextFactory.getBean(ApplicationSettingRepository.BEAN_NAME);
	}

	public static class ApplicationSettingWrapper {
		
		private ApplicationSetting ApplicationSetting;
		
		public ApplicationSettingWrapper(ApplicationSetting c) {
			this.ApplicationSetting = c;
		}
		
		public boolean isNull() {
			return (ApplicationSetting == null);
		}
		
		public ApplicationSetting getControl() {
			return ApplicationSetting;
		}
	}

}
