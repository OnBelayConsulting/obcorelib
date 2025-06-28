package com.onbelay.testfixture.service;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.EntityListItemCollection;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.testfixture.snapshot.MyLocationSnapshot;
import com.onbelay.testfixture.snapshot.MyLocationSnapshotCollection;

import java.util.List;

public interface MyLocationService {

	
	public MyLocationSnapshotCollection find(
			String queryText,
			int start,
			int limit);

	public EntityListItemCollection findList(
			String queryText,
			int start,
			int limit);

	public TransactionResult save(MyLocationSnapshot snapshot);
	
	public TransactionResult save(List<MyLocationSnapshot> snapshots);
	
	public MyLocationSnapshot load(EntityId entityId);
	
	public MyLocationSnapshot findByName(String name);

}
