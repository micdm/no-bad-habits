package com.micdm.nobadhabits.data;

import org.joda.time.DateTime;

public class Habit {

    private final String title;
    private final DateTime startDate;

    public Habit(String title, DateTime startDate) {
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
