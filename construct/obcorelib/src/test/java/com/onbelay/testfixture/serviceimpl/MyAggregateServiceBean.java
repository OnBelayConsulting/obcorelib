package com.onbelay.testfixture.serviceimpl;

import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.testfixture.model.MyAggregate;
import com.onbelay.testfixture.repository.MyAggregateRepository;
import com.onbelay.testfixture.service.MyAggregateService;
import com.onbelay.testfixture.snapshot.MyAggregateSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyAggregateServiceBean implements MyAggregateService {

    @Autowired
    private MyAggregateRepository myAggregateRepository;


    @Override
    public TransactionResult save(MyAggregateSnapshot snapshot) {
        if (snapshot.getEntityState() == EntityState.NEW) {
            MyAggregate created = MyAggregate.create(snapshot);
            return new TransactionResult(created.getId());
        } else if (snapshot.getEntityState() == EntityState.MODIFIED) {
            MyAggregate found = myAggregateRepository.load(snapshot.getEntityId());
            found.updateWith(snapshot);
            return new TransactionResult(found.getId());
        } else if (snapshot.getEntityState() == EntityState.DELETE) {
            MyAggregate found = myAggregateRepository.load(snapshot.getEntityId());
            found.delete();
            return new TransactionResult();
        } else {
            return new TransactionResult();
        }
    }

    @Override
    public MyAggregateSnapshot load(EntityId entityId) {

        MyAggregate aggregate = myAggregateRepository.load(entityId);
        MyAggregateSnapshot snapshot = new MyAggregateSnapshot();
        snapshot.setEntityId(aggregate.generateEntityId());
        snapshot.setEntityState(EntityState.UNMODIFIED);
        snapshot.getDetail().copyFrom(aggregate.getDetail());
        snapshot.setLocationId(aggregate.getLocation().generateEntityId());

        return snapshot;
    }
}
