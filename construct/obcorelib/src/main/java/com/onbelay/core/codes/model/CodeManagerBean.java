package com.onbelay.core.codes.model;

import com.onbelay.core.codes.repository.CodeRepository;
import com.onbelay.core.codes.snapshot.CodeLabel;
import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.exception.OBRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeManagerBean implements CodeManager {

    private Map<String, String> codeEntityHashMap = new HashMap<>();

    @Autowired
    private CodeRepository codeRepository;

    @Override
    public List<CodeLabel> findCodeLabels(String codeFamily) {
        String codeEntityName = codeEntityHashMap.get(codeFamily);
        if (codeEntityName == null)
            throw new OBRuntimeException(CoreTransactionErrorCode.INVALID_CODE_FAMILY.getCode());

        return codeRepository.findCodes(codeEntityName, null);
    }

    @Override
    public List<CodeLabel> findCodeLabels(String codeFamily, String filter) {
        String codeEntityName = codeEntityHashMap.get(codeFamily);
        if (codeEntityName == null)
            throw new OBRuntimeException(CoreTransactionErrorCode.INVALID_CODE_FAMILY.getCode());

        return codeRepository.findCodes(codeEntityName, filter);
    }

    @Override
    public CodeLabel getCodeLabel(String codeFamily, String code) {
        String codeEntityName = codeEntityHashMap.get(codeFamily);
        if (codeEntityName == null)
            throw new OBRuntimeException(CoreTransactionErrorCode.INVALID_CODE_FAMILY.getCode());
        return codeRepository.findCode(codeEntityName, code);
    }

    public void addCodeEntity(String codeFamily, String codeEntityName) {
        codeEntityHashMap.put(codeFamily, codeEntityName);
    }
}
