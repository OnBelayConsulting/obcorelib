package com.onbelay.testfixture.model;

import com.onbelay.core.query.model.ColumnDefinitions;

import java.util.HashMap;
import java.util.Map;

public class OrganizationColumnDefinitionsMap {

    private Map<String, ColumnDefinitions> columnDefinitionsMap = new HashMap<String, ColumnDefinitions>();

    public ColumnDefinitions getColumnDefinitions(String key) {
        return columnDefinitionsMap.get(key);
    }

    public void addColumnDefinitions(String key,ColumnDefinitions columnDefinitions) {
        this.columnDefinitionsMap.put(key, columnDefinitions);
    }
}
