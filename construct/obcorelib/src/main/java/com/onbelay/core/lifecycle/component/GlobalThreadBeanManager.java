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

public interface GlobalThreadBeanManager {
	public static final String BEAN_NAME = "globalThreadBeanManager"; 
	
	public void initialize();
	
	public ThreadBean getThreadBean(String beanName);
	
	public Object getThreadSetting(String key);
	
	public boolean hasThreadSetting(String key);
	
	public void putThreadSetting(String key, Object value);
	
	public void cleanUpThread();
	
	public void terminate();

}
