package com.onbelay.core.testfixture.codes;

import com.onbelay.core.codes.model.CodeManagerBean;
import com.onbelay.core.codes.repository.CodeRepository;

public class MyLocationCodeManagerBean extends CodeManagerBean {

    public MyLocationCodeManagerBean(CodeRepository codeRepository) {
        setCodeRepository(codeRepository);
        addCodeEntity(GeoCodeEntity.codeFamily, "GeoCodeEntity");

    }
}
