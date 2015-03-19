package com.micdm.nobadhabits.misc;

import android.content.Context;

import com.micdm.nobadhabits.R;

public class DurationTextBuilder {

    public static String build(Context context, int days) {
        if (days == 0) {
            return context.getString(R.string.f__habits__text_duration_first_day);
        }
        return context.getString(R.string.f__habits__text_duration, days, context.getResources().getQuantityString(R.plurals.f__habits__text_duration_unit, days));
    }
}
