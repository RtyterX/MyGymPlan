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

    @ColumnInfo(name = "description")
    public String wDescription;

    //public int wImage;

}
