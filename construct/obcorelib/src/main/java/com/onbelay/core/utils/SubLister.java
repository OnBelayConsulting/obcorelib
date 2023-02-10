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
package com.onbelay.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Breaks a large list down into smaller lists.
 * 
 * Note: that the list item must implement hashCode() and equals() for this to work.
 *
 */
public class SubLister<E> {

    private List<E> masterList;
    private int maxNumberOfElements = 1000;
    int listSize;
    int currentStart = 0;
    int currentEnd;
    
    public SubLister(List<E> masterListIn) {
        this.masterList = masterListIn;
        initialize();
    }
    
    public SubLister(Collection<E> collectionIn, int maxNumberOfElements) {
    	this.maxNumberOfElements = maxNumberOfElements;
    	masterList = new ArrayList<E>();
    	masterList.addAll(collectionIn);
    	initialize();
    }
    
    public SubLister(List<E> masterListIn, int maxNumberOfElements) {
        this.masterList = masterListIn;
        this.maxNumberOfElements = maxNumberOfElements;
        initialize();
    }
    
    private void initialize() {
    	currentEnd = maxNumberOfElements;
    	listSize = masterList.size();
    	
    	if (currentEnd > listSize)
    		currentEnd = listSize;
    }


    /**
     * Returns true if there are more items.
     * @return
     */
    public boolean moreElements() {
        return (currentStart < listSize);
    }
    
    /**
     * Returns the next sublist with elements numbering up to the maxNumberOfElements.
     * @return a list - will be empty if no more elements.
     */
    public List<E> nextList() {
        if (moreElements() == false)
            return new ArrayList<>();
        
        List<E> subList = masterList.subList(currentStart, currentEnd);
        currentStart = currentEnd;
        currentEnd = currentEnd + maxNumberOfElements;
        
        if (currentEnd > listSize)
        	currentEnd = listSize;
        
        return subList;
        
    }
    
    public int getMaxNumberOfElements() {
        return maxNumberOfElements;
    }

    /**
     * Set the maximum number of elements in the list.
     * @param maxNumberOfElements
     */
    public void setMaxNumberOfElements(int maxNumberOfElements) {
        this.maxNumberOfElements = maxNumberOfElements;
    }
    
    
}
