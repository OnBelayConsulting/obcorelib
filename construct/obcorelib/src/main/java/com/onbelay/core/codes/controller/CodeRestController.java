package com.onbelay.core.codes.controller;

import com.onbelay.core.codes.adapter.CodesRestAdapter;
import com.onbelay.core.codes.snapshot.CodeLabelCollection;
import com.onbelay.core.controller.BaseRestController;
import com.onbelay.core.exception.OBRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/codes")
public class CodeRestController extends BaseRestController {

    @Autowired
    private CodesRestAdapter codesRestAdapter;

    @GetMapping(value = "/{codeFamily}")
    public ResponseEntity<CodeLabelCollection> fetchCodeLabels(
            @PathVariable String codeFamily,
            @RequestParam(value="filter", required = false) String filter) {

        CodeLabelCollection collection;

        try {
            collection = codesRestAdapter.findCodeLabels(
                    codeFamily,
                    filter);
        } catch (OBRuntimeException e) {
            collection = new CodeLabelCollection(e.getErrorCode(), e.getParms());
            collection.setErrorMessage(errorMessageService.getErrorMessage(e.getErrorCode()));
        }
        return (ResponseEntity<CodeLabelCollection>) processResponse(collection);
    }

}
