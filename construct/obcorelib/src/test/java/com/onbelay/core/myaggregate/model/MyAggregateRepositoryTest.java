package com.onbelay.core.myaggregate.model;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.mylocation.model.LocationFixture;
import com.onbelay.core.test.CoreSpringTestCase;
import com.onbelay.testfixture.enums.GeoCode;
import com.onbelay.testfixture.model.MyAggregate;
import com.onbelay.testfixture.model.MyAggregateAudit;
import com.onbelay.testfixture.model.MyLocation;
import com.onbelay.testfixture.repository.MyAggregateRepository;
import com.onbelay.testfixture.service.MyAggregateService;
import com.onbelay.testfixture.snapshot.MyAggregateSnapshot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MyAggregateRepositoryTest extends CoreSpringTestCase {

    @Autowired
    private MyAggregateService myAggregateService;

    @Autowired
    private MyAggregateRepository myAggregateRepository;

    private MyLocation myLocation;

    private EntityId myAggregateId;

    @Override
    public void setUp() {
        super.setUp();
        myLocation = LocationFixture.createLocation("ddd", "ddd", GeoCode.CENTRAL);
        flush();
        MyAggregateSnapshot snapshot = new MyAggregateSnapshot();
        snapshot.setLocationId(myLocation.generateEntityId());
        snapshot.getDetail().setStartDate(LocalDate.now());
        snapshot.getDetail().setName("Mine");
        snapshot.getDetail().setQuantity(BigDecimal.ONE);
        snapshot.getDetail().setTotal(BigDecimal.ONE);
        TransactionResult result = myAggregateService.save(snapshot);
        flush();
        clearCache();
        myAggregateId = result.getEntityId();
    }

    @Test
    public void updateAggregate() {
        boolean result = myAggregateRepository.updateName("Anna", "Mine");
        flush();

        MyAggregateSnapshot snapshot = myAggregateService.load(myAggregateId);
        assertEquals("Anna", snapshot.getDetail().getName());
    }


}
