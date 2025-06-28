package com.onbelay.testfixture.snapshot;

import com.onbelay.core.entity.snapshot.AbstractSnapshotCollection;

import java.util.List;

public class MyLocationSnapshotCollection extends AbstractSnapshotCollection<MyLocationSnapshot> {
	
	public static final String ITEM_TYPE = "myLocation";
	
	public MyLocationSnapshotCollection() {
		super(ITEM_TYPE);
	}

	public MyLocationSnapshotCollection(
			int start,
			int limit,
			int totalItems,
			List<MyLocationSnapshot> snapshots
			) {

		super(
				ITEM_TYPE,
				start,
				limit,
				totalItems,
				snapshots);
	}

	public MyLocationSnapshotCollection(
			int start,
			int limit,
			int totalItems) {

		super(
				ITEM_TYPE,
				start,
				limit,
				totalItems);
	}

	public MyLocationSnapshotCollection(String errorCode) {
		super(ITEM_TYPE, errorCode);
	}

	public MyLocationSnapshotCollection(String errorCode, List<String> parameters) {
		super(ITEM_TYPE, errorCode, parameters);
	}

}
