package com.example.mygymplan.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Enums.WorkoutType;
import android.content.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutService extends AppCompatActivity {


    // ---------------------------------------------------------------------------------------------------
    public List<Workout> listAllWorkout(Context context) {

        List<Workout> allWorkouts;

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        allWorkouts = dao.listWorkouts();

        db.close();

        return allWorkouts;
    }

    // ---------------------------------------------------------------------------------------------------
    public void insertWorkout(Context context, Workout workout) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Workout> allWorkouts = new ArrayList<>();
                allWorkouts = dao.listWorkouts();

                workout.order = allWorkouts.size();
                dao.insertWorkout(workout);
            }
        }).start();

        db.close();
    }


    // ---------------------------------------------------------------------------------------------------
    public void updateWorkout(Context context, Workout workout) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.updateWorkout(workout);
            }
        }).start();

        db.close();
    }


    // ---------------------------------------------------------------------------------------------------
    public void deleteWorkout(Context context, Workout workout) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Delete Workout
                dao.deleteWorkout(workout);
            }
        }).start();

        db.close();
    }

    // ---------------------------------------------------------------------------------------------------
    public void changeWorkoutOrder(Context context, Workout workout1, Workout workout2) {

        int change = workout1.order;
        workout1.order = workout2.order;
        workout2.order = change;

        updateWorkout(context, workout1);
        updateWorkout(context, workout2);
    }


    // ---------------------------------------------------------------------------------------------------
    public Workout converterWorkout(String name, String description, WorkoutType type, int planId) {
        // Create New Workout DataBase
        LocalDate date = LocalDate.now();
        Workout newWorkout = new Workout();
        newWorkout.plan_Id = planId;
        newWorkout.wName = name;
        newWorkout.wDescription = description;
        newWorkout.wType = Objects.requireNonNullElse(type, WorkoutType.NA);
        newWorkout.lastModified = date.format(DateTimeFormatter.ofPattern("dd/MM"));

        return newWorkout;
    }


    // ---------------------------------------------------------------------------------------------------
    public WorkoutType applyWorkoutType(Workout workout, String item){
        switch(item)  {
            case "Chest":
                workout.wType = WorkoutType.Chest;
                break;
            case "Back":
                workout.wType = WorkoutType.Back;
                break;
            case "Shoulder":
                workout.wType = WorkoutType.Shoulder;
                break;
            case "Arms":
                workout.wType = WorkoutType.Arms;
                break;
            case "Biceps":
                workout.wType = WorkoutType.Biceps;
                break;
            case "Triceps":
                workout.wType = WorkoutType.Triceps;
                break;
            case "Legs":
                workout.wType = WorkoutType.Legs;
                break;
        }

        return workout.wType;
    }

}
