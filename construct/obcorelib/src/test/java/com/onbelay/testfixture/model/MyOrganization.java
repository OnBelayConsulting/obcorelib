package com.onbelay.testfixture.model;

import com.onbelay.core.entity.model.AbstractEntity;
import com.onbelay.core.exception.OBValidationException;
import com.onbelay.testfixture.snapshot.MyCustomerSnapshot;
import com.onbelay.testfixture.snapshot.MyOrganizationSnapshot;
import jakarta.persistence.*;

@Entity
@Table(name = "MY_ORGANIZATION")
@Inheritance(strategy = InheritanceType.JOINED)
public class MyOrganization extends AbstractEntity {

    private Integer id;

    private String name;
    private String description;

    @Override
    @Id
    @Column(name="ENTITY_ID", insertable =  false, updatable = false)
    @SequenceGenerator(name="myOrganizationSeq", sequenceName="MY_ORGANIZATION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "myOrganizationSeq")
    public Integer getId() {
        return id;
    }

    private void setId(Integer dealId) {
        this.id = dealId;
    }

    @Column(name = "ORGANIZATION_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "ORGANIZATION_DESC")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void createWith(MyOrganizationSnapshot snapshot) {
        this.name = snapshot.getName();
        this.description = snapshot.getDescription();
        save();

    }

    public void updateWith(MyOrganizationSnapshot snapshot) {
        this.name = snapshot.getName();
        this.description = snapshot.getDescription();
        update();
    }


    @Override
    protected void validate() throws OBValidationException {

    }
}
