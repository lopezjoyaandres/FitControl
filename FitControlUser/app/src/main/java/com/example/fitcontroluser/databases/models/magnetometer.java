package com.example.fitcontroluser.databases.models;

public class magnetometer {
    private long time;
    private float x;
    private float y;
    private float z;
    private Integer task;

    public magnetometer(long time, float x, float y, float z, Integer task) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
        this.task = task;
    }

    public float getTime() {
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
}
