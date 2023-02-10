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
package com.onbelay.core.entity.snapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all collections of snapshots.
 * The version refers to the API version of the collection.
 * 
 * Start is the start of the collection within the domain.
 * count - the number of elements returned. The default is usually 100.
 * totalItems - total of items in the domain that meet the criteria.
 * 
 * @author lefeu
 *
 * @param <T> - Snapshot class.
 */
public abstract class AbstractJSONCollection<T> {

	private Integer version;
	private int start;
	private int count;
	private int totalItems;
	private String name;
	
	private String errorCode = "0";
	private String errorMessage = "";
	private boolean wasSuccessful = true;
	
	private List<T> snapshots = new ArrayList<T>();
	

	public AbstractJSONCollection() {

	} 
	
	public AbstractJSONCollection(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		wasSuccessful = false;
	} 

	
	public AbstractJSONCollection(String name) {
		
	}

	public AbstractJSONCollection(String name, List<T> snapshots) {
		super();
		this.name = name;
		this.snapshots = snapshots;
		count = snapshots.size();
	}
	
	

	public List<T> getSnapshots() {
		return snapshots;
	}

	public void setSnapshots(List<T> snapshots) {
		this.snapshots = snapshots;
		count = snapshots.size();
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int count) {
		this.totalItems = count;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int limit) {
		this.count = limit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isWasSuccessful() {
		return wasSuccessful;
	}

	public void setWasSuccessful(boolean wasSuccessful) {
		this.wasSuccessful = wasSuccessful;
	}
	
	
}
