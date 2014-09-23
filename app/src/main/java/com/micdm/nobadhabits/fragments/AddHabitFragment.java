package com.micdm.nobadhabits.fragments;

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
import com.micdm.nobadhabits.events.EventManager;
import com.micdm.nobadhabits.events.EventType;
import com.micdm.nobadhabits.events.events.RequestAddHabitEvent;
import com.micdm.nobadhabits.events.events.SelectDateEvent;

import org.joda.time.DateTime;

public class AddHabitFragment extends DialogFragment {

    private static final String SELECT_DATE_FRAGMENT_TAG = "select_date";

    private DateTime startDate = DateTime.now();

    private TextView titleView;

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
                    getEventManager().publish(new RequestAddHabitEvent(title, startDate));
                }
            }
        });
        return builder.create();
    }

    private View setupView() {
        View view = View.inflate(getActivity(), R.layout.f__add, null);
        titleView = (TextView) view.findViewById(R.id.f__add__title);
        View dateView = view.findViewById(R.id.f__add__date);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getChildFragmentManager();
                if (manager.findFragmentByTag(SELECT_DATE_FRAGMENT_TAG) == null) {
                    SelectDateFragment.getInstance(startDate.toLocalDate()).show(manager, SELECT_DATE_FRAGMENT_TAG);
                }
            }
        });
        setupChoicesView(view);
        return view;
    }

    private void setupChoicesView(View view) {
        ViewGroup choicesView = (ViewGroup) view.findViewById(R.id.f__add__quick_choices);
        for (String choice: getResources().getStringArray(R.array.f__add__quick_choices)) {
            TextView choiceView = (TextView) View.inflate(getActivity(), R.layout.v__add_quick_choice, null);
            choiceView.setText(new String(Character.toChars(Integer.parseInt(choice, 16))));
            choiceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = ((TextView) v).getText().toString();
                    getEventManager().publish(new RequestAddHabitEvent(title, startDate));
                    dismiss();
                }
            });
            choicesView.addView(choiceView);
        }
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
