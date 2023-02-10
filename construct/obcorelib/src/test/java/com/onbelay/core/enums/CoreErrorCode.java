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
package com.onbelay.core.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CoreErrorCode {

    SUCCESS                       	 ("0", "Success: transaction was successful"),
    MISSING_MY_LOCATION         ("PR-E00001", "Error: Missing my location."),
    MISSING_MY_INDEX_NAME       ("PR-E00002", "Error: Missing my index name."),
    MISSING_MY_INDEX_TYPE       ("PR-E00003", "Error: Missing my index type."),
    MISSING_INDEX_DAYS_EXPIRY        ("PR-E00004", "Error: Missing my index days offset for expiry."),
    MISSING_BASE_INDEX               ("PR-E00010", "Error: Missing base index and Index type is BASIS"),

    MISSING_PRICE_DATE               ("PR-E00020", "Error: Missing Price date."),
    MISSING_OBSERVED_DATE_TIME       ("PR-E00021", "Error: Missing Price observed date/time."),
    MISSING_PRICE_VALUE               ("PR-E00022", "Error: Missing Price value."),

    
    MISSING_MY_LOCATION_NAME    ("PR-E00030", "Error: Missing my location name."),
    SYSTEM_FAILURE                   ("E99999", "Error: Application error has occurred");

    private String code;
    private String description;

    private static final Map<String, CoreErrorCode> lookup
            = new HashMap<String, CoreErrorCode>();

    static {
        for (CoreErrorCode c : EnumSet.allOf(CoreErrorCode.class))
            lookup.put(c.code, c);
    }


    private CoreErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String toString() {
        return code + ":" + description;
    }

    public String getCode() {
        return code;
    }

    public static List<String> getTransactionCodes() {
        ArrayList<String> list = new ArrayList<String>();
        for (CoreErrorCode c : EnumSet.allOf(CoreErrorCode.class))
            list.add(c.getCode() + " : " + c.getDescription());
        return list;
    }

    public String getDescription() {
        return description;
    }

    public static CoreErrorCode lookUp(String code) {
        return lookup.get(code);
    }

}
