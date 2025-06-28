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
package com.onbelay.core.appsetting.serviceimpl;

import com.onbelay.core.appsetting.model.ApplicationSetting;
import com.onbelay.core.appsetting.repository.ApplicationSettingRepository;
import com.onbelay.core.appsetting.service.ApplicationSettingService;
import com.onbelay.core.appsetting.snapshot.ApplicationSettingSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value="applicationSettingService")
@Transactional

public class ApplicationSettingServiceBean implements ApplicationSettingService {
	private static final Logger logger = LogManager.getLogger(ApplicationSettingServiceBean.class);
	
	private ApplicationSettingRepository applicationSettingRepository;

	@Override
	public ApplicationSettingSnapshot load(String key) {
		ApplicationSetting ac = applicationSettingRepository.load(key);
		ApplicationSettingSnapshot value = new ApplicationSettingSnapshot();
		if (ac != null) {
			value.setKey(ac.getKey());
			value.setValue(ac.getValue());
		} else {
			value.setKey(key);
		}
		return value;
	}


	@Override
	public void createOrUpdateApplicationSetting(ApplicationSettingSnapshot snapshot) {
		
        ApplicationSetting currentSetting = applicationSettingRepository.load(snapshot.getKey());
        
        if (currentSetting != null) {
        	currentSetting.setValue(snapshot.getValue());
        } else {
        	currentSetting = new ApplicationSetting();
        	currentSetting.createWith(snapshot);
        }
	}


    
    @Override
	public String readApplicationSetting(String key) {
        ApplicationSetting appControl = applicationSettingRepository.load(key);
        if (appControl != null) {
        	return appControl.getValue();
        } else {
        	return null;
        }
	}

	@Override
	public List<ApplicationSettingSnapshot> fetchApplicationSettings() {
		List<ApplicationSetting> controls = applicationSettingRepository.loadAll();
		List<ApplicationSettingSnapshot> values = new ArrayList<>();
		
		for (ApplicationSetting c : controls) {
			ApplicationSettingSnapshot value = new ApplicationSettingSnapshot();
			value.setKey(c.getKey());
			value.setValue(c.getValue());
			values.add(value);
		}
		return values;
	}

	@Override
	public void createOrUpdateApplicationSettings(List<ApplicationSettingSnapshot> ApplicationSettingSnapshots) {
		
		
		for (ApplicationSettingSnapshot applicationSettingSnapshot : ApplicationSettingSnapshots) {
			
			createOrUpdateApplicationSetting(applicationSettingSnapshot);
		}
	}

	@Autowired
	public void setApplicationSettingRepository(ApplicationSettingRepository applicationSettingRepository) {
		this.applicationSettingRepository = applicationSettingRepository;
	}

	
	
}
