package com.example.mygymplan.Entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "user")
public class UserData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "bodyType")
    public int bodyType;

    @ColumnInfo(name = "pro")
    public boolean isPro;


    // ----------- FUTURE IDEAS ------------
    // Height
    // Weight



}