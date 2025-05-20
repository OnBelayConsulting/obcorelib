package com.onbelay.core.mylocation.serviceimpl;

import com.onbelay.core.entity.snapshot.EntityListItemCollection;
import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.mylocation.model.LocationFixture;
import com.onbelay.core.test.CoreSpringTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.testfixture.model.MyLocation;
import com.onbelay.testfixture.repository.MyLocationRepository;
import com.onbelay.testfixture.service.MyLocationService;
import com.onbelay.testfixture.shared.LocationDetail;
import com.onbelay.testfixture.snapshot.MyLocationSnapshot;
import com.onbelay.testfixture.snapshot.MyLocationSnapshotCollection;

import static org.junit.jupiter.api.Assertions.*;

public class MyLocationServiceTest extends CoreSpringTestCase {

	
	@Autowired
	private MyLocationRepository myLocationRepository;
	
	@Autowired
	private MyLocationService myLocationService;

	private EntityId locationId;

	private MyLocation parentLocation;

	@Override
	public void setUp() {
		super.setUp();

		parentLocation = new MyLocation(
				new LocationDetail()
						.withName("British Columbia")
						.withDescription("BC Province"));

		MyLocation location = new MyLocation(
				new LocationDetail()
						.withName("Kelowna")
						.withDescription("Kelowna, B.C"));

		locationId = location.generateEntityId();
		flush();
		clearCache();
	}

	@Test
	public void findMyLocation() {
		
		MyLocationSnapshot savedLocation = myLocationService.findByName("Kelowna");
		
		assertNotNull(savedLocation);
			
	}


	@Test
	public void loadMyLocation() {

		MyLocationSnapshot savedLocation = myLocationService.load(locationId);

		assertNotNull(savedLocation);

	}

	@Test
	public void updateMyLocation() {
		
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
	public void addParentMyLocation() {

		MyLocationSnapshot savedLocation = myLocationService.findByName("Kelowna");

		savedLocation.setEntityState(EntityState.MODIFIED);
		savedLocation.setParentLocationId(parentLocation.generateEntityId());

		myLocationService.save(savedLocation);
		flush();

		MyLocation location = myLocationRepository.load(savedLocation.getEntityId());
		assertEquals("British Columbia", location.getParentLocation().getDetail().getName());
		assertNotNull(savedLocation);
	}


	@Test
	public void removeParentMyLocation() {

		MyLocationSnapshot savedLocation = myLocationService.findByName("Kelowna");

		savedLocation.setEntityState(EntityState.MODIFIED);
		savedLocation.setParentLocationId(parentLocation.generateEntityId());

		myLocationService.save(savedLocation);
		flush();

		MyLocation location = myLocationRepository.load(savedLocation.getEntityId());
		assertEquals("British Columbia", location.getParentLocation().getDetail().getName());
		assertNotNull(savedLocation);
		MyLocationSnapshot updateSnapshot = new MyLocationSnapshot();
		updateSnapshot.setEntityState(EntityState.MODIFIED);
		updateSnapshot.setEntityId(location.generateEntityId());
		updateSnapshot.setParentLocationId(EntityId.makeNullEntityId());
		myLocationService.save(updateSnapshot);
		flush();
		clearCache();

		location = myLocationRepository.load(location.generateEntityId());
		assertNull(location.getParentLocation());


	}


	@Test
	public void addParentMyLocationWithName() {

		MyLocationSnapshot savedLocation = myLocationService.findByName("Kelowna");

		savedLocation.setEntityState(EntityState.MODIFIED);
		savedLocation.setParentLocationId((new EntityId("British Columbia")));

		myLocationService.save(savedLocation);
		flush();

		MyLocation location = myLocationRepository.load(locationId);
		assertEquals("British Columbia", location.getParentLocation().getDetail().getName());
		assertNotNull(savedLocation);
	}



	@Test
	public void findMyLocationMissing() {
		
		try {
			myLocationService.findByName("NotHere");
			fail("Should have thrown exception");
		} catch (OBRuntimeException e) {
			assertEquals(CoreTransactionErrorCode.INVALID_ENTITY_ID.getCode(), e.getErrorCode());
		} catch (RuntimeException t) {
			fail("Should have thrown OBRuntimeException");
		}
	}

	@Test
	public void findLocationList() {
		LocationFixture.createLocations("add", 10);
		flush();
		clearCache();

		EntityListItemCollection collection = myLocationService.findList("WHERE", 0, 100);
		assertEquals(12, collection.getCount());
		assertEquals(12, collection.getSnapshots().size());
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
