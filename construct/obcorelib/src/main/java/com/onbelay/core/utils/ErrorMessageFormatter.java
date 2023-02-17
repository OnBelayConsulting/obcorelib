package com.onbelay.core.utils;

import org.apache.commons.text.StringSubstitutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorMessageFormatter {
    private String errorMessage;
    private List<String> parameters = new ArrayList<>();

    public ErrorMessageFormatter(String errorMessage, List<String> parameters) {
        this.errorMessage = errorMessage;
        this.parameters = parameters;
    }

    public String getFormattedMessage() {
        Map<String, String> valuesMap = new HashMap<>();

        for (int i=0; i<parameters.size(); i++) {
            String key = "{p" + i + "}";
            valuesMap.put(key, parameters.get(i));
        }

        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        return sub.replace(errorMessage);
    }


}
