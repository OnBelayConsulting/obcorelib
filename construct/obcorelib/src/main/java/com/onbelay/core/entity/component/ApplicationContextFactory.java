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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 
 */
public class ApplicationContextFactory {
    
	private static Logger logger = LogManager.getLogger(ApplicationContextFactory.class);
	private static ApplicationContext applicationContext;
	private static boolean isTest = false;
	
	public static synchronized void setApplicationContextFromWeb(ApplicationContext webApplicationContext) {
	    applicationContext = webApplicationContext;
	    isTest = false;
	}
	
	public static synchronized void useTest() {
		isTest = true;
	}
	
	/**
	 * return a spring managed bean - see the context xml file for beans.
	 * @param name of bean
	 * @return managed bean instance
	 */
	public static Object getBean(String name) {
	    return getApplicationContext().getBean(name);
	}

	public static <T> Object getBean(String name, Class<T> clazz) {
	    return getApplicationContext().getBean(name, clazz);
	}

    public static boolean hasApplicationContext() {
        return getApplicationContext() != null;
    }

	public static void printBeans() {
		for (String s : applicationContext.getBeanDefinitionNames()) {
			logger.debug(s);
		}
	}
	
	
	/**
	 * Utility method for testing session stuff
	 * @return
	 */
	public static EntityManager getCurrentEntityManagerOnThread() {
		EntityManagerHolder emHolder =(EntityManagerHolder) TransactionSynchronizationManager.getResource(getEntityManagerFactory());
		if (emHolder != null)
			return emHolder.getEntityManager();
		else
			return null;
	}
	
	public static void closeAndRemoveEntityManagerOnThread() {
        EntityManagerHolder emHolder =(EntityManagerHolder) TransactionSynchronizationManager.getResource(getEntityManagerFactory());
        if (emHolder == null || emHolder.getEntityManager() == null) {
            return;
        }
        
        if (emHolder.getEntityManager().getTransaction().isActive()) {
          logger.error("Current transaction is active on a thread just retrieved from pool.");
          logger.error("Transaction name: " + TransactionSynchronizationManager.getCurrentTransactionName());
          emHolder.getEntityManager().getTransaction().rollback();
        }
        
        try {         
          logger.error("Terminating session on a just acquired thread.");
          emHolder.getEntityManager().close();
        }
        catch (PersistenceException ex) {
            logger.error("Could not close JPA EntityManager" + ex.getMessage());
        }
        catch (Throwable ex) {
            logger.error("Unexpected exception on closing JPA EntityManager" + ex);
        } finally {
            TransactionSynchronizationManager.unbindResource(getEntityManagerFactory());
        }
	    
	}
	
	public static EntityManagerFactory getEntityManagerFactory() {
		return (EntityManagerFactory) getBean("entityManagerFactory");
	}
	
	public static void shutdown() {
	    ConfigurableApplicationContext cac = (ConfigurableApplicationContext) getApplicationContext();
	    cac.stop();
	}
	
	public static ApplicationContext getApplicationContext() {
	    return applicationContext;
	}

    /**
     * @return
     */
    public static SessionFactory getSessionFactory() {
        EntityManagerFactory entityManagerFactory = getEntityManagerFactory();
        HibernateEntityManagerFactory hFactory = (HibernateEntityManagerFactory) entityManagerFactory;
        return hFactory.getSessionFactory();
    }

    public static void flushIfInSession() {
        EntityManager entityManager = ApplicationContextFactory.getCurrentEntityManagerOnThread();
        if (entityManager != null)
            entityManager.flush();
    }
}