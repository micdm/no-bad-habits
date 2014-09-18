package com.micdm.nobadhabits.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Period;

import java.util.List;

public class HabitsFragment extends Fragment {

    private class HabitsAdapter extends BaseAdapter {

        private final Typeface TYPEFACE = Typeface.createFromAsset(getActivity().getAssets(), "FontAwesome.otf");

        private class ViewHolder {

            public TextView titleView;
            public TextView durationYearsView;
            public TextView durationMonthsView;
            public TextView durationWeeksView;
            public TextView durationDaysView;
        }

        private List<Habit> _habits;

        @Override
        public int getCount() {
            return (_habits == null) ? 0 : _habits.size();
        }

        @Override
        public Habit getItem(int position) {
            return _habits.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.v__habits_item, null);
            }
            ViewHolder holder = getHolder(convertView);
            Habit habit = getItem(position);
            holder.titleView.setText(habit.getTitle());
            setupDurationViews(holder, habit);
            return convertView;
        }

        private ViewHolder getHolder(View view) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder == null) {
                holder = new ViewHolder();
                holder.titleView = (TextView) view.findViewById(R.id.v__habits_item__title);
                holder.durationYearsView = (TextView) view.findViewById(R.id.v__habits_item__duration_years);
                holder.durationMonthsView = (TextView) view.findViewById(R.id.v__habits_item__duration_months);
                holder.durationWeeksView = (TextView) view.findViewById(R.id.v__habits_item__duration_weeks);
                holder.durationDaysView = (TextView) view.findViewById(R.id.v__habits_item__duration_days);
                view.setTag(holder);
            }
            return holder;
        }

        private void setupDurationViews(ViewHolder holder, Habit habit) {
            Period duration = habit.getDuration();
            setupDurationView(holder.durationYearsView, duration.getYears(), false);
            setupDurationView(holder.durationMonthsView, duration.getMonths(), false);
            setupDurationView(holder.durationWeeksView, duration.getWeeks(), false);
            setupDurationView(holder.durationDaysView, duration.getDays(), true);
        }

        private void setupDurationView(TextView view, int duration, boolean showIfIncomplete) {
            view.setTypeface(TYPEFACE);
            if (duration == 0) {
                if (showIfIncomplete) {
                    view.setText(getString(R.string.f__habits__duration_unit_incomplete));
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            } else {
                view.setText(StringUtils.repeat(getString(R.string.f__habits__duration_unit), duration));
                view.setVisibility(View.VISIBLE);
            }
        }

        public void setHabits(List<Habit> habits) {
            _habits = habits;
            notifyDataSetChanged();
        }
    }

    private ListView _habitsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _habitsView = (ListView) inflater.inflate(R.layout.f__habits, null);
        return _habitsView;
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
                HabitsAdapter adapter = (HabitsAdapter) _habitsView.getAdapter();
                if (adapter == null) {
                    adapter = new HabitsAdapter();
                    _habitsView.setAdapter(adapter);
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
