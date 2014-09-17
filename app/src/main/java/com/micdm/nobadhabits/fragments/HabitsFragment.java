package com.micdm.nobadhabits.fragments;

import android.app.Fragment;
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

import java.util.List;

public class HabitsFragment extends Fragment {

    private class HabitsAdapter extends BaseAdapter {

        private class ViewHolder {

            private final TextView _titleView;
            private final TextView _durationView;

            public ViewHolder(TextView titleView, TextView durationView) {
                _titleView = titleView;
                _durationView = durationView;
            }

            public TextView getTitleView() {
                return _titleView;
            }

            public TextView getDurationView() {
                return _durationView;
            }
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.v__habits_item, null);
                TextView titleView = (TextView) convertView.findViewById(R.id.v__habits_item__title);
                TextView durationView = (TextView) convertView.findViewById(R.id.v__habits_item__duration);
                holder = new ViewHolder(titleView, durationView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Habit habit = getItem(position);
            holder.getTitleView().setText(habit.getTitle());
            holder.getDurationView().setText(String.valueOf(habit.getDuration().getSeconds()));
            return convertView;
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
