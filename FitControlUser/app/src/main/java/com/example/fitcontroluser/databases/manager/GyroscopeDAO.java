package com.example.fitcontroluser.databases.manager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitcontroluser.databases.models.Accelerometer;
import com.example.fitcontroluser.databases.models.Gyroscope;

import java.util.List;
@Dao
public interface GyroscopeDAO {
    @Query("SELECT * FROM " + Gyroscope.TABLE_NAME)
    List<Gyroscope> getAll();

    @Query("SELECT * FROM "+ Gyroscope.TABLE_NAME + " WHERE task=:i")
    List<Gyroscope> getReps(Integer i);

    @Update
    void update(Gyroscope... crewTimes);

    @Insert
    void insert(Gyroscope... crewTimes);

    @Insert
    void insertAll(List<Gyroscope> crewTimes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Gyroscope> crewTimes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Gyroscope crewTime);

    @Delete
    void delete(Gyroscope crewTime);

    @Query("DELETE FROM " + Gyroscope.TABLE_NAME)
    void clearAll();
}
