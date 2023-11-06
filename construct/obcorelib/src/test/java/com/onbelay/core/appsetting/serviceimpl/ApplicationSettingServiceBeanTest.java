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
import com.onbelay.core.appsetting.service.ApplicationSettingService;
import com.onbelay.core.appsetting.snapshot.ApplicationSettingSnapshot;
import com.onbelay.core.test.CoreSpringTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationSettingServiceBeanTest extends CoreSpringTestCase {


	@Autowired
	private ApplicationSettingService applicationSettingService;

	@Override
	public void setUp() {
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


}
