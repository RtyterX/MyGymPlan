package com.example.mygymplan.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Entitys.Plan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlanService extends AppCompatActivity {


    // ---------------------------------------------------------------------------------------------------
    public Plan insertPlan(Context context, Plan plan) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts")
                .build();
        PlanDao dao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // If Plan set Active...
                if (plan.active) {
                    // Deactivate any other Active Plan
                    List<Plan> allPlans = dao.listPlans();
                    for (Plan item : allPlans) {
                        if (item.active == true) {
                            item.active = false;
                            dao.updatePlan(item);
                        }
                    }

                    int newPlanId = 0;

                    // Get Higher id value
                    for (int i = 0; i < allPlans.size(); i++) {
                        if (newPlanId < allPlans.get(i).id) {
                            newPlanId = allPlans.get(i).id;
                        }
                    }

                    plan.id = newPlanId + 1;

                    // Save Active Plan Id for later use...
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("activePlanId", (plan.id));
                    editor.apply();
                }

                Log.d("Active Plan Id", "no DB foi salvo como ID: " + plan.id);

                // Insert Plan in Database
                dao.insertPlan(plan);
            }
        }).start();

        db.close();


        Log.d("Active Plan Id", "no DB depois de 3s: " + plan.id);


        return plan;
    }


    // ---------------------------------------------------------------------------------------------------
    public void savePlan(Context context, Plan plan) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        if (plan.active) {
            // Save Active Plan Id for later use...
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("activePlanId", 0);
            editor.apply();
        }

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
                newPlan.name = name;
                newPlan.description = description;
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
    public Plan convertPlan(String name, String description, String author, boolean fixedDays, boolean isActiveOrNot) {

        // Create New Workout DataBase
        Plan convertPlan = new Plan();
        convertPlan.name = name;
        convertPlan.description = description;
        convertPlan.fixedDays = fixedDays;
        convertPlan.pro = false;
        convertPlan.author = author;
        convertPlan.active = isActiveOrNot;
        convertPlan.createdDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

        return convertPlan;
    }

    // ---------------------------------------------------------------------------------------------------
    public void setActivePlan(Context context, Plan plan) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                plan.active = true;

                // Deactivate any other Active Plan
                List<Plan> allPlans = dao.listPlans();
                for (Plan item : allPlans) {
                    if (item.id != plan.id) {
                        if (item.active == true) {
                            item.active = false;
                            dao.updatePlan(item);
                        }
                    }
                }

                plan.active = true;

                // Set Active in Database
                dao.updatePlan(plan);

                // Save Active Plan Id for later use...
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("activePlanId", plan.id);
                editor.apply();

                Log.d("Active Plan Id", "Salvo ID como: " + plan.id);
            }
        }).start();

        db.close();
    }

}
