package com.onbelay.core.errorhandling;

import java.util.List;

public interface ErrorMessageService {

    public String getErrorMessage(String errorCode);

    public String getErrorMessage(String errorCode, List<String> parameters);

}
