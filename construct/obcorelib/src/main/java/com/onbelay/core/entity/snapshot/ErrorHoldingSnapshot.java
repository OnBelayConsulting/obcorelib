package com.onbelay.core.entity.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class ErrorHoldingSnapshot {
    public static final String SUCCESS = "0";
    private String errorCode = SUCCESS;
    private String errorMessage;
    private List<String> parameters = new ArrayList<>();

    public ErrorHoldingSnapshot() {
    }

    public ErrorHoldingSnapshot(String errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorHoldingSnapshot(String errorCode, List<String> parameters) {
        this.errorCode = errorCode;
        this.parameters = parameters;
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return errorCode.equals(SUCCESS);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonIgnore
    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

}
