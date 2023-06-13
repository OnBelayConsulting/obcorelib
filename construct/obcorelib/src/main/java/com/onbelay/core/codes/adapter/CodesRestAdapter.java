package com.onbelay.core.codes.adapter;

import com.onbelay.core.codes.snapshot.CodeLabelCollection;

public interface CodesRestAdapter {

    CodeLabelCollection findCodeLabels(
            String codeFamily,
            String filter);
}
