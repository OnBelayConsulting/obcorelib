package com.onbelay.core.codes.repository;

import com.onbelay.core.codes.snapshot.CodeLabel;

import java.util.List;

public interface CodeRepository {

    List<CodeLabel> findCodes(String codeEntityName, String filter);

    CodeLabel findCode(String codeEntityName, String code);

}
