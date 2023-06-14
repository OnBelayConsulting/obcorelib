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
    private Boolean isActive;

    @Id
    @Column(name = "CODE_ID")
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
    public Integer getDisplayOrderNo() {
        return displayNo;
    }

    public void setDisplayOrderNo(Integer displayNo) {
        this.displayNo = displayNo;
    }

    @Column(name = "IS_ACTIVE")
    @org.hibernate.annotations.Type(type="yes_no")
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
