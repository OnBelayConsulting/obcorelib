package com.onbelay.core.errorhandlingimpl;

import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.errorhandling.ErrorHandlingManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Configuration
public class ErrorHandlingConfig {

    @Bean
    public ErrorHandlingManager errorHandlingManager() {
        CoreErrorHandlingManager manager = new  CoreErrorHandlingManager();
        return manager;
    }

}
