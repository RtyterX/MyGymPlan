package com.example.mygymplan.Services;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.Workout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.scheduling.WorkQueueKt;

public class PlanService extends AppCompatActivity {

    // ---------------------------------------------------------------------------------------------------
    public void getActivePlan(Context context, Plan plan) {

        Plan activePlan = new Plan();

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Plan> allPlans = dao.listPlans();

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Plan item : allPlans) {
                            if (item.active == true) {
                                //activePlan = item;
                            }
                        }
                    }
                });
            }
        }).start();

        db.close();
    }

    // ---------------------------------------------------------------------------------------------------
    public void insertPlan(Context context, Plan plan) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insertPlan(plan);
            }
        }).start();

        db.close();
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

        db.close();
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

        db.close();
    }

    // ---------------------------------------------------------------------------------------------------
    public void createNewPlan(String name, String description, boolean isActiveOrNot, Context context, String username) {
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
                insertPlan(context, newPlan);

            }
        }).start();

    }

    // ---------------------------------------------------------------------------------------------------
    public Plan convertPlan(String name, String description, String author, boolean isActiveOrNot) {
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

    // ---------------------------------------------------------------------------------------------------
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
