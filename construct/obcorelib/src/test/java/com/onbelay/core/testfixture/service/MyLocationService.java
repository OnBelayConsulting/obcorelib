package com.onbelay.core.testfixture.service;

import java.util.List;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.EntityListItemCollection;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.testfixture.snapshot.MyLocationSnapshot;
import com.onbelay.core.testfixture.snapshot.MyLocationSnapshotCollection;

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
