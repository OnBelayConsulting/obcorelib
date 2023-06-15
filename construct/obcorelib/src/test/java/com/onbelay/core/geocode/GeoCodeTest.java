package com.onbelay.core.geocode;

import com.onbelay.core.codes.model.CodeManager;
import com.onbelay.core.codes.repository.CodeRepository;
import com.onbelay.core.codes.snapshot.CodeLabel;
import com.onbelay.core.test.CoreSpringTestCase;
import com.onbelay.core.testfixture.codes.GeoCodeEntity;
import com.onbelay.core.testfixture.codes.MyLocationCodeManagerBean;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GeoCodeTest extends CoreSpringTestCase {

    @Autowired
    private CodeRepository codeRepository;

    private CodeManager codeManager;

    @Test
    public void testCodes() {
        codeManager = new MyLocationCodeManagerBean(codeRepository);
        List<CodeLabel> codeLabels = codeManager.findCodeLabels(GeoCodeEntity.codeFamily);
        assertEquals(4, codeLabels.size());
    }

}
