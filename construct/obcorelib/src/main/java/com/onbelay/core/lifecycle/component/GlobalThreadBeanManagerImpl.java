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

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.onbelay.core.entity.component.ApplicationContextFactory;

@Component(value="globalThreadBeanManager")
public class GlobalThreadBeanManagerImpl implements GlobalThreadBeanManager {
	
    private static ThreadLocal<ThreadLocalContainer> threadLocalContainerHolder = new ThreadLocal<ThreadLocalContainer>() {
        protected synchronized ThreadLocalContainer initialValue() {
            return new ThreadLocalContainer();
        }
    };
    

	@Override
	public void initialize() {
	}

	@Override
	public ThreadBean getThreadBean(String beanName) {
		
		ThreadBean bean = threadLocalContainerHolder.get().get(beanName);
		if (bean != null)
			return bean;
		
		ThreadBeanFactory threadBeanFactory = (ThreadBeanFactory) ApplicationContextFactory.getBean(beanName);
		
		bean = threadBeanFactory.newInstance();
		
		threadLocalContainerHolder.get().put(beanName, bean);
		
		return bean;
	}

	@Override
	public Object getThreadSetting(String key) {
		return threadLocalContainerHolder.get().getSetting(key);
	}

	@Override
	public boolean hasThreadSetting(String key) {
		return (threadLocalContainerHolder.get().getSetting(key) != null);
	}

	@Override
	public void putThreadSetting(String key, Object value) {
		threadLocalContainerHolder.get().putSetting(key, value);
	}

	@Override
	public void terminate() {
	}
	
	
	public void cleanUpThread() {
		
		threadLocalContainerHolder.get().cleanUp();
		threadLocalContainerHolder.remove();
	}
		
	
	private static class ThreadLocalContainer {
		private HashMap<String, ThreadBean> threadBeans = new HashMap<>();
		private HashMap<String, Object> threadSettings = new HashMap<>();
		
		public ThreadBean get(String beanName) {
			return threadBeans.get(beanName);
		}
		
		public void put(String beanName, ThreadBean bean) {
			threadBeans.put(beanName, bean);
		}
		
		public void putSetting(String key, Object value) {
			threadSettings.put(key, value);
		}
		
		public Object getSetting(String key) {
			return threadSettings.get(key);
		}
		
		public Boolean getSettingAsBoolean(String key) {
			return (Boolean) threadSettings.get(key);
		}
		
		public void cleanUp() {
			for (ThreadBean bean : threadBeans.values()) {
				bean.cleanupThread();
			}
			threadSettings.clear();
			
			threadBeans.clear();
			
		}
		
	}

}
