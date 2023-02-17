package com.onbelay.core.errorhandlingimpl;

import com.onbelay.core.errorhandling.ErrorHandlingManager;
import com.onbelay.core.errorhandling.ErrorMessageService;
import com.onbelay.core.utils.ErrorMessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ErrorMessageServiceBean implements ErrorMessageService {

    @Autowired
    private ErrorHandlingManager errorHandlingManager;

    @Override
    public String getErrorMessage(String errorCode) {
        return errorHandlingManager.getErrorMessage(errorCode);
    }

    @Override
    public String getErrorMessage(String errorCode, List<String> parameters) {
        ErrorMessageFormatter formatter = new ErrorMessageFormatter(
                errorHandlingManager.getErrorMessage(errorCode),
                parameters);

        return formatter.getFormattedMessage();
    }
}
