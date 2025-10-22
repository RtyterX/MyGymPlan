package com.example.mygymplan.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.example.mygymplan.Activitys.ExerciseActivity;
import com.example.mygymplan.Activitys.MainActivity;
import com.example.mygymplan.Activitys.WelcomeActivity;
import com.example.mygymplan.Activitys.WorkoutActivity;
import com.example.mygymplan.Adapters.WorkoutRVAdapter;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.UserData;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Enums.WorkoutType;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class ExerciseService extends AppCompatActivity {

    List<Exercise> newlist;

    // ---------------------------------------------------------------------------------------------------
    public void addExercise(Context context, Exercise exercise) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        ExerciseDao dao = db.exerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insertExercise(exercise);
            }
        }).start();

    }


    // ---------------------------------------------------------------------------------------------------
    public void saveExercise(Context context, Exercise exercise) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        ExerciseDao dao = db.exerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.updateExercise(exercise);
            }
        }).start();

    }


    // ---------------------------------------------------------------------------------------------------
    public void deleteExercise(Context context, Exercise exercise) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        ExerciseDao dao = db.exerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                dao.deleteExercise(exercise);
            }
        }).start();

    }

    public void changeExerciseOrder(Context context, Exercise exercise1, Exercise exercise2) {

        int change = exercise1.order;
        exercise1.order = exercise2.order;
        exercise2.order = change;

        saveExercise(context, exercise1);
        saveExercise(context, exercise2);
    }



    // ---------------------------------------------------------------------------------------------------
    public WorkoutType ApplyExerciseType(Exercise exercise, String item){
        switch(item)  {
            case "Chest":
                exercise.eType = WorkoutType.Chest;
                break;
            case "Back":
                exercise.eType = WorkoutType.Back;
                break;
            case "Shoulder":
                exercise.eType = WorkoutType.Shoulder;
                break;
            case "Arms":
                exercise.eType = WorkoutType.Arms;
                break;
            case "Biceps":
                exercise.eType = WorkoutType.Biceps;
                break;
            case "Triceps":
                exercise.eType = WorkoutType.Triceps;
                break;
            case "Legs":
                exercise.eType = WorkoutType.Legs;
                break;
        }

        return exercise.eType;
    }

}
