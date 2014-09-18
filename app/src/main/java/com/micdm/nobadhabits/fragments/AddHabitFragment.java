package com.micdm.nobadhabits.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.micdm.nobadhabits.CustomApplication;
import com.micdm.nobadhabits.R;
import com.micdm.nobadhabits.events.EventManager;
import com.micdm.nobadhabits.events.events.RequestAddHabitEvent;

public class AddHabitFragment extends DialogFragment {

    private TextView _titleView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(setupView());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = _titleView.getText().toString();
                if (title.length() != 0) {
                    getEventManager().publish(new RequestAddHabitEvent(title));
                }
            }
        });
        return builder.create();
    }

    private View setupView() {
        View view = View.inflate(getActivity(), R.layout.f__add, null);
        _titleView = (TextView) view.findViewById(R.id.f__add__title);
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
                    _titleView.setText(((TextView) v).getText());
                }
            });
            choicesView.addView(choiceView);
        }
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }
}
