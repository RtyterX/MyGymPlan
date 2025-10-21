package com.example.mygymplan.Services;

import android.content.Context;

import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Entitys.Plan;

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

}
