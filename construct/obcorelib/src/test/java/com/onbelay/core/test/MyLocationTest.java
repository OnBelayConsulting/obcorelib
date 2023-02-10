package com.onbelay.core.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.onbelay.core.entity.persistence.TransactionalSpringTestCase;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.query.enums.ExpressionOperator;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.DefinedWhereExpression;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.core.test.model.MyLocation;
import com.onbelay.core.test.repository.MyLocationRepository;
import com.onbelay.core.test.shared.LocationDetail;

@ComponentScan("com.onbelay")
@RunWith(SpringRunner.class)
@TestPropertySource( locations="classpath:application-core-integrationtest.properties")
@SpringBootTest
public class MyLocationTest extends TransactionalSpringTestCase {

	
	@Autowired
	private MyLocationRepository myLocationRepository;
	
	
	@Test
	public void testCreateMyLocation() {
		
		MyLocation location = new MyLocation(
				new LocationDetail(
						"Kelowna",
						"Kelowna, B.C"));
		
		location.save();
		EntityId id = location.getEntityId();
		flush();
		clearCache();
		
		MyLocation savedLocation = myLocationRepository.load(id);
		assertNotNull(savedLocation);
		
		
		
	}
	
	@Test
	public void testFindMyLocation() {
		
		MyLocation location = new MyLocation(
				new LocationDetail(
						"Kelowna",
						"Kelowna, B.C"));
		
		location.save();
		EntityId id = location.getEntityId();
		flush();
		clearCache();
		
		MyLocation savedLocation = myLocationRepository.findByName("Kelowna");
		assertNotNull(savedLocation);
		
	}
	
	@Test
	public void testFindUsingDefinedQuery() {
		
		LocationFixture.createLocations("add", 10);
		flush();
		clearCache();

		DefinedQuery query = new DefinedQuery("MyLocation");
		query.getWhereClause()
			.addExpression(
					new DefinedWhereExpression(
							"name", 
							ExpressionOperator.LIKE, 
							"add%"));
		
		List<Long> ids = myLocationRepository.findMyLocationIds(query);
		assertEquals(10, ids.size());
		
		List<MyLocation> locations = myLocationRepository.fetchByIds(new QuerySelectedPage(ids));
		assertEquals(10, locations.size());
		
	}
	
}
