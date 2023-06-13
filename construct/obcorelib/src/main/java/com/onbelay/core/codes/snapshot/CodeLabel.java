package com.onbelay.core.codes.snapshot;

public class CodeLabel {

    private String code;
    private String label;

    public CodeLabel(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public CodeLabel() {
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
