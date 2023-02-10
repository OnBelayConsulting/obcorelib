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
package com.onbelay.core.test.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.onbelay.core.entity.component.ApplicationContextFactory;
import com.onbelay.core.entity.model.AuditAbstractEntity;
import com.onbelay.core.entity.model.TemporalAbstractEntity;
import com.onbelay.core.entity.repository.BaseRepository;
import com.onbelay.core.entity.snapshot.EntitySlot;
import com.onbelay.core.exception.JSValidationException;
import com.onbelay.core.test.shared.LocationDetail;
import com.onbelay.core.test.snapshot.MyLocationSnapshot;

@Entity
@Table(name = "MY_LOCATION")
@NamedQueries({
    @NamedQuery(
       name = MyLocationRepositoryBean.FIND_MY_LOCATION_BY_NAME,
       query = "SELECT myLocation FROM MyLocation myLocation " +
       	     "   WHERE myLocation.detail.name = :name ")
    
})
public class MyLocation extends TemporalAbstractEntity {

	private Long id;

	private LocationDetail detail = new  LocationDetail();
	

	protected MyLocation() {
	}
	
	
	public static MyLocation create(MyLocationSnapshot snapshot) {
		MyLocation location = new MyLocation();
		location.createWith(snapshot);
		return location;
	}
	
	public MyLocation(LocationDetail detailIn) {
		this.detail.copyFrom(detailIn);
		save();
	}

	public MyLocation(MyLocationSnapshot snapshot) {
		createWith(snapshot);
	}


	protected void createWith(MyLocationSnapshot snapshot) {
		detail.copyFrom(snapshot.getDetail());
		save();
	}
	
	public void updateWith(MyLocationSnapshot snapshot) {
		detail.copyFrom(snapshot.getDetail());
		update();
	}
	
	protected void validate() throws JSValidationException {
		detail.validate();
	}


	public EntitySlot generateSlot() {
		return new EntitySlot(
				getEntityId(),
				detail.getName());
	}
    

	
	@Override
	@Transient
	public String getEntityName() {
		return "MyLocation";
	}
	
	@Id
    @Column(name="ENTITY_ID", insertable = false, updatable = false)
    @SequenceGenerator(name="MyLocationGen", sequenceName="MY_LOCATION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "MyLocationGen")
	public Long getId() {
		return id;
	}

	public void setId(Long myLocationId) {
		this.id = myLocationId;
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
		return getAuditRepository().findRecentHistory(this);
	}

	@Transient
	public MyLocationRepositoryBean getEntityRepository() {
		return (MyLocationRepositoryBean) ApplicationContextFactory.getBean(MyLocationRepositoryBean.BEAN_NAME);
	}

	@Transient
	public MyLocationAuditRepository getAuditRepository() {
		return (MyLocationAuditRepository) ApplicationContextFactory.getBean(MyLocationAuditRepository.BEAN_NAME);
	}

	@Override
	@Transient
	public BaseRepository<MyLocation> getRepository() {
		return getEntityRepository();
	}
	
	
}
