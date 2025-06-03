package com.onbelay.core.myorganization;

import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.test.CoreSpringTestCase;
import com.onbelay.testfixture.model.MyCustomer;
import com.onbelay.testfixture.model.MyOrganization;
import com.onbelay.testfixture.repository.MyOrganizationRepository;
import com.onbelay.testfixture.service.MyOrganizationService;
import com.onbelay.testfixture.snapshot.MyCustomerSnapshot;
import com.onbelay.testfixture.snapshot.MyOrganizationSnapshot;
import com.onbelay.testfixture.snapshot.MyOrganizationSnapshotCollection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyOrganizationServiceTest extends CoreSpringTestCase {

    @Autowired
    private MyOrganizationService myOrganizationService;

    @Autowired
    private MyOrganizationRepository organizationRepository;

    private MyOrganization organization;
    private MyCustomer customer;

    public void setUp() {
        super.setUp();
        organization = new MyOrganization();
        MyOrganizationSnapshot snapshot = new MyOrganizationSnapshot();
        snapshot.setDescription("ddd");
        snapshot.setName("myName");
        organization.createWith(snapshot);

        customer = new MyCustomer();
        MyCustomerSnapshot customerSnapshot = new MyCustomerSnapshot();
        customerSnapshot.setDescription("ddd");
        customerSnapshot.setName("Rocky");
        customerSnapshot.setType("Retail");
        customer.createWith(customerSnapshot);

        flush();

    }

    @Test
    public void findMyOrganization() {
        MyOrganizationSnapshotCollection collection = myOrganizationService.find(
                "WHERE name = '" + organization.getName() + "'",
                0,
                10);

        assertEquals(1, collection.getCount());

    }

    @Test
    public void findMyCustomer() {
        MyOrganizationSnapshotCollection collection = myOrganizationService.find(
                "FROM MyCustomer",
                0,
                10);

        assertEquals(1, collection.getCount());

    }


    @Test
    public void findMyCustomerWithOrderBy() {
        MyOrganizationSnapshotCollection collection = myOrganizationService.find(
                "FROM MyCustomer ORDER BY type",
                0,
                10);

        assertEquals(1, collection.getCount());

    }


    @Test
    public void findMyCustomerWithType() {
        MyOrganizationSnapshotCollection collection = myOrganizationService.find(
                "FROM MyCustomer WHERE type = Retail",
                0,
                10);

        assertEquals(1, collection.getCount());

    }


    @Test
    public void findMyCustomerWithTypeOrderBy() {
        MyOrganizationSnapshotCollection collection = myOrganizationService.find(
                "FROM MyCustomer WHERE type = Retail ORDER BY name ASC",
                0,
                10);

        assertEquals(1, collection.getCount());

    }


    @Test
    public void findMyCustomerWithTypeAndName() {
        MyOrganizationSnapshotCollection collection = myOrganizationService.find(
                "FROM MyCustomer WHERE type = Retail AND name = '" + customer.getName() + "'",
                0,
                10);

        assertEquals(1, collection.getCount());

    }




}
