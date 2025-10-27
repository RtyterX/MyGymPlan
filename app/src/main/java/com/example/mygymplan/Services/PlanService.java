package com.example.mygymplan.Services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Entitys.Plan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlanService {

    // ---------------------------------------------------------------------------------------------------
    public void addPlan(Context context, Plan plan) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insertPlan(plan);
            }
        }).start();

    }


    // ---------------------------------------------------------------------------------------------------
    public void savePlan(Context context, Plan plan) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.updatePlan(plan);
            }
        }).start();

    }


    // ---------------------------------------------------------------------------------------------------
    public void deletePlan(Context context, Plan plan) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                dao.deletePlan(plan);
            }
        }).start();

    }

    public void CreateNewPlan(String name, String description, boolean isActiveOrNot, Context context, String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Get local Time Date
                LocalDate date = LocalDate.now();

                // Create New Workout DataBase
                Plan newPlan = new Plan();
                newPlan.planName = name;
                newPlan.planDescription = description;
                newPlan.pro = false;
                newPlan.author = username;
                newPlan.active = isActiveOrNot;
                newPlan.createdDate = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

                // Insert Plan in Database
                addPlan(context, newPlan);

            }
        }).start();

    }

    public Plan CovertPlan(String name, String description, String author, boolean isActiveOrNot) {
        // Get local Time Date
        LocalDate date = LocalDate.now();

        // Create New Workout DataBase
        Plan newPlan = new Plan();
        newPlan.planName = name;
        newPlan.planDescription = description;
        newPlan.pro = false;
        newPlan.author = author;
        newPlan.active = isActiveOrNot;
        newPlan.createdDate = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

        return newPlan;
    }

    // --------------------------------------------------
    // ------------------ Active Plan -------------------
    // --------------------------------------------------
    public void ActivePlan(Context context, Plan plan) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        // Variables
        Plan deactivePlan = new Plan();
        List<Plan> planList = new ArrayList<>();

        // Access Database
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        // List All Plans from Database
        planList = dao.listPlans();

        // Select only the Last Active Plan
        for (Plan item : planList) {
            if (item.active == true) {
                deactivePlan = item;
            }
        }
    }

}
