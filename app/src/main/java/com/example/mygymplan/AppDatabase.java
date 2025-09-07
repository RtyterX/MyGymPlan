package com.example.mygymplan;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserData.class, Workout.class, Exercise.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract  UserDataDao userDataDao();

    public abstract WorkoutDao workoutDao();

    public abstract ExerciseDao exerciseDao();




}
