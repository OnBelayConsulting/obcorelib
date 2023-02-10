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
package com.onbelay.core.lifecycle.serviceimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onbelay.core.appsetting.component.ApplicationSettingCacheManager;
import com.onbelay.core.appsetting.model.ApplicationSetting;
import com.onbelay.core.appsetting.repository.ApplicationSettingRepository;
import com.onbelay.core.appsetting.snapshot.ApplicationSettingSnapshot;
import com.onbelay.core.lifecycle.component.ApplicationLifecycleState;
import com.onbelay.core.lifecycle.component.GlobalThreadBeanManager;
import com.onbelay.core.lifecycle.enums.LifecycleApplicationSettings;
import com.onbelay.core.lifecycle.service.ApplicationLifecycleService;

@Service(value="applicationLifecycleService")
@Transactional
public class ApplicationLifecycleServiceBean implements ApplicationLifecycleService {
    private static final Logger logger = LogManager.getLogger(ApplicationLifecycleServiceBean.class);

    private boolean isInitialized = false;
    private GlobalThreadBeanManager globalThreadBeanManager;
    private ApplicationSettingRepository ApplicationSettingRepository;
    
    public ApplicationLifecycleState getLifecycleState() {
    	if (isInitialized == false)
    		initialize();

    	
        ApplicationSetting ApplicationSetting = getApplicationSettingCacheManager().getValue(LifecycleApplicationSettings.APPLICATION_STATUS.getName());
        if (ApplicationSetting != null) {
            return ApplicationLifecycleState.lookUp(ApplicationSetting.getValue());
        }
        return null;
    }
    
    public synchronized void initialize() {
    	
        ApplicationSetting ApplicationSetting = getApplicationSettingCacheManager().getValue(LifecycleApplicationSettings.APPLICATION_STATUS.getName());

        ApplicationLifecycleState state = null;
        if (ApplicationSetting != null) {
            state =  ApplicationLifecycleState.lookUp(ApplicationSetting.getValue());
        }
        
        if (state != null) {
        	if (state == ApplicationLifecycleState.SHUTTING_DOWN)
        		updateLifecycleState(state, ApplicationLifecycleState.RUNNING);
        } else {
            boolean succeeded = saveLifecycleState(ApplicationLifecycleState.RUNNING);
            if (succeeded == false) {
                logger.info("Save failed - must have been created by another webapp");
            }
        }
        isInitialized = true;
    }

    public boolean transitionState(ApplicationLifecycleState requestedState) {
    	
    	if (isInitialized == false)
    		initialize();
    	
        ApplicationLifecycleState state = getLifecycleState();
    	
        if (state == requestedState)
        	return true;
        
        if (state == ApplicationLifecycleState.SHUTTING_DOWN) {
            logger.error("Cannot transition state if shutting down");
            return false;
        }
        
        
        if (requestedState == ApplicationLifecycleState.SHUTTING_DOWN) {
        	logger.info("Transitioning from " + state.getName() + " to " + ApplicationLifecycleState.SHUTTING_DOWN.getName());
            return updateLifecycleState(state, ApplicationLifecycleState.SHUTTING_DOWN);
        }
        
        if (requestedState == ApplicationLifecycleState.ADMIN) {
            return updateLifecycleState(state, ApplicationLifecycleState.ADMIN);
        }
        
        if (state == ApplicationLifecycleState.ADMIN ) {
                if (requestedState == ApplicationLifecycleState.RUNNING) {
                boolean succeeded = updateLifecycleState(state,requestedState);
                if (succeeded){
                	if(requestedState == ApplicationLifecycleState.RUNNING) {
                		logger.info("Successfully transitioning state to :" + requestedState.getName() + ".  Please restart Tomcat before proceeding.");
                	} else {
                		logger.info("Successfully transitioning state to :" + requestedState.getName());
                	}
                }
                else
                    logger.error("Failed to transition state to :" + requestedState);
                return succeeded;
            } else {
                logger.error("Requested state change " + requestedState.getName() + " is not legal with current state: " + state.getName());
                return false;
            }
        }
        
        
        if (state == ApplicationLifecycleState.RUNNING ) {
            if (requestedState == ApplicationLifecycleState.ADMIN) {
                boolean succeeded = updateLifecycleState(state,requestedState);
                if (succeeded) 
                    logger.info("Successfully transitioning state to :" + requestedState.getName());
                else
                    logger.error("Failed to transition state to :" + requestedState);
                return succeeded;
            } else {
                logger.error("Requested state change " + requestedState.getName() + " is not legal with current state: " + state.getName());
                return false;
            }
        }
        
        logger.error("Requested state change " + requestedState.getName() + " is not legal with current state: " + state.getName());
        return false;
    }
    

	public boolean saveLifecycleState(ApplicationLifecycleState state) {
        ApplicationSetting ApplicationSetting = new ApplicationSetting();
        try {
            ApplicationSetting.createWith(
            		new ApplicationSettingSnapshot(
            				LifecycleApplicationSettings.APPLICATION_STATUS.getName(), 
            				state.getCode()));
        } catch (Throwable t) {
            logger.error("Lifecycle state save failed", t);
            return false;
        }
        
        return true;

    }

    public boolean updateLifecycleState(ApplicationLifecycleState oldState, ApplicationLifecycleState newState) {
        ApplicationSetting ApplicationSetting = ApplicationSettingRepository.load(LifecycleApplicationSettings.APPLICATION_STATUS.getName());
        
        if (ApplicationSetting.getValue().equals(oldState.getCode()) == false) {
            logger.info("Lifecycle state has been updated.");
            return false;
        }
        
        return ApplicationSettingRepository.selectForUpdate(ApplicationSetting, newState.getCode());
    }

    
	public ApplicationSettingCacheManager getApplicationSettingCacheManager() {
		return (ApplicationSettingCacheManager) globalThreadBeanManager.getThreadBean(ApplicationSettingCacheManager.BEAN_NAME);
	}
	
	@Autowired
	public void setGlobalThreadBeanManager(GlobalThreadBeanManager globalThreadBeanManager) {
		this.globalThreadBeanManager = globalThreadBeanManager;
	}
	
	@Autowired
	public void setApplicationSettingRepository(ApplicationSettingRepository ApplicationSettingRepository) {
		this.ApplicationSettingRepository = ApplicationSettingRepository;
	}
	

}
