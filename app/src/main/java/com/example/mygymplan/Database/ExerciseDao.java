package com.example.mygymplan.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Enums.WorkoutType;

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

    @Query("SELECT * FROM exercises WHERE name LIKE :search")
    List<Exercise> listByName(String search);

    @Query("SELECT * FROM exercises WHERE type LIKE :searchType")
    List<Exercise> listByType(WorkoutType searchType);

    @Query("INSERT INTO exercises (name,description,sets,reps,rest,load,type,sequence,lastModified,plan_Id,workout_Id,savedExercise_Id,image,video) VALUES (:name, :description, :sets, :reps, :rest, :load, :type, :sequence, :lastModified, :plan_Id, :workout_Id, :savedExercise_Id, :image, :video)")
    void rawInsert(String name, String description, int sets, int reps, int rest, int load, WorkoutType type, int sequence, String lastModified, int plan_Id, int workout_Id, int savedExercise_Id, String image, String video);

}
