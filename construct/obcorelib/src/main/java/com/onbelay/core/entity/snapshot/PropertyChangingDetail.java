package com.onbelay.core.entity.snapshot;

import com.onbelay.core.changes.model.PropertyChangedEvent;

import java.util.ArrayList;
import java.util.List;

public class PropertyChangingDetail extends AbstractDetail {

    public List<PropertyChangedEvent> generatePropertyChangedEvents(PropertyChangingDetail detailIn) {
        ArrayList<PropertyChangedEvent> events = new ArrayList<>();
        return events;
    }

}
