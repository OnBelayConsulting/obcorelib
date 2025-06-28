/*
 Copyright 2019, OnBelay Consulting Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.  
 */
package com.onbelay.testfixture.model;

import com.onbelay.core.entity.component.ApplicationContextFactory;
import com.onbelay.core.entity.model.AuditAbstractEntity;
import com.onbelay.core.entity.model.TemporalAbstractEntity;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.exception.OBValidationException;
import com.onbelay.testfixture.shared.LocationDetail;
import com.onbelay.testfixture.snapshot.MyLocationSnapshot;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "MY_LOCATION")
@NamedQueries({
    @NamedQuery(
       name = MyLocationRepositoryBean.FIND_MY_LOCATION_BY_NAME,
       query = "SELECT myLocation FROM MyLocation myLocation " +
       	     "   WHERE myLocation.detail.name = :name ")
    
})
public class MyLocation extends TemporalAbstractEntity {

	private Integer id;

	private LocationDetail detail = new  LocationDetail();

	private MyLocation parentLocation;

	protected MyLocation() {
	}
	
	
	public static MyLocation create(MyLocationSnapshot snapshot) {
		MyLocation location = new MyLocation();
		location.createWith(snapshot);
		return location;
	}
	
	public MyLocation(LocationDetail detailIn) {
		MyLocationSnapshot snapshot = new MyLocationSnapshot();
		snapshot.getDetail().copyFrom(detailIn);
		this.createWith(snapshot);
	}

	public MyLocation(MyLocationSnapshot snapshot) {
		createWith(snapshot);
	}


	protected void createWith(MyLocationSnapshot snapshot) {
		detail.copyFrom(snapshot.getDetail());
		if (detail.getEffectiveDate() == null) {
			detail.setEffectiveDate(LocalDate.now());
		}
		if (detail.getGeneratedDateTime() == null) {
			LocalDateTime now = LocalDateTime.now();
			now = now.truncatedTo(ChronoUnit.SECONDS);
			detail.setGeneratedDateTime(now.plusHours(48));
		}
		setRelationships(snapshot);
		save();
	}
	
	public void updateWith(MyLocationSnapshot snapshot) {
		detail.copyFrom(snapshot.getDetail());
		setRelationships(snapshot);
		update();
	}

	@Override
	public void preSave() {
		if (detail.getLocationNo() == null) {
			detail.setLocationNo(
					getLocationRepository().nextLocationNo());
		}
	}

	private void setRelationships(MyLocationSnapshot snapshot) {

		if (snapshot.getParentLocationId() != null) {
			this.parentLocation = getLocationRepository().load(snapshot.getParentLocationId());
		}

	}
	
	protected void validate() throws OBValidationException {
		detail.validate();
	}

	@Override
	@Transient
	public String getEntityName() {
		return "MyLocation";
	}

	@Override
	public EntityId generateEntityId() {
		return new EntityId(
				getId(),
				detail.getName(),
				detail.getDescription(),
				getIsExpired());
	}

	@Id
    @Column(name="ENTITY_ID", insertable = false, updatable = false)
    @SequenceGenerator(name="MyLocationGen", sequenceName="MY_LOCATION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "MyLocationGen")
	public Integer getId() {
		return id;
	}

	public void setId(Integer myLocationId) {
		this.id = myLocationId;
	}

	@ManyToOne()
	@JoinColumn(name = "PARENT_LOCATION_ID")
	public MyLocation getParentLocation() {
		return parentLocation;
	}

	public void setParentLocation(MyLocation parentLocation) {
		this.parentLocation = parentLocation;
	}

	@Embedded
	public LocationDetail getDetail() {
		return detail;
	}

	public void setDetail(LocationDetail detail) {
		this.detail = detail;
	}

	@Override
	protected AuditAbstractEntity createHistory() {
		return MyLocationAudit.create(this);
	}

	@Override
	public AuditAbstractEntity fetchRecentHistory() {
		return MyLocationAudit.findRecentHistory(this);
	}

	@Transient
	public MyLocationRepositoryBean getLocationRepository() {
		return (MyLocationRepositoryBean) ApplicationContextFactory.getBean(MyLocationRepositoryBean.BEAN_NAME);
	}

}
