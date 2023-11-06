package com.onbelay.core.geocode;

import com.onbelay.core.codes.model.CodeManager;
import com.onbelay.core.codes.snapshot.CodeLabel;
import com.onbelay.core.test.CoreSpringTestCase;
import com.onbelay.testfixture.codes.GeoCodeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeoCodeTest extends CoreSpringTestCase {

    @Autowired
    private CodeManager codeManager;

    @Test
    public void testCodes() {
        List<CodeLabel> codeLabels = codeManager.findCodeLabels(GeoCodeEntity.codeFamily);
        assertEquals(4, codeLabels.size());
    }

}
