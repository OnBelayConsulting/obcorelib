package com.onbelay.testfixture.adapterimpl;

import com.onbelay.core.controller.BaseRestAdapterBean;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.testfixture.adapter.MyLocationRestAdapter;
import com.onbelay.testfixture.service.MyLocationService;
import com.onbelay.testfixture.snapshot.MyLocationSnapshot;
import com.onbelay.testfixture.snapshot.MyLocationSnapshotCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyLocationRestAdapterBean extends BaseRestAdapterBean implements MyLocationRestAdapter {

    @Autowired
    private MyLocationService myLocationService;

    public MyLocationSnapshotCollection find(
            String queryText,
            Integer start,
            Integer limit) {

        initializeSession();

        return myLocationService.find(
                queryText,
                start,
                limit);

    }

    @Override
    public TransactionResult save(MyLocationSnapshot snapshot) {
        initializeSession();
        return myLocationService.save(snapshot);
    }

    @Override
    public MyLocationSnapshot load(Integer id) {
        initializeSession();
        return myLocationService.load(new EntityId(id));
    }
}
