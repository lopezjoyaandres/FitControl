package com.example.fitcontroluser.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitcontroluser.databases.manager.AccelerometerDAO;
import com.example.fitcontroluser.databases.manager.GyroscopeDAO;
import com.example.fitcontroluser.databases.manager.MovementDAO;
import com.example.fitcontroluser.databases.manager.ProfileDAO;
import com.example.fitcontroluser.databases.manager.RepetitionDAO;
import com.example.fitcontroluser.databases.models.Accelerometer;
import com.example.fitcontroluser.databases.models.Gyroscope;
import com.example.fitcontroluser.databases.models.Movement;
import com.example.fitcontroluser.databases.models.Profile;
import com.example.fitcontroluser.databases.models.Repetition;
import com.facebook.stetho.Stetho;



@Database(entities = {
        Accelerometer.class,
        Gyroscope.class,
        Movement.class,
        Profile.class,
        Repetition.class,
},

        version = 2,
        exportSchema = false)

public abstract class database extends RoomDatabase {
    public static final String DATABASE_NAME = "FitControlUser";
    private Context mContext;
    private static database instance;

    public static database getInstance(final Context context){

       // Stetho.initializeWithDefaults(context);

        if(instance == null){

            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    database.class,
                    DATABASE_NAME
            ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract ProfileDAO getProfileDAO();
    public abstract AccelerometerDAO getAccelerometerDAO();
    public abstract GyroscopeDAO getGyroscopeDAO();
    public abstract MovementDAO getMovementDAO();
    public abstract RepetitionDAO getRepetitionDAO();

}

