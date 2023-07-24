package com.onbelay.testfixture.codes;

import com.onbelay.core.codes.model.AbstractCodeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "GEO_CODE")
public class GeoCodeEntity extends AbstractCodeEntity {

    public static String codeFamily = "geoCode";

}
