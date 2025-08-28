package com.example.mygymplan;

import android.provider.MediaStore;
import android.text.Editable;
import android.widget.EditText;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "exercises", foreignKeys =
@ForeignKey(entity = Workout.class,
        parentColumns = "id",
        childColumns = "workout_Id"))
public class Exercise implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String eName;

    @ColumnInfo(name = "description")
    public String eDescription;

    @ColumnInfo(name = "sets")
    public int eSets;

    @ColumnInfo(name = "reps")
    public int eReps;

    @ColumnInfo(name = "rest")
    public int eRest; // Time

    @ColumnInfo(name = "load")
    public int eLoad;


    // public WorkoutType eType;    // Later
    // public int eImage;           // Later

    @ColumnInfo(name = "workout_Id")
    public int workout_Id;



}