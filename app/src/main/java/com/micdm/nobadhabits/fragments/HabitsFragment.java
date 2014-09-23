package com.micdm.nobadhabits.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.micdm.nobadhabits.CustomApplication;
import com.micdm.nobadhabits.R;
import com.micdm.nobadhabits.data.Habit;
import com.micdm.nobadhabits.events.EventManager;
import com.micdm.nobadhabits.events.EventType;
import com.micdm.nobadhabits.events.events.LoadHabitsEvent;
import com.micdm.nobadhabits.events.events.RequestLoadHabitsEvent;
import com.micdm.nobadhabits.events.events.RequestRemoveHabitEvent;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import java.util.List;

public class HabitsFragment extends Fragment {

    private static enum DurationMode {
        TEXT,
        GRAPHICS
    }

    private class HabitsAdapter extends BaseAdapter {

        private final Typeface TYPEFACE = Typeface.createFromAsset(getActivity().getAssets(), "FontAwesome.otf");

        private class ViewHolder {

            public View contentView;
            public TextView titleView;
            public TextView textDurationView;
            public View graphicsDurationView;
            public TextView durationYearsView;
            public TextView durationMonthsView;
            public TextView durationWeeksView;
            public TextView durationDaysView;
        }

        private List<Habit> habits;

        @Override
        public int getCount() {
            return (habits == null) ? 0 : habits.size();
        }

        @Override
        public Habit getItem(int position) {
            return habits.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.v__habits_item, null);
                holder = getHolder(convertView);
                convertView.setTag(holder);
                holder.durationYearsView.setTypeface(TYPEFACE);
                holder.durationMonthsView.setTypeface(TYPEFACE);
                holder.durationWeeksView.setTypeface(TYPEFACE);
                holder.durationDaysView.setTypeface(TYPEFACE);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Habit habit = getItem(position);
            holder.contentView.setSelected(selectedHabit == habit);
            holder.titleView.setText(habit.getTitle());
            setupDurationViews(holder, habit);
            return convertView;
        }

        private ViewHolder getHolder(View view) {
            ViewHolder holder = new ViewHolder();
            holder.contentView = view.findViewById(R.id.v__habits_item__content);
            holder.titleView = (TextView) view.findViewById(R.id.v__habits_item__title);
            holder.textDurationView = (TextView) view.findViewById(R.id.v__habits_item__text_duration);
            holder.graphicsDurationView = view.findViewById(R.id.v__habits_item__graphics_duration);
            holder.durationYearsView = (TextView) view.findViewById(R.id.v__habits_item__duration_years);
            holder.durationMonthsView = (TextView) view.findViewById(R.id.v__habits_item__duration_months);
            holder.durationWeeksView = (TextView) view.findViewById(R.id.v__habits_item__duration_weeks);
            holder.durationDaysView = (TextView) view.findViewById(R.id.v__habits_item__duration_days);
            return holder;
        }

        private void setupDurationViews(ViewHolder holder, Habit habit) {
            int days = Days.daysBetween(habit.getStartDate(), DateTime.now()).getDays();
            switch (durationMode) {
                case TEXT:
                    holder.graphicsDurationView.setVisibility(View.GONE);
                    String text;
                    if (days == 0) {
                        text = getString(R.string.f__habits__text_duration_first_day);
                    } else {
                        text = getString(R.string.f__habits__text_duration, days, getResources().getQuantityString(R.plurals.f__habits__text_duration_unit, days));
                    }
                    holder.textDurationView.setText(text);
                    holder.textDurationView.setVisibility(View.VISIBLE);
                    break;
                case GRAPHICS:
                    holder.textDurationView.setVisibility(View.GONE);
                    if (days == 0) {
                        holder.durationYearsView.setVisibility(View.GONE);
                        holder.durationMonthsView.setVisibility(View.GONE);
                        holder.durationWeeksView.setVisibility(View.GONE);
                        holder.durationDaysView.setText(getString(R.string.f__habits__graphics_duration_incomplete_unit));
                        holder.durationDaysView.setVisibility(View.VISIBLE);
                    } else {
                        Period duration = new Period(habit.getStartDate(), DateTime.now());
                        setupDurationView(holder.durationYearsView, duration.getYears());
                        setupDurationView(holder.durationMonthsView, duration.getMonths());
                        setupDurationView(holder.durationWeeksView, duration.getWeeks());
                        setupDurationView(holder.durationDaysView, duration.getDays());
                    }
                    holder.graphicsDurationView.setVisibility(View.VISIBLE);
                    break;
            }
        }

        private void setupDurationView(TextView view, int duration) {
            if (duration == 0) {
                view.setVisibility(View.GONE);
            } else {
                view.setText(StringUtils.repeat(getString(R.string.f__habits__graphics_duration_unit), duration));
                view.setVisibility(View.VISIBLE);
            }
        }

        public void setHabits(List<Habit> habits) {
            this.habits = habits;
            notifyDataSetChanged();
        }
    }

    private final ActionMode.Callback onStartActionModeListener = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.habit, menu);
            return true;
        }
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.m__habit_remove:
                    getEventManager().publish(new RequestRemoveHabitEvent(selectedHabit));
                    selectedHabit = null;
                    mode.finish();
                    return true;
            }
            return false;
        }
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            selectedHabit = null;
            ((HabitsAdapter) habitsView.getAdapter()).notifyDataSetChanged();
        }
    };

    private DurationMode durationMode = DurationMode.GRAPHICS;
    private Habit selectedHabit;

    private ListView habitsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        habitsView = (ListView) inflater.inflate(R.layout.f__habits, null);
        habitsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (durationMode) {
                    case TEXT:
                        durationMode = DurationMode.GRAPHICS;
                        break;
                    case GRAPHICS:
                        durationMode = DurationMode.TEXT;
                        break;
                }
                ((HabitsAdapter) parent.getAdapter()).notifyDataSetChanged();
            }
        });
        habitsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedHabit == null) {
                    view.startActionMode(onStartActionModeListener);
                }
                HabitsAdapter adapter = (HabitsAdapter) parent.getAdapter();
                selectedHabit = adapter.getItem(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        return habitsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribeForEvents();
        getEventManager().publish(new RequestLoadHabitsEvent());
    }

    private void subscribeForEvents() {
        EventManager manager = getEventManager();
        manager.subscribe(this, EventType.LOAD_HABITS, new EventManager.OnEventListener<LoadHabitsEvent>() {
            @Override
            public void onEvent(LoadHabitsEvent event) {
                HabitsAdapter adapter = (HabitsAdapter) habitsView.getAdapter();
                if (adapter == null) {
                    adapter = new HabitsAdapter();
                    habitsView.setAdapter(adapter);
                }
                adapter.setHabits(event.getHabits());
            }
        });
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }

    @Override
    public void onStop() {
        super.onStop();
        getEventManager().unsubscribeAll(this);
    }
}
