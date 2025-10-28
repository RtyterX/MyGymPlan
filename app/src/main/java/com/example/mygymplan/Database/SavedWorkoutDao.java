package com.example.mygymplan.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.Entitys.SavedWorkout;

import java.util.List;

@Dao
public interface SavedWorkoutDao {
    @Query("SELECT * FROM savedWorkouts")
    List<SavedWorkout> listSavedWorkouts();

    @Insert
    void insertSavedWorkout(SavedWorkout... savedWorkout);

    @Update
    void updateSavedWorkout(SavedWorkout... savedWorkout);

    @Delete
    void deleteSavedWorkout(SavedWorkout savedWorkout);

}
