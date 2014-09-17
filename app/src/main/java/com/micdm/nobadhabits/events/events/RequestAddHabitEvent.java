package com.micdm.nobadhabits.events.events;

import com.micdm.nobadhabits.events.Event;
import com.micdm.nobadhabits.events.EventType;

public class RequestAddHabitEvent extends Event {

    private final String _title;

    public RequestAddHabitEvent(String title) {
        super(EventType.REQUEST_ADD_HABIT);
        _title = title;
    }

    public String getTitle() {
        return _title;
    }
}
