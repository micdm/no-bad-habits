package com.micdm.nobadhabits.events.events;

import com.micdm.nobadhabits.data.Habit;
import com.micdm.nobadhabits.events.Event;
import com.micdm.nobadhabits.events.EventType;

public class RequestRemoveHabitEvent extends Event {

    private final Habit habit;

    public RequestRemoveHabitEvent(Habit habit) {
        super(EventType.REQUEST_REMOVE_HABIT);
        this.habit = habit;
    }

    public Habit getHabit() {
        return habit;
    }
}
