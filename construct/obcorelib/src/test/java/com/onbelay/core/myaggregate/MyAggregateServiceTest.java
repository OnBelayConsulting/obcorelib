package com.onbelay.core.myaggregate;

import com.onbelay.core.entity.enums.EntityState;
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

import static org.junit.jupiter.api.Assertions.*;

public class MyAggregateServiceTest extends CoreSpringTestCase {

    @Autowired
    private MyAggregateService myAggregateService;

    @Autowired
    private MyAggregateRepository myAggregateRepository;

    private MyLocation myLocation;

    @Override
    public void setUp() {
        super.setUp();
        myLocation = LocationFixture.createLocation("ddd", "ddd", GeoCode.CENTRAL);
        flush();
    }

    @Test
    public void createAggregate() {
        MyAggregateSnapshot snapshot = new MyAggregateSnapshot();
        snapshot.setLocationId(myLocation.generateEntityId());
        snapshot.getDetail().setStartDate(LocalDate.now());
        snapshot.getDetail().setName("Mine");
        snapshot.getDetail().setQuantity(BigDecimal.ONE);
        snapshot.getDetail().setTotal(BigDecimal.ONE);
        TransactionResult result = myAggregateService.save(snapshot);
        flush();
        clearCache();
        MyAggregateSnapshot created = myAggregateService.load(result.getEntityId());
        assertNotNull(created);
        MyAggregate myAggregate = myAggregateRepository.load(created.getEntityId());
        MyAggregateAudit audit = MyAggregateAudit.findRecentHistory(myAggregate);
        assertEquals("Mine", audit.getDetail().getName());
    }



    @Test
    public void updateAggregate() {
        MyAggregateSnapshot snapshot = new MyAggregateSnapshot();
        snapshot.setLocationId(myLocation.generateEntityId());
        snapshot.getDetail().setStartDate(LocalDate.now());
        snapshot.getDetail().setName("Mine");
        snapshot.getDetail().setQuantity(BigDecimal.ONE);
        snapshot.getDetail().setTotal(BigDecimal.ONE);
        TransactionResult result = myAggregateService.save(snapshot);
        flush();
        clearCache();
        MyAggregateSnapshot created = myAggregateService.load(result.getEntityId());
        assertNotNull(created);

        MyAggregateSnapshot update = new MyAggregateSnapshot();
        update.setEntityId(created.getEntityId());
        update.setEntityState(EntityState.MODIFIED);
        update.getDetail().setName("yours");
        result = myAggregateService.save(update);
        flush();
        clearCache();

        MyAggregate myAggregate = myAggregateRepository.load(created.getEntityId());
        assertEquals("yours", myAggregate.getDetail().getName());
        MyAggregateAudit audit = MyAggregateAudit.findRecentHistory(myAggregate);
        assertEquals("yours", audit.getDetail().getName());
    }


    @Test
    public void deleteAggregate() {
        MyAggregateSnapshot snapshot = new MyAggregateSnapshot();
        snapshot.setLocationId(myLocation.generateEntityId());
        snapshot.getDetail().setStartDate(LocalDate.now());
        snapshot.getDetail().setName("Mine");
        snapshot.getDetail().setQuantity(BigDecimal.ONE);
        snapshot.getDetail().setTotal(BigDecimal.ONE);
        TransactionResult result = myAggregateService.save(snapshot);
        flush();
        clearCache();
        MyAggregateSnapshot created = myAggregateService.load(result.getEntityId());
        assertNotNull(created);

        MyAggregateSnapshot letsDelete = new MyAggregateSnapshot();
        letsDelete.setEntityId(created.getEntityId());
        letsDelete.setEntityState(EntityState.DELETE);
        TransactionResult deleteResult = myAggregateService.save(letsDelete);
        flush();
        clearCache();
        MyAggregateSnapshot deleted = myAggregateService.load(result.getEntityId());
        assertNotNull(deleted);
        assertTrue(deleted.getEntityId().isDeleted());

        MyAggregate myAggregate = myAggregateRepository.load(created.getEntityId());
        MyAggregateAudit audit = MyAggregateAudit.findRecentHistory(myAggregate);
        assertTrue(audit.getIsDeleted().booleanValue());

    }


}
