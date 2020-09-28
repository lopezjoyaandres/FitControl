package com.example.fitcontroluser.databases.manager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitcontroluser.databases.models.Profile;
import com.example.fitcontroluser.databases.models.Repetition;

import java.util.List;
@Dao
public interface RepetitionDAO {
    @Query("SELECT * FROM " + Repetition.TABLE_NAME)
    List<Repetition> getAll();

    @Query("SELECT COUNT(*) FROM " + Repetition.TABLE_NAME + " WHERE name=:user AND nameMovement=:movimiento AND task=1")
    Integer getSets(String user,String movimiento );

    @Query("SELECT COUNT(*) FROM " + Repetition.TABLE_NAME + " WHERE name=:user AND nameMovement=:movimiento AND date=:date AND task=1")
    Integer getSets(String user,String date,String movimiento );

    @Query("SELECT COUNT(*) FROM " + Repetition.TABLE_NAME + " WHERE name=:user AND nameMovement=:movimiento AND repOK=:form")
    Integer getRepetitionsByForm(String user,String movimiento ,int form);

    @Query("SELECT COUNT(*) FROM " + Repetition.TABLE_NAME + " WHERE name=:user AND nameMovement=:movimiento AND repOK=:form AND date=:fecha")
    Integer getRepetitionsByForm(String user,String movimiento ,String fecha,int form);

    @Query("SELECT COUNT(*) FROM " + Repetition.TABLE_NAME + " WHERE name=:user AND repOK=:form")
    Integer getAllRepetitions(String user,int form);

    @Query("SELECT COUNT(*) FROM "+ Repetition.TABLE_NAME+" WHERE name=:nameUser AND date=:date")
    Integer getCountReps(String nameUser,String date);


    @Query("SELECT COUNT(*) FROM " + Repetition.TABLE_NAME + " WHERE name=:user AND nameMovement=:movement")
    Integer getAllRepetitions(String user,String movement);

    @Query("SELECT * FROM " + Repetition.TABLE_NAME + " WHERE name=:user AND date=:date")
    List<Repetition> getAllRepetitionsBy(String user,String date);

    @Query("SELECT * FROM " + Repetition.TABLE_NAME + " WHERE name=:user AND date=:date AND nameMovement=:movement")
    List<Repetition> getAllRepetitionsBy(String user,String date,String movement);

    @Query("SELECT * FROM " + Repetition.TABLE_NAME + " WHERE name=:user AND nameMovement=:movement")
    List<Repetition> getAllRepetitionsByM(String user,String movement);

    @Query("DELETE FROM " + Repetition.TABLE_NAME + " WHERE name=:user ")
    void deleteReps(String user);


    @Update
    void update(Repetition... crewTimes);

    @Insert
    void insert(Repetition... crewTimes);

    @Insert
    void insertAll(List<Repetition> crewTimes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Repetition> crewTimes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Repetition crewTime);

    @Delete
    void delete(Repetition crewTime);

    @Query("DELETE FROM " + Repetition.TABLE_NAME)
    void clearAll();
}
