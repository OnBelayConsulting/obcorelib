package com.onbelay.core.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onbelay.core.entity.persistence.TransactionalSpringTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;

@ComponentScan(basePackages = {"com.onbelay.core.*", "com.onbelay.testfixture.*"})
@EntityScan(basePackages = {"com.onbelay.*"})
@TestPropertySource( locations="classpath:application-core-integrationtest.properties")
@SpringBootTest
public class CoreSpringTestCase extends TransactionalSpringTestCase {
    @Autowired
    protected ObjectMapper objectMapper;

    protected 	MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Override
    public void setUp() {
        super.setUp();
        mappingJackson2HttpMessageConverter = new  MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
    }
}
