package com.micdm.nobadhabits.events.intents;

import android.content.Intent;
import android.os.Parcelable;

import com.micdm.nobadhabits.data.Habit;
import com.micdm.nobadhabits.events.Event;
import com.micdm.nobadhabits.events.EventType;
import com.micdm.nobadhabits.events.events.LoadHabitsEvent;
import com.micdm.nobadhabits.events.events.RequestEditHabitEvent;
import com.micdm.nobadhabits.events.events.RequestLoadHabitsEvent;
import com.micdm.nobadhabits.events.events.RequestRemoveHabitEvent;
import com.micdm.nobadhabits.events.events.SelectDateEvent;
import com.micdm.nobadhabits.parcels.HabitParcel;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class IntentConverter {

    public Event convert(Intent intent) {
        switch (getEventTypeFromIntent(intent)) {
            case REQUEST_LOAD_HABITS:
                return new RequestLoadHabitsEvent();
            case LOAD_HABITS:
                return getLoadHabitsEvent(intent);
            case REQUEST_EDIT_HABIT:
                return getRequestEditHabitEvent(intent);
            case REQUEST_REMOVE_HABIT:
                return getRequestRemoveHabitEvent(intent);
            case SELECT_DATE:
                return getSelectDateEvent(intent);
            default:
                throw new RuntimeException("unknown event type");
        }
    }

    private EventType getEventTypeFromIntent(Intent intent) {
        String action = intent.getAction();
        String[] parts = action.split("\\.");
        String typeName = parts[parts.length - 1];
        for (EventType type: EventType.values()) {
            if (type.toString().equals(typeName)) {
                return type;
            }
        }
        throw new RuntimeException("unknown event type");
    }

    private LoadHabitsEvent getLoadHabitsEvent(Intent intent) {
        List<Habit> habits = new ArrayList<Habit>();
        for (Parcelable parcel: intent.getParcelableArrayListExtra("habits")) {
            habits.add(((HabitParcel) parcel).getHabit());
        }
        return new LoadHabitsEvent(habits);
    }

    private RequestEditHabitEvent getRequestEditHabitEvent(Intent intent) {
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        DateTime startDate = new DateTime(intent.getStringExtra("start_date"));
        return new RequestEditHabitEvent(id, title, startDate);
    }

    private RequestRemoveHabitEvent getRequestRemoveHabitEvent(Intent intent) {
        Habit habit = ((HabitParcel) intent.getParcelableExtra("habit")).getHabit();
        return new RequestRemoveHabitEvent(habit);
    }

    private SelectDateEvent getSelectDateEvent(Intent intent) {
        LocalDate date = new LocalDate(intent.getStringExtra("date"));
        return new SelectDateEvent(date);
    }
}
