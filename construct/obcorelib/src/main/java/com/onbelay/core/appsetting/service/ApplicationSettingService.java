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
package com.onbelay.core.appsetting.service;

import com.onbelay.core.appsetting.snapshot.ApplicationSettingSnapshot;

import java.util.List;

public interface ApplicationSettingService {
	public static final String BEAN_NAME = "applicationSettingService";

    public String readApplicationSetting(String key);
    
    public ApplicationSettingSnapshot load(String key);

    public void createOrUpdateApplicationSetting(ApplicationSettingSnapshot snapshot);
    
    public List<ApplicationSettingSnapshot> fetchApplicationSettings();
    
    public void createOrUpdateApplicationSettings(List<ApplicationSettingSnapshot> ApplicationSettings);
	
}
