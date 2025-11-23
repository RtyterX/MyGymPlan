package com.example.mygymplan.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Enums.ExperienceLevel;
import com.example.mygymplan.Enums.WorkoutType;

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

    @Query("INSERT INTO plans (name, description, active, fixedDays, bodyType, level, author, userCreated, createdDate, pro) VALUES (:name, :description, :active, :fixedDays, :type, :level, :author, :userCreated, :createdDate, :pro)")
    void rawInsert(String name, String description, boolean active, boolean fixedDays, int type, ExperienceLevel level, String author, boolean userCreated, String createdDate, boolean pro);


}