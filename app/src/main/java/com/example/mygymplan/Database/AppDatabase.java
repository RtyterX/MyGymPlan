package com.example.mygymplan.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.Entitys.SavedWorkout;
import com.example.mygymplan.Entitys.Workout;

@Database(entities = {Plan.class, Workout.class, SavedWorkout.class, Exercise.class, SavedExercise.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract  PlanDao planDao();

    public abstract WorkoutDao workoutDao();

    public abstract ExerciseDao exerciseDao();

    public abstract SavedExerciseDao savedExerciseDao();

    public abstract SavedWorkoutDao savedWorkoutDao();

}
