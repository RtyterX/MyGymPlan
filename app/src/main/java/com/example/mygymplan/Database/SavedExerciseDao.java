package com.example.mygymplan.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.SavedExercise;

import java.util.List;

@Dao
public interface SavedExerciseDao {

    @Query("SELECT * FROM savedExercises")
    List<SavedExercise> listSavedExercise();

    @Insert
    void insertSavedExercise(SavedExercise... savedExercises);

    @Update
    void updateSavedExercise(SavedExercise... savedExercise);

    @Delete
    void deleteSavedExercise(SavedExercise savedExercise);

}
