package com.onbelay.testfixture.adapter;

import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.testfixture.snapshot.MyLocationSnapshot;
import com.onbelay.testfixture.snapshot.MyLocationSnapshotCollection;

public interface MyLocationRestAdapter {

    public MyLocationSnapshotCollection find(
            String queryText,
            Integer start,
            Integer limit);

    TransactionResult save(MyLocationSnapshot snapshot);

    MyLocationSnapshot load(Integer id);
}
