package com.onbelay.testfixture.service;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.testfixture.snapshot.MyAggregateSnapshot;

public interface MyAggregateService {

    MyAggregateSnapshot load(EntityId entityId);

    TransactionResult save(MyAggregateSnapshot snapshot);

}
