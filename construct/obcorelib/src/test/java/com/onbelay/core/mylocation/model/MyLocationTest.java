package com.onbelay.core.mylocation.model;

import java.util.List;

import com.onbelay.core.test.CoreSpringTestCase;
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
import com.onbelay.testfixture.model.MyLocation;
import com.onbelay.testfixture.repository.MyLocationRepository;
import com.onbelay.testfixture.shared.LocationDetail;

public class MyLocationTest extends CoreSpringTestCase {

	
	@Autowired
	private MyLocationRepository myLocationRepository;
	
	
	@Test
	public void testCreateMyLocation() {
		
		MyLocation location = new MyLocation(
				new LocationDetail()
						.withName("Kelowna")
						.withDescription("Kelowna, B.C"));
		
		EntityId id = location.generateEntityId();
		flush();
		clearCache();
		
		MyLocation savedLocation = myLocationRepository.load(id);
		assertNotNull(savedLocation);
		assertNotNull(savedLocation.getDetail().getLocationNo());
		
		
	}
	
	@Test
	public void testFindMyLocation() {
		
		MyLocation location = new MyLocation(
				new LocationDetail()
						.withName("Kelowna")
						.withDescription("Kelowna, B.C"));
		
		location.save();
		EntityId id = location.generateEntityId();
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
		
		List<Integer> ids = myLocationRepository.findMyLocationIds(query);
		assertEquals(10, ids.size());
		
		List<MyLocation> locations = myLocationRepository.fetchByIds(new QuerySelectedPage(ids));
		assertEquals(10, locations.size());
		
	}
	
}
