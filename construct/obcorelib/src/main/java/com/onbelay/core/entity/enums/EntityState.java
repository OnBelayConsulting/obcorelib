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
package com.onbelay.core.entity.enums;

/**
 * Defines the current state of a value object returned in a service call to the server
 * for processing:
 * <ul>
 * <li> UNMODIFIED - This value object was not modified. (Usually that means it will be ignored.)
 * <li> MODIFIED - This value object was modified by the client.
 * <li> NEW - This value object is newly created on the client
 * <li> EXPIRE - This value object has been marked for expiry.
 * </ul>
 * 
 * @author canmxf
 *
 */
public enum EntityState {
    UNMODIFIED,
    MODIFIED,
    NEW,
    DELETE;

}
