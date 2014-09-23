package com.micdm.nobadhabits.events.intents;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.micdm.nobadhabits.data.Habit;
import com.micdm.nobadhabits.events.Event;
import com.micdm.nobadhabits.events.EventType;
import com.micdm.nobadhabits.events.events.LoadHabitsEvent;
import com.micdm.nobadhabits.events.events.RequestEditHabitEvent;
import com.micdm.nobadhabits.events.events.RequestRemoveHabitEvent;
import com.micdm.nobadhabits.events.events.SelectDateEvent;
import com.micdm.nobadhabits.parcels.HabitParcel;

import java.util.ArrayList;

public class EventConverter {

    private final Context context;

    public EventConverter(Context context) {
        this.context = context;
    }

    public Intent convert(Event event) {
        Intent intent = new Intent(getIntentAction(event.getType()));
        switch (event.getType()) {
            case LOAD_HABITS:
                buildIntentForLoadHabitsEvent((LoadHabitsEvent) event, intent);
                break;
            case REQUEST_EDIT_HABIT:
                buildIntentForRequestEditHabitEvent((RequestEditHabitEvent) event, intent);
                break;
            case REQUEST_REMOVE_HABIT:
                buildIntentForRequestRemoveHabitEvent((RequestRemoveHabitEvent) event, intent);
                break;
            case SELECT_DATE:
                buildIntentForSelectDateEvent((SelectDateEvent) event, intent);
                break;
        }
        return intent;
    }

    public String getIntentAction(EventType type) {
        return String.format("%s.event.%s", context.getPackageName(), type);
    }

    private void buildIntentForLoadHabitsEvent(LoadHabitsEvent event, Intent intent) {
        ArrayList<Parcelable> parcels = new ArrayList<Parcelable>();
        for (Habit habit: event.getHabits()) {
            parcels.add(new HabitParcel(habit));
        }
        intent.putParcelableArrayListExtra("habits", parcels);
    }

    private void buildIntentForRequestEditHabitEvent(RequestEditHabitEvent event, Intent intent) {
        intent.putExtra("id", event.getId());
        intent.putExtra("title", event.getTitle());
        intent.putExtra("start_date", event.getStartDate().toString());
    }

    private void buildIntentForRequestRemoveHabitEvent(RequestRemoveHabitEvent event, Intent intent) {
        intent.putExtra("habit", new HabitParcel(event.getHabit()));
    }

    private void buildIntentForSelectDateEvent(SelectDateEvent event, Intent intent) {
        intent.putExtra("date", event.getDate().toString());
    }
}
