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
package com.onbelay.core.entity.model;

import javax.persistence.Transient;

import com.onbelay.core.appsetting.component.ApplicationSettingCacheManager;
import com.onbelay.core.appsetting.model.ApplicationSetting;
import com.onbelay.core.entity.component.ApplicationContextFactory;
import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.repository.BaseRepository;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.exception.JSRuntimeException;
import com.onbelay.core.exception.JSValidationException;
import com.onbelay.core.lifecycle.component.ApplicationLifecycleState;
import com.onbelay.core.lifecycle.component.GlobalThreadBeanManager;
import com.onbelay.core.lifecycle.enums.LifecycleApplicationSettings;
import com.onbelay.core.messaging.pubsub.EntityEventPublisher;

/**
 * Top-level persisted class. All  subtypes are expected to have an Integer primary key.
 * 
 *
 */
public abstract class AbstractEntity {
	
	protected abstract Long getId();

	public String getEntityName() {
		return this.getClass().getName();
	}
	
	public EntityId getEntityId() {
		return new EntityId(getId());
	}

	protected abstract BaseRepository getRepository();
	
	protected abstract void validate() throws JSValidationException; 
	
    
    @Transient
    public EntityState getEntityState() {
        return EntityState.UNMODIFIED;
    }

	public void save() {
	    try {
	        validate();
	    } catch (JSValidationException validationException) {
	        throw new JSRuntimeException(validationException);
	    }
	    getRepository().save(this);
	}
	
	/**
	 * Triggers an update by validating the object.
	 */
	public void update() {
        try {
            validate();
        } catch (JSValidationException validationException) {
            throw new JSRuntimeException(validationException);
        }
	}
	
	/**
	 * Overridden by sub-classes to provide additional behavior before the save.
	 */
	public void preSave() {
	    
	}
    
	@Override
	public int hashCode() {
		if (getId() == null)
			return super.hashCode();
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	
	protected static AuditManager getAuditManager() {
	    return (AuditManager) ApplicationContextFactory.getBean(AuditManager.BEAN_NAME); 
	}

	/**
	 * Return true if APPLICATION_STATUS is MIGRATION or ADMIN.
	 * False otherwise.
	 * 
	 * @return
	 */
    protected boolean isInAdminState() {
        ApplicationLifecycleState state = null;
        ApplicationSetting ApplicationSetting = getApplicationSettingCacheManager().getValue(LifecycleApplicationSettings.APPLICATION_STATUS.getName());

        if (ApplicationSetting != null) {
            state =  ApplicationLifecycleState.lookUp(ApplicationSetting.getValue());
        }
        if (state == ApplicationLifecycleState.ADMIN)
            return true;
        return false;
    }
	
    

	@Transient
	protected EntityEventPublisher getEventPublisher() {
		return (EntityEventPublisher) ApplicationContextFactory.getBean(EntityEventPublisher.BEAN_NAME);
	}

    
	@Transient
    protected static ApplicationSettingCacheManager getApplicationSettingCacheManager() {
    	return (ApplicationSettingCacheManager) getGlobalThreadBeanManager().getThreadBean(ApplicationSettingCacheManager.BEAN_NAME);
    }
    
	@Transient
    protected static GlobalThreadBeanManager getGlobalThreadBeanManager() {
    	return (GlobalThreadBeanManager) ApplicationContextFactory.getBean(GlobalThreadBeanManager.BEAN_NAME);
    }
}
