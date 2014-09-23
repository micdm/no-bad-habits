package com.micdm.nobadhabits.events.events;

import com.micdm.nobadhabits.events.Event;
import com.micdm.nobadhabits.events.EventType;

import org.joda.time.DateTime;

public class RequestAddHabitEvent extends Event {

    private final String title;
    private final DateTime startDate;

    public RequestAddHabitEvent(String title, DateTime startDate) {
        super(EventType.REQUEST_ADD_HABIT);
        this.title = title;
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public DateTime getStartDate() {
        return startDate;
    }
}
