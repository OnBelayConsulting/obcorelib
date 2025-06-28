package com.onbelay.testfixture.service;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.testfixture.snapshot.MyOrganizationSnapshot;
import com.onbelay.testfixture.snapshot.MyOrganizationSnapshotCollection;

public interface MyOrganizationService {

	
	public MyOrganizationSnapshotCollection find(
			String queryText,
			int start,
			int limit);

	public TransactionResult save(MyOrganizationSnapshot snapshot);

	public MyOrganizationSnapshot load(EntityId entityId);

}
