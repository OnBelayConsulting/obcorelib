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
package com.onbelay.testfixture.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.onbelay.core.codes.annotations.CodeLabelSerializer;
import com.onbelay.core.codes.annotations.InjectCodeLabel;
import com.onbelay.core.entity.snapshot.AbstractDetail;
import com.onbelay.core.enums.CoreErrorCode;
import com.onbelay.core.exception.OBValidationException;
import com.onbelay.testfixture.enums.GeoCode;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyAggregateDetail extends AbstractDetail {

	private String name;
	private BigDecimal quantity;
	private LocalDate startDate;
	private BigDecimal total;

	public MyAggregateDetail() {
	}
	

	public MyAggregateDetail withName(String name) {
		this.name = name;
		return this;
	}

	public MyAggregateDetail withQuantity(BigDecimal quantity) {
		this.quantity = quantity;
		return this;
	}


	public MyAggregateDetail withStartDate(LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	public MyAggregateDetail withTotal(BigDecimal total) {
		this.total = total;
		return this;
	}

	public void setDefaults() {
		
	}

	public void validate() throws OBValidationException {
		if (name == null)
			throw new OBValidationException(CoreErrorCode.MISSING_MY_LOCATION_NAME.getCode());
	}

	@Column(name = "AGGREGATE_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "MY_QUANTITY")
	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Column(name = "START_DATE")
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	@Column(name = "MY_TOTAL")
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void copyFrom(MyAggregateDetail copy) {
		
		if (copy.name != null)
			this.name = copy.name;

		if (copy.quantity != null)
			this.quantity = copy.quantity;

		if (copy.startDate != null)
			this.startDate = copy.startDate;

		if (copy.total != null)
			this.total = copy.total;

	}
	
}
