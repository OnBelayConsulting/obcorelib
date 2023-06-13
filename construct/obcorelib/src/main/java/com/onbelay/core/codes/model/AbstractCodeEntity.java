package com.onbelay.core.codes.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract
class AbstractCodeEntity {

    private String code;
    private String label;
    private Integer displayNo;

    @Id
    @Column(name = "CODE_CD")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "CODE_LABEL")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(name = "DISPLAY_ORDER_NO")
    public Integer getDisplayNo() {
        return displayNo;
    }

    public void setDisplayNo(Integer displayNo) {
        this.displayNo = displayNo;
    }
}
