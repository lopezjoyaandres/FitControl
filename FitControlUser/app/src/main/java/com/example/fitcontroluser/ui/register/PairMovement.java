package com.example.fitcontroluser.ui.register;

public class PairMovement {
    private int day;
    private int reps;

    public PairMovement(int day, int reps) {
        this.day = day;
        this.reps = reps;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
