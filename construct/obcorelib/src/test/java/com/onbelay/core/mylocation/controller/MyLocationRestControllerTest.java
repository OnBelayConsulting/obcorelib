package com.onbelay.core.mylocation.controller;

import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.test.CoreSpringTestCase;
import com.onbelay.testfixture.controller.MyLocationRestController;
import com.onbelay.testfixture.enums.GeoCode;
import com.onbelay.testfixture.model.MyLocation;
import com.onbelay.testfixture.snapshot.MyLocationSnapshot;
import com.onbelay.testfixture.snapshot.MyLocationSnapshotCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WithMockUser
public class MyLocationRestControllerTest extends CoreSpringTestCase {
    private static final Logger logger = LogManager.getLogger();

    private MyLocation myLocation;
    
    @Autowired
    private MyLocationRestController myLocationRestController;

    @Override
    public void setUp() {
        super.setUp();
        MyLocationSnapshot snapshot = new MyLocationSnapshot();
        snapshot.getDetail().setName("Kelowna");
        snapshot.getDetail().setDescription("desc");
        snapshot.getDetail().setGeoCode(GeoCode.CENTRAL);
        
        myLocation = MyLocation.create(snapshot);
    }


    @Test
    public void getCollection() throws Exception {

        MockMvc mvc = MockMvcBuilders.standaloneSetup(myLocationRestController)
                .defaultRequest(MockMvcRequestBuilders.get("/api/locations")
                        .accept(MediaType.APPLICATION_JSON))
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();

        ResultActions result = mvc.perform(get("/api/locations"));

        MvcResult mvcResult = result.andReturn();

        String jsonStringResponse = mvcResult.getResponse().getContentAsString();

        logger.error("Json: " + jsonStringResponse);


        MyLocationSnapshotCollection collection = objectMapper.readValue(jsonStringResponse, MyLocationSnapshotCollection.class);

        assertEquals(1, collection.getSnapshots().size());

        assertEquals(1, collection.getCount());

        assertEquals(1, collection.getTotalItems());

    }

    @Test
    public void postLocation() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(myLocationRestController)
                .defaultRequest(post("/locations/api")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();

        MyLocationSnapshot secondLocation = new MyLocationSnapshot();
        secondLocation.getDetail().setName("second");
        secondLocation.getDetail().setDescription("second desc");
        secondLocation.getDetail().setGeoCode(GeoCode.WEST);

        String jsonContent = objectMapper.writeValueAsString(secondLocation);

        ResultActions result = mvc.perform(post("/api/locations/").content(jsonContent));

        MvcResult mvcResult = result.andReturn();

        String jsonStringResponse = mvcResult.getResponse().getContentAsString();

        TransactionResult transactionResult = objectMapper.readValue(jsonStringResponse, TransactionResult.class);
        assertTrue(transactionResult.isSuccessful());
    }


    @Test
    public void getLocation() throws Exception {

        MockMvc mvc = MockMvcBuilders.standaloneSetup(myLocationRestController)
                .defaultRequest(MockMvcRequestBuilders.get("/api/locations")
                        .accept(MediaType.APPLICATION_JSON))
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();


        ResultActions result = mvc.perform(get("/api/locations/" + myLocation.getId()));

        MvcResult mvcResult = result.andReturn();

        String jsonStringResponse = mvcResult.getResponse().getContentAsString();

        logger.error("Json: " + jsonStringResponse);

        MyLocationSnapshot snapshot = objectMapper.readValue(jsonStringResponse, MyLocationSnapshot.class);
        assertEquals("Kelowna", snapshot.getDetail().getName());

    }

}
