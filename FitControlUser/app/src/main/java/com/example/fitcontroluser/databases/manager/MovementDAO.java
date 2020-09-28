package com.example.fitcontroluser.databases.manager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitcontroluser.databases.models.Movement;
import com.example.fitcontroluser.databases.models.Profile;
import com.example.fitcontroluser.databases.models.Repetition;

import java.util.List;
@Dao
public interface MovementDAO {
    @Query("SELECT * FROM " + Movement.TABLE_NAME)
    List<Movement> getAll();

    @Query("DELETE FROM " + Movement.TABLE_NAME + " WHERE nameUSER=:user ")
    void deleteMovements(String user);

    @Query("SELECT COUNT(*) FROM "+ Movement.TABLE_NAME+" WHERE nameMovement=:nameMovement AND nameUSER=:nameUser AND date=:date")
    Integer getCount(String nameMovement,String nameUser,String date);


    @Update
    void update(Movement... crewTimes);

    @Insert
    void insert(Movement... crewTimes);


    @Insert
    void insertAll(List<Movement> crewTimes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Movement> crewTimes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Movement crewTime);

    @Delete
    void delete(Movement crewTime);

    @Query("DELETE FROM " + Movement.TABLE_NAME)
    void clearAll();
}
