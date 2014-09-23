package com.micdm.nobadhabits.events.events;

import com.micdm.nobadhabits.data.Habit;
import com.micdm.nobadhabits.events.Event;
import com.micdm.nobadhabits.events.EventType;

import java.util.List;

public class LoadHabitsEvent extends Event {

    private final List<Habit> habits;

    public LoadHabitsEvent(List<Habit> habits) {
        super(EventType.LOAD_HABITS);
        this.habits = habits;
    }

    public List<Habit> getHabits() {
        return habits;
    }
}
