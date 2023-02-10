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
package com.onbelay.core.entity.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component (value="contextSetter")
public class ApplicationContextSetter implements ApplicationContextAware {

	private static final Logger logger = LogManager.getLogger();
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextFactory.setApplicationContextFromWeb(applicationContext);
		logger.info("ApplicationContext Set");
		if (logger.isDebugEnabled())
			ApplicationContextFactory.printBeans();
	}

}
