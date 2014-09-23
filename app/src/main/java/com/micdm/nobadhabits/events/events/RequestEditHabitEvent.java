package com.micdm.nobadhabits.events.events;

import com.micdm.nobadhabits.events.Event;
import com.micdm.nobadhabits.events.EventType;

import org.joda.time.DateTime;

public class RequestEditHabitEvent extends Event {

    private final String id;
    private final String title;
    private final DateTime startDate;

    public RequestEditHabitEvent(String id, String title, DateTime startDate) {
        super(EventType.REQUEST_EDIT_HABIT);
        this.id = id;
        this.title = title;
        this.startDate = startDate;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public DateTime getStartDate() {
        return startDate;
    }
}
