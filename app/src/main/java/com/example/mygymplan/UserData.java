package com.example.mygymplan;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


@Entity(tableName = "user")
public class UserData implements Serializable {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "email")
    String email;

    @ColumnInfo(name = "pro")
    boolean isPro;


    // ---- Constructor ----


    // ---- Getters and Setters ----



}