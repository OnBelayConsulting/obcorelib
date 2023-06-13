package com.onbelay.core.query.model;

import com.onbelay.core.query.enums.ColumnDataType;

import java.util.HashMap;
import java.util.Map;

public class BaseColumnDefinitions {

    protected Map<String, ColumnDefinition> definitionsMap = new HashMap<String, ColumnDefinition>();

    public static final ColumnDefinition IS_EXPIRED = new ColumnDefinition("isExpired", ColumnDataType.BOOLEAN, "isExpired");
    public static final ColumnDefinition id = new ColumnDefinition("id", ColumnDataType.INTEGER, "id");

    public BaseColumnDefinitions() {
        add(IS_EXPIRED);
        add(id);
    }

    public BaseColumnDefinitions(boolean hasExpired) {
        if (hasExpired)
            add(IS_EXPIRED);
        add(id);
    }

    public ColumnDefinition get(String name) {
        return definitionsMap.get(name);
    }

    protected void add(ColumnDefinition definition) {
        definitionsMap.put(definition.getName(), definition);
    }

}
