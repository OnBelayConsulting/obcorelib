package com.onbelay.core.codes.adapterimpl;

import com.onbelay.core.codes.adapter.CodesRestAdapter;
import com.onbelay.core.codes.model.CodeManager;
import com.onbelay.core.codes.snapshot.CodeLabel;
import com.onbelay.core.codes.snapshot.CodeLabelCollection;
import com.onbelay.core.controller.BaseRestAdapterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodesRestAdapterBean extends BaseRestAdapterBean implements CodesRestAdapter {

    @Autowired
    private CodeManager codeManager;


    @Override
    public CodeLabelCollection findCodeLabels(
            String codeFamily,
            String filter) {

        List<CodeLabel> codeLabels = codeManager.findCodeLabels(codeFamily);
        return new CodeLabelCollection(
                0,
                codeLabels.size(),
                codeLabels.size(),
                codeLabels);
    }
}
