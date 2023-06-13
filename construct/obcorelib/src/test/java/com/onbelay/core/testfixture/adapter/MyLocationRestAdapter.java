package com.onbelay.core.testfixture.adapter;

import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.testfixture.snapshot.MyLocationSnapshot;
import com.onbelay.core.testfixture.snapshot.MyLocationSnapshotCollection;

public interface MyLocationRestAdapter {

    public MyLocationSnapshotCollection find(
            String queryText,
            Integer start,
            Integer limit);

    TransactionResult save(MyLocationSnapshot snapshot);

    MyLocationSnapshot load(Integer id);
}
