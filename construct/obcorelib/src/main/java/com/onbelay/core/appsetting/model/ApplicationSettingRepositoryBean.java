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
package com.onbelay.core.appsetting.model;

import com.onbelay.core.appsetting.repository.ApplicationSettingRepository;
import com.onbelay.core.entity.repository.BaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository (value="applicationSettingRepository")
@Transactional
public class ApplicationSettingRepositoryBean extends BaseRepository<ApplicationSetting> implements ApplicationSettingRepository {
	public static final String LOAD_ALL = "load_all_application_setting";

    
    public static final String UPDATE_APP_CONTROL_VALUE = "UPDATE ApplicationSetting set value = :value " +
    		                                              " WHERE keyValue = :key and value = :oldValue ";


	
    public List<ApplicationSetting> loadAll() {
    	return (List<ApplicationSetting>) executeReportQuery(LOAD_ALL);
    }
    
    /**
     * Update an existing application control value with a batch update that insures that the update will only exceed if the 
     * old value is unchanged
     * @param key - identifies the application control to change
     * @param oldValue - current value
     * @param newValue - value to set
     * @return 1 if this update exceeds or 0 if the update fails because of stale data.
     */
    public int batchUpdateAppControl(String key, String oldValue, String newValue) {
        String [] parmNames = {"key", "oldValue", "value" };
        Object[]  parms = {key, oldValue, newValue };
        return executeUpdate(UPDATE_APP_CONTROL_VALUE, parmNames, parms);
    }
    

    
    public void save(ApplicationSetting applicationSetting) {
    	entityManager.persist(applicationSetting);
    }
    
    public ApplicationSetting find(String keyValue) {
    	return  loadNonEntity(ApplicationSetting.class, keyValue);
    }
    
    /**
     * Calls the batchUpdate with the old value as a "Select for Update".
     * @param newValue
     * @return true if the update exceeds.
     */
    public boolean selectForUpdate(ApplicationSetting ApplicationSetting, String newValue) {
        return (1 == batchUpdateAppControl(
        		ApplicationSetting.getKey(), 
        		ApplicationSetting.getValue(), 
        		newValue));
    }

    
    public ApplicationSetting load(String key) {
        ApplicationSetting appCtl = (ApplicationSetting) loadNonEntity(ApplicationSetting.class, key);
        
        if (appCtl != null) {
            return appCtl;
        }
        return null;
    }
    
	public void remove(ApplicationSetting applicationSetting) {
		entityManager.remove(applicationSetting);
		
	}

}
