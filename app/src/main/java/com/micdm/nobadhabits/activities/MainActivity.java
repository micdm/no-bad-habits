package com.micdm.nobadhabits.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.micdm.nobadhabits.CustomApplication;
import com.micdm.nobadhabits.R;
import com.micdm.nobadhabits.events.EventManager;
import com.micdm.nobadhabits.events.EventType;
import com.micdm.nobadhabits.events.events.LoadHabitsEvent;
import com.micdm.nobadhabits.events.events.RequestEditHabitEvent;
import com.micdm.nobadhabits.events.events.RequestLoadHabitsEvent;
import com.micdm.nobadhabits.events.events.RequestRemoveHabitEvent;
import com.micdm.nobadhabits.fragments.EditHabitFragment;
import com.micdm.nobadhabits.fragments.HabitsFragment;
import com.micdm.nobadhabits.misc.FragmentTag;
import com.micdm.nobadhabits.misc.HabitManager;
import com.micdm.nobadhabits.widgets.CustomAppWidgetProvider;

import org.joda.time.DateTime;

public class MainActivity extends FragmentActivity {

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
        manager.subscribe(this, EventType.LOAD_HABITS, new EventManager.OnEventListener<LoadHabitsEvent>() {
            @Override
            public void onEvent(LoadHabitsEvent event) {
                updateWidgets();
            }
        });
        manager.subscribe(this, EventType.REQUEST_EDIT_HABIT, new EventManager.OnEventListener<RequestEditHabitEvent>() {
            @Override
            public void onEvent(RequestEditHabitEvent event) {
                String id = event.getId();
                String title = event.getTitle();
                DateTime startDate = event.getStartDate();
                boolean isFavorite = event.isFavorite();
                if (id == null) {
                    habitManager.add(title, startDate, isFavorite);
                } else {
                    habitManager.update(id, title, startDate, isFavorite);
                }
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

    private void updateWidgets() {
        int ids[] = getWidgetIds();
        if (ids.length == 0) {
            return;
        }
        Intent intent = new Intent(this, CustomAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    private int[] getWidgetIds() {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        ComponentName name = new ComponentName(this, CustomAppWidgetProvider.class);
        return manager.getAppWidgetIds(name);
    }

    private void addHabitListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.a__main__content, new HabitsFragment(), FragmentTag.HABITS);
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (habitManager.get().size() == 0) {
            showAddHabitDialog();
        }
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
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag(FragmentTag.EDIT_HABIT) == null) {
            EditHabitFragment.getInstance().show(manager, FragmentTag.EDIT_HABIT);
        }
    }
}
