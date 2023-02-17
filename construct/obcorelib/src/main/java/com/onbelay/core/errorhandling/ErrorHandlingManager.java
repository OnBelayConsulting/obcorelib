package com.onbelay.core.errorhandling;

public interface ErrorHandlingManager {

    public void addErrorMessage(String errorCode, String errorMessage);

    public String getErrorMessage(String errorCode);


}
