package com.onbelay.core.testfixture.codes;

import com.onbelay.core.codes.model.AbstractCodeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GEO_CODE")
public class GeoCodeEntity extends AbstractCodeEntity {

    public static String codeFamily = "geoCode";

}
