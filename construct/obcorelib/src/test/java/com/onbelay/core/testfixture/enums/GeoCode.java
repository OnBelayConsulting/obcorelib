package com.onbelay.core.testfixture.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines Geo code for Location
 * mapped in database as GEO_CODE_LANG
 */
public enum GeoCode {

	EAST ("East"),
	CENTRAL ("Central"),
	NORTH ("North"),
	WEST ("West");

	private final String code;

	private static final Map<String, GeoCode> codeMap
			= new HashMap<>();

	static {
		for(GeoCode c : EnumSet.allOf(GeoCode.class))
			codeMap.put(c.code, c);
	}

	private GeoCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	/**
	 * Returns an enum constant by its code representation.
	 * If the code does not exist as a property value of one
	 * of the enum constants, null is returned.
	 */
	public static GeoCode lookUp(String code) {
		return codeMap.get(code);
	}


}
