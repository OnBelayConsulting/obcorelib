package com.onbelay.core.errorhandlingimpl;

import com.onbelay.core.errorhandling.ErrorHandlingManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorHandlingConfig {

    @Bean
    public ErrorHandlingManager errorHandlingManager() {
        return new CoreErrorHandlingManager();
    }

}
