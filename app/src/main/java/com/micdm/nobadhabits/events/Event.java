package com.micdm.nobadhabits.events;

public abstract class Event {

    private final EventType type;

    protected Event(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
