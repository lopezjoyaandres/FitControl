package com.example.fitcontroluser.databases.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Movement",primaryKeys = {"nameMovement", "nameUser","date"})

public class Movement {
    public static final String TABLE_NAME = "Movement";



    @NonNull
    @ColumnInfo(name = "nameMovement")
    private String nameMovement;


    @NonNull
    @ColumnInfo(name = "nameUser")
    private String nameUser;


    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "reps")
    private Integer reps;

    @ColumnInfo(name = "repsOK")
    private Integer repsOK;

    public Movement(@NonNull String nameMovement, @NonNull String nameUser, @NonNull String date, Integer reps, Integer repsOK) {
        this.nameMovement = nameMovement;
        this.nameUser = nameUser;
        this.date = date;
        this.reps = reps;
        this.repsOK = repsOK;
    }

    public String getNameMovement() {
        return nameMovement;
    }

    public void setNameMovement(String nameMovement) {
        this.nameMovement = nameMovement;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getRepsOK() {
        return repsOK;
    }

    public void setRepsOK(Integer repsOK) {
        this.repsOK = repsOK;
    }
}
