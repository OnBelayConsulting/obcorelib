package com.onbelay.core.codes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.type.YesNoConverter;

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
    @Convert(converter = YesNoConverter.class)
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
