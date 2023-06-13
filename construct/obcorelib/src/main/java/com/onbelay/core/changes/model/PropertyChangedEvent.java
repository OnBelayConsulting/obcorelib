package com.onbelay.core.changes.model;

import com.onbelay.core.changes.enums.EventStatus;

public class PropertyChangedEvent {

    private EventStatus eventStatus;
    private String propertyName;

    private String previousValue;

    private String newValue;

    public PropertyChangedEvent(EventStatus eventStatus, String propertyName, String previousValue, String newValue) {
        this.eventStatus = eventStatus;
        this.propertyName = propertyName;
        this.previousValue = previousValue;
        this.newValue = newValue;
    }

    public PropertyChangedEvent(String propertyName, String previousValue, String newValue) {
        this.eventStatus = EventStatus.MODIFIED;
        this.propertyName = propertyName;
        this.previousValue = previousValue;
        this.newValue = newValue;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
