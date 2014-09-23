package com.micdm.nobadhabits.events.events;

import com.micdm.nobadhabits.events.Event;
import com.micdm.nobadhabits.events.EventType;

import org.joda.time.LocalDate;

public class SelectDateEvent extends Event {

    private final LocalDate date;

    public SelectDateEvent(LocalDate date) {
        super(EventType.SELECT_DATE);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
