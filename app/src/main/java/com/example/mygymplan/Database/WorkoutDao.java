package com.example.mygymplan.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mygymplan.Entitys.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Query("SELECT * FROM workouts")
    List<Workout> listWorkouts();

    @Insert
    void insertWorkout(Workout... workouts);

    @Update
    void updateWorkout(Workout... workout);

    @Delete
    void deleteWorkout(Workout workout);

}
