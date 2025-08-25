package com.example.mygymplan;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "workouts")
public class Workout implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String wName;
    //public int wImage;
    //public String wDescription;
    //public WorkoutType[] types;
    //public ArrayList<Exercise> wExercises;
    //public Boolean active;


}
