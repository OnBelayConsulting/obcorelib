package com.onbelay.core.errorhandlingimpl;

import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.errorhandling.ErrorHandlingManager;

import java.util.EnumSet;
import java.util.concurrent.ConcurrentHashMap;

public class CoreErrorHandlingManager implements ErrorHandlingManager {

    private ConcurrentHashMap<String, String> errorMap = new ConcurrentHashMap<>(100, .25f);

    public CoreErrorHandlingManager() {
        initialize();
    }

    protected void initialize() {
        for (CoreTransactionErrorCode c : EnumSet.allOf(CoreTransactionErrorCode.class))
            addErrorMessage(c.getCode(), c.getDescription());
    }

    public void addErrorMessage(String errorCode, String errorMessage) {
        errorMap.put(errorCode, errorMessage);
    }

    public String getErrorMessage(String errorCode) {
        return errorMap.get(errorCode);
    }

}
