package com.example.mygymplan.Services;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Workout;

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
            minTime += ((item.sets * (item.reps * 4)) + (item.sets * item.rest));
            maxTime += ((item.sets * (item.reps * 12)) + (item.sets * item.rest));
        }

        // Divide by Minute
        minTime = minTime / 60;
        maxTime = maxTime / 60;

        // Return String
        return minTime + " - " + maxTime + "min";
    }

}
