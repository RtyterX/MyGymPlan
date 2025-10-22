package com.example.mygymplan.Services;

import android.content.Context;

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
