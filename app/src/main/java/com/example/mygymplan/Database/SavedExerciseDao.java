package com.example.mygymplan.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.Enums.WorkoutType;

import java.util.List;

@Dao
public interface SavedExerciseDao {

    @Query("SELECT * FROM savedExercises")
    List<SavedExercise> listSavedExercise();

    @Query("SELECT * FROM savedExercises WHERE name LIKE :search")
    List<SavedExercise> searchByName(String search);

    @Insert
    void insertSavedExercise(SavedExercise... savedExercise);

    @Update
    void updateSavedExercise(SavedExercise... savedExercise);

    @Delete
    void deleteSavedExercise(SavedExercise savedExercise);

    @Query("INSERT INTO savedExercises (name,description,sets,reps,rest,load,type,userCreated,createdDate,modDate,image,video) VALUES (:name, :description, :sets, :reps, :rest, :load, :type, :userCreated, :createdDate, :modDate, :image, :video)")
    void rawInsert(String name, String description, int sets, int reps, int rest, int load, WorkoutType type, boolean userCreated, String createdDate, String modDate, String image, String video);

}

