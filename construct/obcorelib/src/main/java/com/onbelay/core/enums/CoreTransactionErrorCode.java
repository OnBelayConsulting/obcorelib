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

import java.util.*;

public enum CoreTransactionErrorCode {

    SUCCESS                                       ("0", "Success: transaction was successful"),
    APPSET_MISSING_KEY                            ("OB-E0001", "Missing application setting key"),
    APPSET_MISSING_VALUE                          ("OB-E0002", "Missing application setting value"),
    ENTITY_VERSION_FAIL                           ("OB-E0003", "Update failed - unmatched version number."),
    ENTITY_DELETE_FAIL                            ("OB-E0004", "Entity delete failed."),
    INVALID_ENTITY_ID                             ("OB-E0005", "Invalid EntityId."),
    QUERY_MORE_THAN_ONE_RESULT                    ("OB-E0100", "More than one result returned"),
    INVALID_QUERY                                 ("OB-E0110", "Invalid Query."),
    USER_MISSING_NAME                             ("OB-E0200", "Missing user name"),
    INVALID_CODE_FAMILY                           ("OB-E1000", "Invalid code family"),
    USER_UNAUTHORIZED                             ("OB-E2000", "User not authorized for this function."),
    SYSTEM_FAILURE                                ("OB-9999", "Error: Application error has occurred");

    private String code;
    private String description;

    private static final Map<String, CoreTransactionErrorCode> lookup
            = new HashMap<String, CoreTransactionErrorCode>();

    static {
        for (CoreTransactionErrorCode c : EnumSet.allOf(CoreTransactionErrorCode.class))
            lookup.put(c.code, c);
    }


    private CoreTransactionErrorCode(String code, String description) {
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
        for (CoreTransactionErrorCode c : EnumSet.allOf(CoreTransactionErrorCode.class))
            list.add(c.getCode() + " : " + c.getDescription());
        return list;
    }

    public String getDescription() {
        return description;
    }

    public static CoreTransactionErrorCode lookUp(String code) {
        return lookup.get(code);
    }
}
