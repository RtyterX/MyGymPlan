package com.example.mygymplan;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Query("SELECT * FROM exercises")
    List<Exercise> listExercise();

    @Insert
    void insertExercise(Exercise... exercises);

    @Update
    void updateExercise(Exercise... exercise);

    @Delete
    void deleteExercise(Exercise exercise);
}
