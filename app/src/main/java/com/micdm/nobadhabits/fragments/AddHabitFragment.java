package com.micdm.nobadhabits.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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
        _titleView = (TextView) View.inflate(getActivity(), R.layout.f__add, null);
        return _titleView;
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }
}
