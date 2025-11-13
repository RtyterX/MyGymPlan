package com.example.mygymplan.Entitys;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mygymplan.Enums.WorkoutType;

@Entity(tableName = "savedExercises")
public class SavedExercise {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "sets")
    public int sets;

    @ColumnInfo(name = "reps")
    public int reps;

    @ColumnInfo(name = "rest")
    public int rest;

    @ColumnInfo(name = "load")
    public int load;

    @ColumnInfo(name = "type")
    public WorkoutType type;

    @ColumnInfo(name = "userCreated")
    public boolean userCreated;


    @ColumnInfo(name = "createdDate")
    public String createdDate;

    @ColumnInfo(name = "modDate")
    public String modDate;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "video")
    public String video;

}