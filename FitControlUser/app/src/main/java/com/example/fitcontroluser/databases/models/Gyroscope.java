package com.example.fitcontroluser.databases.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Gyroscope")
public class Gyroscope {
    public static final String TABLE_NAME = "Gyroscope";


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "x")
    private float x;

    @ColumnInfo(name = "y")
    private float y;

    @ColumnInfo(name = "z")
    private float z;

    @ColumnInfo(name = "task")
    private Integer task;

    public Gyroscope(long time, float x, float y, float z, Integer task) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
        this.task = task;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "Gyroscope{" +
                "time=" + time +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", task=" + task +
                '}';
    }
}
