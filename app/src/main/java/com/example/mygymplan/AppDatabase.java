package com.example.mygymplan;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Exercise.class, Workout.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExerciseDao exerciseDao();
    public abstract WorkoutDao workoutDao();

}
