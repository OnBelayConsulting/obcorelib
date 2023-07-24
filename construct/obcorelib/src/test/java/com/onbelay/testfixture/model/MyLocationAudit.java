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

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.onbelay.core.entity.model.AuditAbstractEntity;
import com.onbelay.core.entity.model.TemporalAbstractEntity;
import com.onbelay.testfixture.shared.LocationDetail;
import com.onbelay.core.utils.DateUtils;

@Entity
@Table(name = "MY_LOCATION_AUDIT")
@NamedQueries({
    @NamedQuery(
       name = MyLocationAudit.FIND_AUDIT_BY_TO_DATE,
       query = "SELECT audit " +
			   "  FROM MyLocationAudit audit " +
       		    "WHERE audit.historyDateTimeStamp.validToDateTime = :date " +
       		      "AND audit.myLocation = :myLocation")
})
public class MyLocationAudit extends AuditAbstractEntity {
	public static final String FIND_AUDIT_BY_TO_DATE = "MyLocationAudit.FIND_AUDIT_BY_TO_DATE";

	private Integer id;

	private MyLocation myLocation;
	
	private LocationDetail detail = new  LocationDetail();
	
	protected MyLocationAudit() {
		
	}
	
	protected MyLocationAudit(MyLocation myLocation) {
		this.myLocation = myLocation;
	}
	
	protected static MyLocationAudit create(MyLocation myLocation) {
		MyLocationAudit audit = new MyLocationAudit(myLocation);
		audit.copyFrom(myLocation);
		return audit;
	}
	

	@Id
    @Column(name="AUDIT_ID", insertable = false, updatable = false)
    @SequenceGenerator(name="MyLocationAuditGen", sequenceName="MY_LOCATION_AUDIT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "MyLocationAuditGen")
	public Integer getId() {
		return id;
	}

	public void setId(Integer myLocationId) {
		this.id = myLocationId;
	}

	
	@ManyToOne
	@JoinColumn(name="ENTITY_ID")
	public MyLocation getMyLocation() {
		return myLocation;
	}

	private void setMyLocation(MyLocation myLocation) {
		this.myLocation = myLocation;
	}

	@Embedded
	public LocationDetail getDetail() {
		return detail;
	}

	private void setDetail(LocationDetail detail) {
		this.detail = detail;
	}


	@Override
	@Transient
	public TemporalAbstractEntity getParent() {
		return myLocation;
	}

	@Override
	public void copyFrom(TemporalAbstractEntity entity) {
		MyLocation myLocation = (MyLocation) entity;
		this.detail.copyFrom(myLocation.getDetail());
	}

	public static MyLocationAudit findRecentHistory(MyLocation myLocation) {
		String[] parmNames = {"myLocation", "date" };
		Object[] parms =     {myLocation,   DateUtils.getValidToDateTime()};

		return (MyLocationAudit) getAuditEntityRepository().executeSingleResultQuery(
				FIND_AUDIT_BY_TO_DATE,
				parmNames,
				parms);

	}



}
