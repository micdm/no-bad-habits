package com.micdm.nobadhabits.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.micdm.nobadhabits.CustomApplication;
import com.micdm.nobadhabits.R;
import com.micdm.nobadhabits.data.Habit;
import com.micdm.nobadhabits.events.EventManager;
import com.micdm.nobadhabits.events.EventType;
import com.micdm.nobadhabits.events.events.RequestEditHabitEvent;
import com.micdm.nobadhabits.events.events.SelectDateEvent;
import com.micdm.nobadhabits.misc.FragmentTag;
import com.micdm.nobadhabits.parcels.HabitParcel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EditHabitFragment extends DialogFragment {

    private static final String INIT_ARG_HABIT = "habit";
    private static final DateTimeFormatter dateFormatter = DateTimeFormat.shortDate();

    private Habit habit;
    private DateTime startDate;

    private TextView titleView;
    private TextView startDateView;

    public static EditHabitFragment getInstance() {
        return getInstance(null);
    }

    public static EditHabitFragment getInstance(Habit habit) {
        EditHabitFragment fragment = new EditHabitFragment();
        Bundle arguments = new Bundle();
        if (habit != null) {
            arguments.putParcelable(INIT_ARG_HABIT, new HabitParcel(habit));
        }
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        habit = getHabit();
        startDate = (habit == null) ? DateTime.now().withTimeAtStartOfDay() : habit.getStartDate();
    }

    private Habit getHabit() {
        HabitParcel parcel = getArguments().getParcelable(INIT_ARG_HABIT);
        return (parcel == null) ? null : parcel.getHabit();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(setupView());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = titleView.getText().toString();
                if (title.length() != 0) {
                    publishEditEvent(title);
                }
            }
        });
        return builder.create();
    }

    private View setupView() {
        View view = View.inflate(getActivity(), R.layout.f__edit_habit, null);
        titleView = (TextView) view.findViewById(R.id.f__edit_habit__title);
        if (habit != null) {
            titleView.setText(habit.getTitle());
        }
        startDateView = (TextView) view.findViewById(R.id.f__edit_habit__start_date);
        if (startDate != null) {
            setupStartDateView();
        }
        View dateView = view.findViewById(R.id.f__edit_habit__select_date);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getChildFragmentManager();
                if (manager.findFragmentByTag(FragmentTag.SELECT_DATE) == null) {
                    SelectDateFragment.getInstance(startDate.toLocalDate()).show(manager, FragmentTag.SELECT_DATE);
                }
            }
        });
        return view;
    }

    private void setupStartDateView() {
        if (startDate.isEqual(DateTime.now().withTimeAtStartOfDay())) {
            startDateView.setText(getString(R.string.f__edit_habit__start_date));
        } else {
            startDateView.setText(getString(R.string.f__edit_habit__start_date_past, dateFormatter.print(startDate)));
        }
    }

    private void publishEditEvent(String title) {
        String id = (habit == null) ? null : habit.getId();
        boolean isFavorite = (habit != null && habit.isFavorite());
        getEventManager().publish(new RequestEditHabitEvent(id, title, startDate, isFavorite));
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribeForEvents();
    }

    private void subscribeForEvents() {
        EventManager manager = getEventManager();
        manager.subscribe(this, EventType.SELECT_DATE, new EventManager.OnEventListener<SelectDateEvent>() {
            @Override
            public void onEvent(SelectDateEvent event) {
                startDate = event.getDate().toDateTimeAtStartOfDay();
                setupStartDateView();
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
