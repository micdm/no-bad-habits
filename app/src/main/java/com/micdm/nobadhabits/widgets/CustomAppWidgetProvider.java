package com.micdm.nobadhabits.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.micdm.nobadhabits.R;
import com.micdm.nobadhabits.activities.MainActivity;
import com.micdm.nobadhabits.data.Habit;
import com.micdm.nobadhabits.misc.DurationTextBuilder;
import com.micdm.nobadhabits.misc.HabitManager;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomAppWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        List<Habit> habits = new HabitManager(context).get();
        Habit habit = getMostSuitableHabit(habits);
        String duration = getDuration(context, habit);
        for (int id: ids) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.w__main);
            if (habit == null) {
                views.setViewVisibility(R.id.w__main__text_container, View.GONE);
                views.setViewVisibility(R.id.w__main__icon, View.VISIBLE);
            } else {
                views.setViewVisibility(R.id.w__main__icon, View.GONE);
                views.setTextViewText(R.id.w__main__title, habit.getTitle());
                views.setTextViewText(R.id.w__main__duration, duration);
                views.setViewVisibility(R.id.w__main__text_container, View.VISIBLE);
            }
            views.setOnClickPendingIntent(R.id.w__main__container, getPendingIntent(context));
            manager.updateAppWidget(id, views);
        }
    }

    private Habit getMostSuitableHabit(List<Habit> habits) {
        if (habits.size() == 0) {
            return null;
        }
        Collections.sort(habits, new Comparator<Habit>() {
            @Override
            public int compare(Habit a, Habit b) {
                return a.getStartDate().compareTo(b.getStartDate());
            }
        });
        return habits.get(0);
    }

    private String getDuration(Context context, Habit habit) {
        if (habit == null) {
            return null;
        }
        int days = Days.daysBetween(habit.getStartDate(), DateTime.now()).getDays();
        return DurationTextBuilder.build(context, days);
    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}
