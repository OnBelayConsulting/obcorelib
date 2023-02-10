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
package com.onbelay.core.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.onbelay.core.utils.SubLister;

import junit.framework.TestCase;

public class SubListerTest extends TestCase {

	
	@Test
	public void testLister() {
		
		List<Integer> masterListIn 
		= Stream
		.of(1, 2, 3, 5 ,6 ,6 ,7 ,7 ,8 ,9 ,3)
		.collect(Collectors.toList());
		
		SubLister<Integer> lister = new SubLister<Integer>(masterListIn, 4);
		
		List<Integer> nextList = lister.nextList();
		
		System.out.println(nextList);
	}
	
}
