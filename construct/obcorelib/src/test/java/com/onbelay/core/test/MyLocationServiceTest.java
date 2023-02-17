package com.onbelay.core.test;

import com.onbelay.core.entity.snapshot.EntityListItemCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.persistence.TransactionalSpringTestCase;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.enums.CoreErrorCode;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.core.test.model.MyLocation;
import com.onbelay.core.test.repository.MyLocationRepository;
import com.onbelay.core.test.service.MyLocationService;
import com.onbelay.core.test.shared.LocationDetail;
import com.onbelay.core.test.snapshot.MyLocationSnapshot;
import com.onbelay.core.test.snapshot.MyLocationSnapshotCollection;

@ComponentScan("com.onbelay")
@RunWith(SpringRunner.class)
@TestPropertySource( locations="classpath:application-core-integrationtest.properties")
@SpringBootTest
public class MyLocationServiceTest extends TransactionalSpringTestCase {

	
	@Autowired
	private MyLocationRepository myLocationRepository;
	
	@Autowired
	private MyLocationService myLocationService;

	private EntityId locationId;

	@Override
	public void setUp() {

	}

	public void beforeRun() throws Throwable {
		super.beforeRun();
		MyLocation location = new MyLocation(
				new LocationDetail(
						"Kelowna",
						"Kelowna, B.C"));
		
		location.save();
		locationId = location.getEntityId();
		flush();
		clearCache();
		
	}
	
	
	@Test
	public void testFindMyLocation() {
		
		MyLocationSnapshot savedLocation = myLocationService.findByName("Kelowna");
		
		assertNotNull(savedLocation);
			
	}
	
	@Test
	public void testUpdateMyLocation() {
		
		MyLocationSnapshot savedLocation = myLocationService.findByName("Kelowna");
		
		savedLocation.setEntityState(EntityState.MODIFIED);
		savedLocation.getDetail().setDescription("changed");
		
		myLocationService.save(savedLocation);
		flush();
		
		MyLocation location = myLocationRepository.load(savedLocation.getEntityId());
		assertEquals("changed", location.getDetail().getDescription());
		assertNotNull(savedLocation);
	}
	
	@Test
	public void testFindMyLocationMissing() {
		
		try {
			myLocationService.findByName("NotHere");
			fail("Should have thrown exception");
		} catch (OBRuntimeException e) {
			assertEquals(CoreErrorCode.MISSING_MY_LOCATION.getCode(), e.getErrorCode());
		} catch (RuntimeException t) {
			fail("Should have thrown JSRuntimeException");
		}
	}

	@Test
	public void findLocationList() {
		LocationFixture.createLocations("add", 10);
		flush();
		clearCache();

		EntityListItemCollection collection = myLocationService.findList("WHERE", 0, 100);
		assertEquals(11, collection.getCount());
		assertEquals(11, collection.getSnapshots().size());
	}

	
	@Test
	public void testFindUsingDefinedQuery() {
		
		LocationFixture.createLocations("add", 10);
		flush();
		clearCache();

		
		MyLocationSnapshotCollection collection = myLocationService.find(
				"where name = 'Kelowna'",
				0, 
				10);
		
		assertEquals(1, collection.getCount());
		
	}
	
}
