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

public class HabitManager {

    private static final String PREF_KEY = "habits";

    private final Context _context;
    private List<Habit> _habits;

    public HabitManager(Context context) {
        _context = context;
    }

    public List<Habit> get() {
        if (_habits == null) {
            _habits = new ArrayList<Habit>();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
            String serialized = prefs.getString(PREF_KEY, null);
            if (serialized != null) {
                _habits.addAll(unserialize(serialized));
            }
        }
        return _habits;
    }

    private List<Habit> unserialize(String serialized) {
        List<Habit> habits = new ArrayList<Habit>();
        try {
            JSONArray habitsJson = new JSONArray(serialized);
            for (int i = 0; i < habitsJson.length(); i += 1) {
                JSONObject habitJson = habitsJson.getJSONObject(i);
                habits.add(new Habit(habitJson.getString("title"), new DateTime(habitJson.getString("start_date"))));
            }
        } catch (JSONException e) {}
        return habits;
    }

    public void add(String title) {
        _habits.add(new Habit(title, DateTime.now()));
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(_context).edit();
        editor.putString(PREF_KEY, serialize(_habits));
        editor.commit();
        _habits = null;
    }

    private String serialize(List<Habit> habits) {
        try {
            JSONArray habitsJson = new JSONArray();
            for (Habit habit: habits) {
                JSONObject habitJson = new JSONObject();
                habitJson.put("title", habit.getTitle());
                habitJson.put("start_date", habit.getStartDate().toString());
                habitsJson.put(habitJson);
            }
            return habitsJson.toString();
        } catch (JSONException e) {
            return null;
        }
    }
}
