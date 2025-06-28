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

import com.onbelay.core.entity.model.AuditAbstractEntity;
import com.onbelay.core.entity.model.TemporalAbstractEntity;
import com.onbelay.core.utils.DateUtils;
import com.onbelay.testfixture.shared.MyAggregateDetail;
import jakarta.persistence.*;

@Entity
@Table(name = "MY_AGGREGATE_AUDIT")
@NamedQueries({
    @NamedQuery(
       name = MyAggregateAudit.FIND_AUDIT_BY_TO_DATE,
       query = "SELECT audit " +
			   "  FROM MyAggregateAudit audit " +
       		    "WHERE audit.historyDateTimeStamp.validToDateTime = :date " +
       		      "AND audit.myAggregate = :myAggregate")
})
public class MyAggregateAudit extends AuditAbstractEntity {
	public static final String FIND_AUDIT_BY_TO_DATE = "MyAggregateAudit.FIND_AUDIT_BY_TO_DATE";

	private Integer id;

	private MyAggregate myAggregate;

	private MyLocation location;
	
	private MyAggregateDetail detail = new  MyAggregateDetail();
	
	protected MyAggregateAudit() {
		
	}
	
	protected MyAggregateAudit(MyAggregate myAggregate) {
		this.myAggregate = myAggregate;
		this.location = myAggregate.getLocation();

	}
	
	protected static MyAggregateAudit create(MyAggregate myAggregate) {
		MyAggregateAudit audit = new MyAggregateAudit(myAggregate);
		audit.copyFrom(myAggregate);
		return audit;
	}
	

	@Id
    @Column(name="AUDIT_ID", insertable = false, updatable = false)
    @SequenceGenerator(name="MyAggregateAuditGen", sequenceName="MY_AGGREGATE_AUDIT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "MyAggregateAuditGen")
	public Integer getId() {
		return id;
	}

	public void setId(Integer myAggregateId) {
		this.id = myAggregateId;
	}

	@ManyToOne
	@JoinColumn(name="LOCATION_ID")
	public MyLocation getLocation() {
		return location;
	}

	public void setLocation(MyLocation location) {
		this.location = location;
	}

	@ManyToOne
	@JoinColumn(name="ENTITY_ID")
	public MyAggregate getMyAggregate() {
		return myAggregate;
	}

	private void setMyAggregate(MyAggregate myAggregate) {
		this.myAggregate = myAggregate;
	}

	@Embedded
	public MyAggregateDetail getDetail() {
		return detail;
	}

	private void setDetail(MyAggregateDetail detail) {
		this.detail = detail;
	}


	@Override
	@Transient
	public TemporalAbstractEntity getParent() {
		return myAggregate;
	}

	@Override
	public void copyFrom(TemporalAbstractEntity entity) {
		MyAggregate myAggregate = (MyAggregate) entity;
		this.location = myAggregate.getLocation();
		this.detail.copyFrom(myAggregate.getDetail());
	}

	public static MyAggregateAudit findRecentHistory(MyAggregate myAggregate) {
		String[] parmNames = {"myAggregate", "date" };
		Object[] parms =     {myAggregate,   DateUtils.getValidToDateTime()};

		return (MyAggregateAudit) getAuditEntityRepository().executeSingleResultQuery(
				FIND_AUDIT_BY_TO_DATE,
				parmNames,
				parms);

	}



}
