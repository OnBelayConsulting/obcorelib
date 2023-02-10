package com.onbelay.core.test;

import java.util.ArrayList;
import java.util.List;

import com.onbelay.core.test.model.MyLocation;
import com.onbelay.core.test.shared.LocationDetail;

public class LocationFixture {

	public static List<MyLocation> createLocations(String prefix, int total) {
		
		ArrayList<MyLocation> locations = new ArrayList<MyLocation>();
		
		for (int i = 0; i < total; i++) {
			String name = prefix + "_" + i;
			MyLocation location = new MyLocation(
						new LocationDetail(
								name,
								name));
			locations.add(location);					
			
		}
		return locations;
	}
	
	
}
