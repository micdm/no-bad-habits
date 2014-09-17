package com.micdm.nobadhabits.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.nobadhabits.data.Habit;

import org.joda.time.DateTime;

public class HabitParcel implements Parcelable {

    public static final Creator<Habit> CREATOR = new Creator<Habit>() {

        public Habit createFromParcel(Parcel in) {
            String title = in.readString();
            DateTime startDate = new DateTime(in.readString());
            return new Habit(title, startDate);
        }

        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    private final Habit _habit;

    public HabitParcel(Habit habit) {
        _habit = habit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(_habit.getTitle());
        out.writeString(_habit.getStartDate().toString());
    }

    public Habit getHabit() {
        return _habit;
    }
}
