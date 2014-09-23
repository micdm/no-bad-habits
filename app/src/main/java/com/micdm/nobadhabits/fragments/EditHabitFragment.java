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
import android.view.ViewGroup;
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

public class EditHabitFragment extends DialogFragment {

    private static final String INIT_ARG_HABIT = "habit";

    private Habit habit;
    private DateTime startDate;

    private TextView titleView;

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
        startDate = (habit == null) ? DateTime.now() : habit.getStartDate();
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
        View dateView = view.findViewById(R.id.f__edit_habit__date);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getChildFragmentManager();
                if (manager.findFragmentByTag(FragmentTag.SELECT_DATE) == null) {
                    SelectDateFragment.getInstance(startDate.toLocalDate()).show(manager, FragmentTag.SELECT_DATE);
                }
            }
        });
        setupChoicesView(view);
        return view;
    }

    private void setupChoicesView(View view) {
        ViewGroup choicesView = (ViewGroup) view.findViewById(R.id.f__edit_habit__quick_choices);
        for (String choice: getResources().getStringArray(R.array.f__edit_habit__quick_choices)) {
            TextView choiceView = (TextView) View.inflate(getActivity(), R.layout.v__add_quick_choice, null);
            choiceView.setText(new String(Character.toChars(Integer.parseInt(choice, 16))));
            choiceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = ((TextView) v).getText().toString();
                    publishEditEvent(title);
                    dismiss();
                }
            });
            choicesView.addView(choiceView);
        }
    }

    private void publishEditEvent(String title) {
        String id = (habit == null) ? null : habit.getId();
        getEventManager().publish(new RequestEditHabitEvent(id, title, startDate));
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
