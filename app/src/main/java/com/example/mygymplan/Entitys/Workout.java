package com.example.mygymplan.Entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.mygymplan.Enums.WorkoutType;

import java.io.Serializable;
import java.time.DayOfWeek;

@Entity(tableName = "workouts", foreignKeys =
@ForeignKey(entity = Plan.class,
        parentColumns = "id",
        childColumns = "plan_Id",
        onDelete = ForeignKey.CASCADE))

public class Workout implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "type")
    public WorkoutType type;

    @ColumnInfo(name = "sequence")
    public int sequence;

    @ColumnInfo(name = "duration")
    public String duration;

    @ColumnInfo(name = "dayOfWeek")
    public DayOfWeek dayOfWeek;

    @ColumnInfo(name = "lastModified")
    public String lastModified;

    @ColumnInfo(name = "plan_Id")
    public  int plan_Id;

}
