package com.example.mygymplan.Entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity(tableName = "plans")
public class Plan implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String planName;

    @ColumnInfo(name = "description")
    public String planDescription;

    @ColumnInfo(name = "author")
    public String author;

    @ColumnInfo(name = "createdDate")
    public String createdDate;

    @ColumnInfo(name = "active")
    public Boolean active;

    @ColumnInfo(name = "pro")
    public Boolean pro;


}
