package com.onbelay.core.mylocation.model;

import java.util.ArrayList;
import java.util.List;

import com.onbelay.core.testfixture.enums.GeoCode;
import com.onbelay.core.testfixture.model.MyLocation;
import com.onbelay.core.testfixture.shared.LocationDetail;

public class LocationFixture {

	public static MyLocation createLocation(String name, String description, GeoCode geoCode) {
		return new MyLocation(
				new LocationDetail()
						.withName(name)
						.withDescription(description)
						.withGeoCode(geoCode));
	}

	public static List<MyLocation> createLocations(String prefix, int total) {
		
		ArrayList<MyLocation> locations = new ArrayList<MyLocation>();
		
		for (int i = 0; i < total; i++) {
			String name = prefix + "_" + i;
			MyLocation location = new MyLocation(
						new LocationDetail()
								.withName(name)
								.withDescription(name));
			locations.add(location);					
			
		}
		return locations;
	}
	
	
}
