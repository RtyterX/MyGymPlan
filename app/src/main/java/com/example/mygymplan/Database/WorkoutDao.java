package com.example.mygymplan.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Enums.WorkoutType;

import java.time.DayOfWeek;
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

    @Query("INSERT INTO workouts (name,description,type,sequence,duration,dayOfWeek,lastModified,plan_Id,editable) VALUES (:name, :description, :type, :sequence, :duration, :dayOfWeek, :lastModified, :plan_Id, :editable)")
    void rawInsert(String name, String description, WorkoutType type, int sequence, String duration, DayOfWeek dayOfWeek, String lastModified, int plan_Id, boolean editable);


}
