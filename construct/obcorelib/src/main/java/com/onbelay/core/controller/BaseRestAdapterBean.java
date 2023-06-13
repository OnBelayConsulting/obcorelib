package com.onbelay.core.controller;

import com.onbelay.core.auth.model.OBUser;
import com.onbelay.core.auth.service.ApplicationAuthenticationService;
import com.onbelay.core.entity.model.AuditManager;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRestAdapterBean {

    @Autowired
    private ApplicationAuthenticationService applicationAuthenticationService;

    @Autowired
    private AuditManager auditManager;


    protected void initializeSession() {
        OBUser user = applicationAuthenticationService.getCurrentUser();
        auditManager.setCurrentAuditUserName(user.getName());
    }



}
