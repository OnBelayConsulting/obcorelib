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
import com.onbelay.testfixture.repository.MyLocationRepository;
import com.onbelay.testfixture.shared.MyAggregateDetail;
import com.onbelay.testfixture.snapshot.MyAggregateSnapshot;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "MY_AGGREGATE")
public class MyAggregate extends TemporalAbstractEntity {

	private Integer id;

	private MyAggregateDetail detail = new  MyAggregateDetail();

	private MyLocation location;

	protected MyAggregate() {
	}
	
	
	public static MyAggregate create(MyAggregateSnapshot snapshot) {
		MyAggregate aggregate = new MyAggregate();
		aggregate.createWith(snapshot);
		return aggregate;
	}

	public MyAggregate(MyAggregateSnapshot snapshot) {
		createWith(snapshot);
	}


	protected void createWith(MyAggregateSnapshot snapshot) {
		detail.copyFrom(snapshot.getDetail());
		if (detail.getStartDate() == null) {
			detail.setStartDate(LocalDate.now());
		}
		setRelationships(snapshot);
		save();
	}
	
	public void updateWith(MyAggregateSnapshot snapshot) {
		super.updateWith(snapshot);
		detail.copyFrom(snapshot.getDetail());
		setRelationships(snapshot);
		update();
	}

	private void setRelationships(MyAggregateSnapshot snapshot) {

		if (snapshot.getLocationId() != null) {
			this.location = getLocationRepository().load(snapshot.getLocationId());
		}

	}
	
	protected void validate() throws OBValidationException {
		detail.validate();
	}

	@Override
	@Transient
	public String getEntityName() {
		return "MyAggregate";
	}

	@Override
	public EntityId generateEntityId() {
		return new EntityId(
				getId(),
				detail.getName(),
				detail.getName(),
				getIsExpired());
	}

	@Id
    @Column(name="ENTITY_ID", insertable = false, updatable = false)
    @SequenceGenerator(name="MyAggregateGen", sequenceName="MY_AGGREGATE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "MyAggregateGen")
	public Integer getId() {
		return id;
	}

	public void setId(Integer myLocationId) {
		this.id = myLocationId;
	}

	@ManyToOne()
	@JoinColumn(name = "LOCATION_ID")
	public MyLocation getLocation() {
		return location;
	}

	public void setLocation(MyLocation parentLocation) {
		this.location = parentLocation;
	}

	@Embedded
	public MyAggregateDetail getDetail() {
		return detail;
	}

	public void setDetail(MyAggregateDetail detail) {
		this.detail = detail;
	}

	@Override
	protected AuditAbstractEntity createHistory() {
		return MyAggregateAudit.create(this);
	}

	@Override
	public AuditAbstractEntity fetchRecentHistory() {
		return MyAggregateAudit.findRecentHistory(this);
	}

	@Transient
	public MyLocationRepository getLocationRepository() {
		return (MyLocationRepository) ApplicationContextFactory.getBean(MyLocationRepository.BEAN_NAME);
	}

	@Override
	public void delete() {
		setIsExpired(true);
		update();
	}
}
