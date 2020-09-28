package com.example.fitcontroluser.databases.manager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitcontroluser.databases.models.Profile;

import java.util.List;

@Dao
public interface ProfileDAO {
    @Query("SELECT * FROM " + Profile.TABLE_NAME)
    List<Profile> getAll();

    @Query("SELECT * FROM " + Profile.TABLE_NAME + " WHERE name LIKE :id")
    Profile getMemberById(String id);

    @Update
    void update(Profile... crewTimes);

    @Insert
    void insert(Profile... crewTimes);

    @Insert
    void insertAll(List<Profile> crewTimes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Profile> crewTimes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Profile crewTime);

    @Delete
    void delete(Profile crewTime);

    @Query("DELETE FROM " + Profile.TABLE_NAME)
    void clearAll();
}
