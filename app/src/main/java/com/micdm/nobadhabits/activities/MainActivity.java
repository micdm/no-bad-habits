package com.micdm.nobadhabits.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.micdm.nobadhabits.CustomApplication;
import com.micdm.nobadhabits.R;
import com.micdm.nobadhabits.events.EventManager;
import com.micdm.nobadhabits.events.EventType;
import com.micdm.nobadhabits.events.events.LoadHabitsEvent;
import com.micdm.nobadhabits.events.events.RequestAddHabitEvent;
import com.micdm.nobadhabits.events.events.RequestLoadHabitsEvent;
import com.micdm.nobadhabits.events.events.RequestRemoveHabitEvent;
import com.micdm.nobadhabits.fragments.AddHabitFragment;
import com.micdm.nobadhabits.fragments.HabitsFragment;
import com.micdm.nobadhabits.misc.HabitManager;

public class MainActivity extends Activity {

    private static final String HABIT_LIST_FRAGMENT_TAG = "habit_list";
    private static final String ADD_HABIT_FRAGMENT_TAG = "add_habit";

    private final HabitManager habitManager = new HabitManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeForEvents();
        setContentView(R.layout.a__main);
        addHabitListFragment();
    }

    private void subscribeForEvents() {
        final EventManager manager = ((CustomApplication) getApplication()).getEventManager();
        manager.subscribe(this, EventType.REQUEST_LOAD_HABITS, new EventManager.OnEventListener<RequestLoadHabitsEvent>() {
            @Override
            public void onEvent(RequestLoadHabitsEvent event) {
                manager.publish(new LoadHabitsEvent(habitManager.get()));
            }
        });
        manager.subscribe(this, EventType.REQUEST_ADD_HABIT, new EventManager.OnEventListener<RequestAddHabitEvent>() {
            @Override
            public void onEvent(RequestAddHabitEvent event) {
                habitManager.add(event.getTitle());
                manager.publish(new LoadHabitsEvent(habitManager.get()));
            }
        });
        manager.subscribe(this, EventType.REQUEST_REMOVE_HABIT, new EventManager.OnEventListener<RequestRemoveHabitEvent>() {
            @Override
            public void onEvent(RequestRemoveHabitEvent event) {
                habitManager.remove(event.getHabit());
                manager.publish(new LoadHabitsEvent(habitManager.get()));
            }
        });
    }

    private void addHabitListFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.a__main__content, new HabitsFragment(), HABIT_LIST_FRAGMENT_TAG);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m__main__add:
                showAddHabitDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddHabitDialog() {
        FragmentManager manager = getFragmentManager();
        if (manager.findFragmentByTag(ADD_HABIT_FRAGMENT_TAG) == null) {
            (new AddHabitFragment()).show(manager, ADD_HABIT_FRAGMENT_TAG);
        }
    }
}
