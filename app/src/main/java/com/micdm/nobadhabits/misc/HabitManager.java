package com.micdm.nobadhabits.misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.micdm.nobadhabits.data.Habit;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HabitManager {

    private static final String PREF_KEY = "habits";

    private final Context context;
    private List<Habit> habits;

    public HabitManager(Context context) {
        this.context = context;
    }

    public List<Habit> get() {
        if (habits == null) {
            habits = new ArrayList<>(load());
        }
        return habits;
    }

    public void add(String title, DateTime startDate, boolean isFavorite) {
        habits.add(new Habit(UUID.randomUUID().toString(), title, startDate, isFavorite));
        save(habits);
        habits = null;
    }

    public void update(String id, String title, DateTime startDate, boolean isFavorite) {
        Habit habit = getHabitById(habits, id);
        habit.setTitle(title);
        habit.setStartDate(startDate);
        habit.setIsFavorite(isFavorite);
        save(habits);
        habits = null;
    }

    private Habit getHabitById(List<Habit> habits, String id) {
        for (Habit habit: habits) {
            if (habit.getId().equals(id)) {
                return habit;
            }
        }
        throw new RuntimeException(String.format("cannot find habit with id %s", id));
    }

    public void remove(Habit habit) {
        habits.remove(habit);
        save(habits);
        habits = null;
    }

    private List<Habit> load() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String serialized = prefs.getString(PREF_KEY, null);
        return (serialized == null) ? new ArrayList<Habit>() : unserialize(serialized);
    }

    private void save(List<Habit> habits) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(PREF_KEY, serialize(habits));
        editor.apply();
    }

    private String serialize(List<Habit> habits) {
        try {
            JSONArray habitsJson = new JSONArray();
            for (Habit habit: habits) {
                JSONObject habitJson = new JSONObject();
                habitJson.put("id", habit.getId());
                habitJson.put("title", habit.getTitle());
                habitJson.put("start_date", habit.getStartDate().toString());
                habitJson.put("is_favorite", habit.isFavorite());
                habitsJson.put(habitJson);
            }
            return habitsJson.toString();
        } catch (JSONException e) {
            return null;
        }
    }

    private List<Habit> unserialize(String serialized) {
        List<Habit> habits = new ArrayList<>();
        try {
            JSONArray habitsJson = new JSONArray(serialized);
            for (int i = 0; i < habitsJson.length(); i += 1) {
                JSONObject habitJson = habitsJson.getJSONObject(i);
                String id = habitJson.getString("id");
                String title = habitJson.getString("title");
                DateTime startDate = new DateTime(habitJson.getString("start_date"));
                boolean isFavorite = habitJson.optBoolean("is_favorite", false);
                habits.add(new Habit(id, title, startDate, isFavorite));
            }
        } catch (JSONException e) {}
        return habits;
    }
}
