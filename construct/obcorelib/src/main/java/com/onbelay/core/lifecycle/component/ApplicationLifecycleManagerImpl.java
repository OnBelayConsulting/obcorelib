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
package com.onbelay.core.lifecycle.component;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.onbelay.core.entity.component.ApplicationContextFactory;
import com.onbelay.core.entity.model.AuditManager;

@Component(value="applicationLifecycleManager")
public class ApplicationLifecycleManagerImpl implements ApplicationLifecycleManager {
    private static Logger logger = LogManager.getLogger(ApplicationLifecycleManagerImpl.class);
    
    
    public void initialize() {
    	
    }
    
    public void cleanUpThread() {
        
        
        GlobalThreadBeanManager globalThreadBeanManager = (GlobalThreadBeanManager) ApplicationContextFactory.getBean(GlobalThreadBeanManager.BEAN_NAME);
        globalThreadBeanManager.cleanUpThread();
        
        
        
        
        AuditManager auditManager = (AuditManager)ApplicationContextFactory.getBean(AuditManager.BEAN_NAME);
        
        EntityManager entityManager =  ApplicationContextFactory.getCurrentEntityManagerOnThread();
        if (entityManager != null) {
            logger.error("Error - Found a current session on a thread just retrieved from the pool - should have been closed.");
            ApplicationContextFactory.closeAndRemoveEntityManagerOnThread();
        }
    }
    
}