package com.onbelay.core.test.snapshot;

import java.util.List;

import com.onbelay.core.entity.snapshot.AbstractJSONCollection;

public class MyLocationSnapshotCollection extends AbstractJSONCollection<MyLocationSnapshot>{
	
	public static final String ITEM_TYPE = "myLocation";
	
	public MyLocationSnapshotCollection() {
		super(ITEM_TYPE);
	}
	
	public MyLocationSnapshotCollection(List<MyLocationSnapshot> snapshots) {
		super(ITEM_TYPE, snapshots);
	}
	
	public MyLocationSnapshotCollection(List<MyLocationSnapshot> snapshots, int totalItems) {
		super(ITEM_TYPE, snapshots);
		setTotalItems(totalItems);
	}
	
	public MyLocationSnapshotCollection(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}
	
	public MyLocationSnapshotCollection(int total) {
		super(ITEM_TYPE);
		setTotalItems(total);
	}

}
