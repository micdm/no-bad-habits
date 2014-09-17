package com.micdm.nobadhabits.events.events;

import com.micdm.nobadhabits.events.Event;
import com.micdm.nobadhabits.events.EventType;

public class RequestLoadHabitsEvent extends Event {

    public RequestLoadHabitsEvent() {
        super(EventType.REQUEST_LOAD_HABITS);
    }
}
