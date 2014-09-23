package com.micdm.nobadhabits.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.micdm.nobadhabits.CustomApplication;
import com.micdm.nobadhabits.events.EventManager;
import com.micdm.nobadhabits.events.events.SelectDateEvent;

import org.joda.time.LocalDate;

public class SelectDateFragment extends DialogFragment {

    private static final String INIT_ARG_DATE = "date";

    public static SelectDateFragment getInstance(LocalDate date) {
        SelectDateFragment fragment = new SelectDateFragment();
        Bundle arguments = new Bundle();
        arguments.putString(INIT_ARG_DATE, date.toString());
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LocalDate date = new LocalDate(getArguments().getString(INIT_ARG_DATE));
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                getEventManager().publish(new SelectDateEvent(new LocalDate(year, monthOfYear + 1, dayOfMonth)));
            }
        }, date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        LocalDate today = LocalDate.now();
        dialog.getDatePicker().setMaxDate(today.toDate().getTime());
        return dialog;
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }
}
