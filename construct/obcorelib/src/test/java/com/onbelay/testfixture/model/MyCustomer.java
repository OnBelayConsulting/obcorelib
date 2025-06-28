package com.onbelay.testfixture.model;

import com.onbelay.core.exception.OBValidationException;
import com.onbelay.testfixture.snapshot.MyCustomerSnapshot;
import com.onbelay.testfixture.snapshot.MyOrganizationSnapshot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "MY_CUSTOMER")
public class MyCustomer extends MyOrganization {

    private Integer id;

    private String organizationType;

    @Column(name = "ORGANIZATION_TYPE")
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String description) {
        this.organizationType = description;
    }

    @Override
    protected void validate() throws OBValidationException {

    }


    public void createWith(MyOrganizationSnapshot snapshot) {
        MyCustomerSnapshot customerSnapshot = (MyCustomerSnapshot) snapshot;
        setOrganizationType(customerSnapshot.getType());
        super.createWith(snapshot);
    }

    public void updateWith(MyOrganizationSnapshot snapshot) {
        MyCustomerSnapshot customerSnapshot = (MyCustomerSnapshot) snapshot;
        setOrganizationType(customerSnapshot.getType());
        super.updateWith(snapshot);
    }


}
