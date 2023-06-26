package com.onbelay.testfixture.codes;

import com.onbelay.core.codes.model.CodeManager;
import com.onbelay.core.codes.model.CodeManagerBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Note that bean overriding is accomplished through the list of directories to search in the
 * component scan. Last directory overrides the first.
 */
@Configuration
public class LocationCodeManagerConfig {

    @Primary
    @Bean(value = "codeManager")
    public CodeManager codeManager() {
        CodeManagerBean codeManager = new CodeManagerBean();
        codeManager.addCodeEntity(GeoCodeEntity.codeFamily, "GeoCodeEntity");
        return codeManager;
    }

}
