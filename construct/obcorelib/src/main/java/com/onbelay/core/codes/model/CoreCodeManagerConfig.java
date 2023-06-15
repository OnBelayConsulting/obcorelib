package com.onbelay.core.codes.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CoreCodeManagerConfig {

    @Bean(value = "codeManager")
    public CodeManager codeManager() {
        return new CodeManagerBean();
    }

}
