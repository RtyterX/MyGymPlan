package com.example.mygymplan.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Enums.WorkoutType;
import android.content.Context;

public class WorkoutService extends AppCompatActivity {


    // ---------------------------------------------------------------------------------------------------
    public void addExercise(Context context, Workout workout) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insertWorkout(workout);
            }
        }).start();

    }


    // ---------------------------------------------------------------------------------------------------
    public void saveWorkout(Context context, Workout workout) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.updateWorkout(workout);
            }
        }).start();

    }


    // ---------------------------------------------------------------------------------------------------
    public void deleteWorkout(Context context, Workout workout) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                dao.deleteWorkout(workout);
            }
        }).start();

    }

    // ---------------------------------------------------------------------------------------------------
    public void changeWorkoutOrder(Context context, Workout workout1, Workout workout2) {

        int change = workout1.order;
        workout1.order = workout2.order;
        workout2.order = change;

        saveWorkout(context, workout1);
        saveWorkout(context, workout2);
    }


    // ---------------------------------------------------------------------------------------------------
    public WorkoutType ApplyWorkoutType(Workout workout, String item){
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
