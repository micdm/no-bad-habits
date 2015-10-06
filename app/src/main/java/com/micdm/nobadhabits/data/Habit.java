package com.micdm.nobadhabits.data;

import org.joda.time.DateTime;

public class Habit {

    private final String id;
    private String title;
    private DateTime startDate;
    private boolean isFavorite;

    public Habit(String id, String title, DateTime startDate, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.isFavorite = isFavorite;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
