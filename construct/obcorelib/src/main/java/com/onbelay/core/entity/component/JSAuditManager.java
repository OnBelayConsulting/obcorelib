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

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.onbelay.core.entity.model.AuditManager;

/**
 * 
 * The audit manager provides support for audit information written into the audit tables.
 *
 */
@Component(value="auditManager")
public class JSAuditManager implements AuditManager {
    private static volatile ThreadLocal<AuditHolder> auditHolder;

    public String getCurrentAuditUserName() {
        AuditHolder currentHolder = getCurrentAuditHolder();
        return currentHolder.getAuditUser();
    }
    
    public void setCurrentAuditUserName(String name) {
        AuditHolder currentHolder = getCurrentAuditHolder();
        currentHolder.setAuditUser(name);
    }
    
    public LocalDateTime getCurrentHistoryDate() {
        AuditHolder currentHolder = getCurrentAuditHolder();
        return currentHolder.getHistoryDate();
    }
    
    public void setCurrentHistoryDate(LocalDateTime date) {
        AuditHolder currentHolder = getCurrentAuditHolder();
        currentHolder.setHistoryDate(date);
    }
    
    public void setCurrentAuditComments(String comments) {
        AuditHolder currentHolder = getCurrentAuditHolder();
        currentHolder.setAuditComments(comments);
    }

    public String getCurrentAuditComments() {
        AuditHolder currentHolder = getCurrentAuditHolder();
        return currentHolder.getAuditComments();
    }

    private AuditHolder getCurrentAuditHolder() {
        if (auditHolder == null)
            auditHolder = new ThreadLocal<AuditHolder>();
        
        if (auditHolder.get() == null)
            auditHolder.set(new AuditHolder(LocalDateTime.now()));
        
        return auditHolder.get();
    }
    
    
    public void currentUserSessionCleanup() {
        
        if (auditHolder != null)
            auditHolder.set(null);
    }
    
    public static class AuditHolder {
        private LocalDateTime historyDate;
        private String auditComments = "";
        private String auditUser;

        public AuditHolder(LocalDateTime historyDate) {
            super();
            this.historyDate = historyDate;
        }

        public LocalDateTime getHistoryDate() {
            return historyDate;
        }

        public void setHistoryDate(LocalDateTime historyDate) {
            this.historyDate = historyDate;
        }
        
        public void setAuditComments(String comments) {
            this.auditComments = comments;
            
        }
        
        public String getAuditComments() {
            return auditComments;
        }

        public String getAuditUser() {
            return auditUser;
        }

        public void setAuditUser(String auditUser) {
            this.auditUser = auditUser;
        }
        
        
    }



}
