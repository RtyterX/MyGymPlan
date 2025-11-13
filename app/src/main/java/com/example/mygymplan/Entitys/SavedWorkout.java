package com.example.mygymplan.Entitys;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mygymplan.Enums.WorkoutType;

import java.io.Serializable;

@Entity(tableName = "savedWorkouts")
public class SavedWorkout implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "type")
    public WorkoutType type;

    @ColumnInfo(name = "createdDate")
    public String createdDate;

    @ColumnInfo(name = "lastModified")
    public String lastModified;

    @ColumnInfo(name = "exercisesList")
    public String exercisesList;
}
