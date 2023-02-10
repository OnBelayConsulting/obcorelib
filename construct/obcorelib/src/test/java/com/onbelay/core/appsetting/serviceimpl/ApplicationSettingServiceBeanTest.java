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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.onbelay.core.appsetting.model.ApplicationSetting;
import com.onbelay.core.appsetting.service.ApplicationSettingService;
import com.onbelay.core.appsetting.snapshot.ApplicationSettingSnapshot;
import com.onbelay.core.entity.persistence.TransactionalSpringTestCase;

@Configuration
@ComponentScan("com.onbelay.*")
@EntityScan("com.onbelay.*")
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource( locations="classpath:application-core-integrationtest.properties")
public class ApplicationSettingServiceBeanTest extends TransactionalSpringTestCase {

	
	private ApplicationSettingService applicationSettingService;
	
	
	@Override
	public void beforeRun() throws Throwable {
		super.beforeRun();
		
		ApplicationSetting applicationSetting = new ApplicationSetting();
		applicationSetting.createWith(
				new ApplicationSettingSnapshot(
						"key",
						"value"));
	}
	
	@Test
	public void testFetchAllApplicationSettings() {
		List<ApplicationSettingSnapshot> settings = applicationSettingService.fetchApplicationSettings();
		assertEquals(1, settings.size());
	}
	
	@Test
	public void testUpdate() {
		flush();
		clearCache();
		
		ApplicationSettingSnapshot applicationSettingUpdated = new ApplicationSettingSnapshot();
		applicationSettingUpdated.setKey("key");
		applicationSettingUpdated.setValue("updated");
		applicationSettingService.createOrUpdateApplicationSetting(applicationSettingUpdated);
		List<ApplicationSettingSnapshot> settings = applicationSettingService.fetchApplicationSettings();
		assertEquals(1, settings.size());

		assertEquals("updated", settings.get(0).getValue());
	}


	@Autowired
	public void setApplicationSettingService(ApplicationSettingService applicationSettingService) {
		this.applicationSettingService = applicationSettingService;
	}
	
	
	
	
}
