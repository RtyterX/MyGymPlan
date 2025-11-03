package com.example.mygymplan.Entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.mygymplan.Enums.WorkoutType;

import java.io.Serializable;
import java.util.Calendar;


@Entity(tableName = "exercises", foreignKeys =
@ForeignKey(entity = Workout.class,
        parentColumns = "id",
        childColumns = "workout_Id",
        onDelete = ForeignKey.CASCADE))
public class Exercise implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String eName;

    @ColumnInfo(name = "description")
    public String eDescription;

    //@ColumnInfo(name = "image")
    // public int eImage;           // Later

    @ColumnInfo(name = "sets")
    public int eSets;

    @ColumnInfo(name = "reps")
    public int eReps;

    @ColumnInfo(name = "rest")
    public int eRest; // Time

    @ColumnInfo(name = "load")
    public int eLoad;

    @ColumnInfo(name = "type")
    public WorkoutType eType;    // Enum

    @ColumnInfo(name = "order")
    public int order;

    @ColumnInfo(name = "lastModified")
    public String lastModified;

    @ColumnInfo(name = "plan_Id")
    public int plan_Id;

    @ColumnInfo(name = "workout_Id")
    public int workout_Id;

    @ColumnInfo(name = "savedExercise_Id")
    public int savedExercise_Id;

}