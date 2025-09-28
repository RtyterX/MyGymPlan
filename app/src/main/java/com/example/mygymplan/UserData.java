package com.example.mygymplan;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


@Entity(tableName = "user")
public class UserData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "email")
    String email;

    @ColumnInfo(name = "bodyType")
    int bodyType;

    @ColumnInfo(name = "pro")
    boolean isPro;


    // ----------- FUTURE IDEAS ------------
    // Height
    // Weight



}