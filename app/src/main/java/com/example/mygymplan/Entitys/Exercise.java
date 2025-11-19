package com.example.mygymplan.Entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.mygymplan.Enums.WorkoutType;

import java.io.Serializable;


@Entity(tableName = "exercises", foreignKeys =
@ForeignKey(entity = Workout.class,
        parentColumns = "id",
        childColumns = "workout_Id",
        onDelete = ForeignKey.CASCADE))
public class Exercise implements Serializable {

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
    public int rest; // Time

    @ColumnInfo(name = "load")
    public int load;

    @ColumnInfo(name = "type")
    public WorkoutType type;

    @ColumnInfo(name = "sequence")
    public int sequence;

    @ColumnInfo(name = "lastModified")
    public String lastModified;

    @ColumnInfo(name = "plan_Id")
    public int plan_Id;

    @ColumnInfo(name = "workout_Id")
    public int workout_Id;

    @ColumnInfo(name = "savedExercise_Id")
    public int savedExercise_Id;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "video")
    public String video;

}