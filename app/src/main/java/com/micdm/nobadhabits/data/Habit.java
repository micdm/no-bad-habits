package com.micdm.nobadhabits.data;

import org.joda.time.DateTime;

public class Habit {

    private final String _title;
    private final DateTime _startDate;

    public Habit(String title, DateTime startDate) {
        _title = title;
        _startDate = startDate;
    }

    public String getTitle() {
        return _title;
    }

    public DateTime getStartDate() {
        return _startDate;
    }
}
