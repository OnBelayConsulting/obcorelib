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
public abstract class AbstractSnapshotCollection<T> extends ErrorHoldingSnapshot {

	private int start = 0;
	private int limit = 10;
	private int count = 0;
	private int totalItems;
	private String name;

	private List<T> snapshots = new ArrayList<T>();
	

	public AbstractSnapshotCollection() {

	}

	public AbstractSnapshotCollection(String name) {
		this.name = name;
	}


	public AbstractSnapshotCollection(String name, String errorMessage) {
		super(errorMessage);
		this.name = name;
	}

	public AbstractSnapshotCollection(
			String name,
			String errorMessage,
			List<String> parameters) {
		super(errorMessage, parameters);
		this.name = name;
	}

	public AbstractSnapshotCollection(
			String name,
			int start,
			int limit,
			int totalItems,
			List<T> snapshots) {

		this.start = start;
		this.limit = limit;
		this.count = snapshots.size();
		this.totalItems = totalItems;
		this.name = name;
		this.snapshots = snapshots;
	}

	public AbstractSnapshotCollection(
			String name,
			int start,
			int limit,
			int totalItems) {

		this.start = start;
		this.limit = limit;
		this.count = 0;
		this.totalItems = totalItems;
		this.name = name;
	}

	public List<T> getSnapshots() {
		return snapshots;
	}

	public void setSnapshots(List<T> snapshots) {
		this.snapshots = snapshots;
		count = snapshots.size();
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

	public void setCount(int count) {
		this.count = count;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
