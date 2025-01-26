package com.onbelay.testfixture.snapshot;

import com.onbelay.core.entity.snapshot.AbstractSnapshotCollection;

import java.util.List;

public class MyOrganizationSnapshotCollection extends AbstractSnapshotCollection<MyOrganizationSnapshot> {
	
	public static final String ITEM_TYPE = "myOrganization";
	
	public MyOrganizationSnapshotCollection() {
		super(ITEM_TYPE);
	}

	public MyOrganizationSnapshotCollection(
			int start,
			int limit,
			int totalItems,
			List<MyOrganizationSnapshot> snapshots
			) {

		super(
				ITEM_TYPE,
				start,
				limit,
				totalItems,
				snapshots);
	}

	public MyOrganizationSnapshotCollection(
			int start,
			int limit,
			int totalItems) {

		super(
				ITEM_TYPE,
				start,
				limit,
				totalItems);
	}

	public MyOrganizationSnapshotCollection(String errorCode) {
		super(ITEM_TYPE, errorCode);
	}

	public MyOrganizationSnapshotCollection(String errorCode, List<String> parameters) {
		super(ITEM_TYPE, errorCode, parameters);
	}

}
