package com.example.mygymplan;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlanDao {

    @Query("SELECT * FROM plans")
    List<Plan> listPlans();

    @Insert
    void insertPlan(Plan... plans);

    @Update
    void updatePlan(Plan... plan);

    @Delete
    void deletePlan(Plan plan);

}