package com.example.mygymplan.Entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mygymplan.Enums.ExperienceLevel;

import java.io.Serializable;

@Entity(tableName = "plans")
public class Plan implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "active")
    public Boolean active;

    @ColumnInfo(name = "fixedDays")
    public Boolean fixedDays;

    @ColumnInfo(name = "bodyType")
    public int bodyType;

    @ColumnInfo(name = "level")
    public ExperienceLevel level;

    @ColumnInfo(name = "author")
    public String author;

    @ColumnInfo(name = "createdDate")
    public String createdDate;

    @ColumnInfo(name = "pro")
    public Boolean pro;


}
