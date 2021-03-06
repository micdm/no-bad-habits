package com.micdm.nobadhabits.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.nobadhabits.data.Habit;

import org.joda.time.DateTime;

public class HabitParcel implements Parcelable {

    public static final Creator<Habit> CREATOR = new Creator<Habit>() {

        public Habit createFromParcel(Parcel in) {
            String id = in.readString();
            String title = in.readString();
            DateTime startDate = new DateTime(in.readString());
            boolean isFavorite = (in.readInt() == 1);
            return new Habit(id, title, startDate, isFavorite);
        }

        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    private final Habit habit;

    public HabitParcel(Habit habit) {
        this.habit = habit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(habit.getId());
        out.writeString(habit.getTitle());
        out.writeString(habit.getStartDate().toString());
        out.writeInt(habit.isFavorite() ? 1 : 0);
    }

    public Habit getHabit() {
        return habit;
    }
}
