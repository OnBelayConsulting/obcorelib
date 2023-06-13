package com.onbelay.core.testfixture.codes;

import com.onbelay.core.codes.model.CodeManager;
import com.onbelay.core.codes.model.CodeManagerBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MyLocationCodeManagerConfig {

    @Bean
    @Primary
    public CodeManager codeManager() {
        CodeManagerBean codeManagerBean = new CodeManagerBean();
        codeManagerBean.addCodeEntity(GeoCodeEntity.codeFamily, "GeoCodeEntity");
        return codeManagerBean;
    }

}
