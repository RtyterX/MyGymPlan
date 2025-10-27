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
import com.example.mygymplan.Database.SavedExerciseDao;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.Entitys.UserData;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Enums.WorkoutType;

import android.content.Context;
import android.content.Intent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExerciseService extends AppCompatActivity {

    Exercise newExercise;
    SavedExercise savedExercise;

    // ---------------------------------------------------------------------------------------------------
    public void createExercise(Context context, Exercise exercise) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        ExerciseDao dao = db.exerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Set Variables
                LocalDate date = LocalDate.now();
                SavedExercise newUserExercise = new SavedExercise();
                List<SavedExercise> allSavedExercises = new ArrayList<>();

                // Access Dao and All Saved Exercises
                SavedExerciseDao dao2 = db.savedExerciseDao();
                allSavedExercises = dao2.listSavedExercise();

                // Set SavedExercise Values
                newUserExercise.name = exercise.eName;
                newUserExercise.description = exercise.eDescription;
                newUserExercise.type = exercise.eType;
                newUserExercise.sets = exercise.eSets;
                newUserExercise.reps = exercise.eReps;
                newUserExercise.rest = exercise.eRest;
                newUserExercise.load = exercise.eLoad;
                newUserExercise.createdDate = date.format(DateTimeFormatter.ofPattern("dd/MM"));
                newUserExercise.userCreated = true;
                newUserExercise.id = allSavedExercises.size() + 1;

                // Create Exercise and Saved Exercise Link
                exercise.savedExercise_Id = newUserExercise.id;

                // Save Exercise Pattern
                dao2.insertSavedExercise(newUserExercise);

                // Save Exercise in Workout
                dao.insertExercise(exercise);
            }
        }).start();

    }


    // ---------------------------------------------------------------------------------------------------
    public void addExercise(Context context, Exercise exercise) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        ExerciseDao dao = db.exerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Save Exercise in Workout
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

        LocalDate date = LocalDate.now();
        savedExercise = new SavedExercise();
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        ExerciseDao dao = db.exerciseDao();
        SavedExerciseDao secondDao = db.savedExerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Exercise> allExercises = dao.listExercise();

                // Reorder Exercises in this Workout
                if (!allExercises.isEmpty()) {
                    for (Exercise item : allExercises) {
                        if (item.plan_Id == exercise.plan_Id) {
                            if (item.workout_Id == exercise.workout_Id) {
                                if (item.order > exercise.order) {
                                    newExercise = item;
                                    newExercise.order -= 1;
                                    saveExercise(context, newExercise);
                                }
                            }
                        }
                    }
                }

                // Search for Saved Exercise
                List<SavedExercise> allSavedExercises = secondDao.listSavedExercise();
                if (!allSavedExercises.isEmpty()) {
                    for (SavedExercise item : allSavedExercises) {
                        if (item.userCreated) {
                            if (item.id == exercise.savedExercise_Id) {
                                // Save Variable Values
                                savedExercise = item;

                                savedExercise.name = exercise.eName;
                                savedExercise.description = exercise.eDescription;
                                savedExercise.sets = exercise.eSets;
                                savedExercise.reps = exercise.eReps;
                                savedExercise.rest = exercise.eRest;
                                savedExercise.load = exercise.eLoad;
                                savedExercise.type = exercise.eType;
                                savedExercise.deleteDate = date.format(DateTimeFormatter.ofPattern("dd/MM"));
                                secondDao.updateSavedExercise(item);
                            }
                        }
                    }
                }

                // Delete Exercise from Workout
                dao.deleteExercise(exercise);
            }
        }).start();

    }

    // ---------------------------------------------------------------------------------------------------
    public void changeExerciseOrder(Context context, Exercise exercise1, Exercise exercise2) {

        int change = exercise1.order;
        exercise1.order = exercise2.order;
        exercise2.order = change;

        saveExercise(context, exercise1);
        saveExercise(context, exercise2);
    }


    // ---------------------------------------------------------------------------------------------------
    public Exercise ConvertExercise(SavedExercise savedExercise, int workoutOrder, int planId, int workoutId) {
        ExerciseService exerciseService = new ExerciseService();
        // --------------------------------------
        LocalDate date = LocalDate.now();
        newExercise = new Exercise();
        newExercise.eName = savedExercise.name;
        newExercise.eDescription = savedExercise.description;
        newExercise.eSets = savedExercise.sets;
        newExercise.eReps = savedExercise.reps;
        newExercise.eRest = savedExercise.rest;
        newExercise.eLoad = savedExercise.load;
        newExercise.eType = savedExercise.type;
        newExercise.order = workoutOrder;
        newExercise.plan_Id = planId;
        newExercise.workout_Id = workoutId;
        newExercise.savedExercise_Id = savedExercise.id;
        newExercise.lastModified = date.format(DateTimeFormatter.ofPattern("dd/MM"));

        return newExercise;
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
