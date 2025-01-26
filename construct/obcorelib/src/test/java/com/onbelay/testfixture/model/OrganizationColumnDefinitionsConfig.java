package com.onbelay.testfixture.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrganizationColumnDefinitionsConfig {

    @Bean
    public MyOrganizationColumnDefinitions myOrganizationColumnDefinitions() {
        return new MyOrganizationColumnDefinitions();
    }

    @Bean
    public MyCustomerColumnDefinitions myCustomerColumnDefinitions() {
        return new MyCustomerColumnDefinitions();
    }

    @Bean
    public OrganizationColumnDefinitionsMap organizationColumnDefinitionsMap() {
        OrganizationColumnDefinitionsMap map = new OrganizationColumnDefinitionsMap();
        map.addColumnDefinitions("MyOrganization", myOrganizationColumnDefinitions());
        map.addColumnDefinitions("MyCustomer", myCustomerColumnDefinitions());
        return map;
    }
}
