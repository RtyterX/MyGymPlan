package com.example.mygymplan;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDataDao {

    @Query("SELECT * FROM user")
    List<UserData> listUserData();

    @Insert
    void insertUser(UserData... UserData);

    @Update
    void updateUser(UserData... UserData);

    @Delete
    void deleteUser(UserData UserData);

}
