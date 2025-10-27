package com.example.mygymplan.Services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDuration extends AppCompatActivity {

    // Time Maximum and Minimum Values
    int maxTime;
    int minTime;

    public String CalculateDurationTime(Workout workout, List<Exercise> list) {

        // Remove Any Exercise that is not from this Workout
        list.removeIf(item -> item.workout_Id != workout.id);

        for (Exercise item : list) {
            // Calculate Individually by each Exercise inside the Workout
            minTime += ((item.eSets * (item.eReps * 4)) + (item.eSets * item.eRest));
            maxTime += ((item.eSets * (item.eReps * 12)) + (item.eSets * item.eRest));
        }

        // Divide by Minute
        minTime = minTime / 60;
        maxTime = maxTime / 60;

        // Return String
        return minTime + " - " + maxTime + "min";
    }

    public String CalculateDurationTime2(Context context, Workout workout) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        ExerciseDao dao = db.exerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Exercise> allExercises = new ArrayList<>();

                // Remove Any Exercise that is not from this Workout
                allExercises.removeIf(item -> item.workout_Id != workout.id);

                for (Exercise item : allExercises) {
                    // Calculate Individually by each Exercise inside the Workout
                    minTime += ((item.eSets * (item.eReps * 4)) + (item.eSets * item.eRest));
                    maxTime += ((item.eSets * (item.eReps * 12)) + (item.eSets * item.eRest));
                }

                // Divide by Minute
                minTime = minTime / 60;
                maxTime = maxTime / 60;

            }
        }).start();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //
            }
        }, 3000); // 3000 milliseconds = 3 seconds
        // Return String
        return minTime + " - " + maxTime + "min";
    }

}
