package com.example.fitcontroluser.databases.manager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitcontroluser.databases.models.Accelerometer;


import java.util.List;
@Dao
public interface AccelerometerDAO {
    @Query("SELECT * FROM " + Accelerometer.TABLE_NAME)
    List<Accelerometer> getAll();

    @Query("SELECT COUNT(*) FROM "+ Accelerometer.TABLE_NAME + " GROUP BY task")
    Integer getNumberofReps();

    @Query("SELECT * FROM "+ Accelerometer.TABLE_NAME + " WHERE task=:i")
    List<Accelerometer> getReps(Integer i);

    @Update
    void update(Accelerometer... crewTimes);

    @Insert
    void insert(Accelerometer... crewTimes);

    @Insert
    void insertAll(List<Accelerometer> crewTimes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Accelerometer> crewTimes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Accelerometer crewTime);

    @Delete
    void delete(Accelerometer crewTime);

    @Query("DELETE FROM " + Accelerometer.TABLE_NAME)
    void clearAll();
}
