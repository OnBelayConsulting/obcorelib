package com.onbelay.core.codes.model;

import com.onbelay.core.codes.snapshot.CodeLabel;

import java.util.List;

public interface CodeManager {

    List<CodeLabel> findCodeLabels(String codeFamily);

    List<CodeLabel> findCodeLabels(
            String codeFamily,
            String filter);

    CodeLabel getCodeLabel(
            String codeFamily,
            String code);

}
