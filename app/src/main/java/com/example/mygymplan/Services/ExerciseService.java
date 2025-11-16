package com.example.mygymplan.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Database.SavedExerciseDao;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.Enums.WorkoutType;

import android.content.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                SavedExercise savedExercise = new SavedExercise();

                // Access Dao and All Saved Exercises
                SavedExerciseDao dao2 = db.savedExerciseDao();

                // Set SavedExercise Values
                savedExercise.name = exercise.name;
                savedExercise.description = exercise.description;
                savedExercise.type = exercise.type;
                savedExercise.sets = exercise.sets;
                savedExercise.reps = exercise.reps;
                savedExercise.rest = exercise.rest;
                savedExercise.load = exercise.load;
                savedExercise.createdDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"));
                savedExercise.modDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"));
                savedExercise.userCreated = true;
                savedExercise.image = exercise.image;
                savedExercise.video = exercise.video;

                // Create Exercise and Saved Exercise Link
                exercise.savedExercise_Id = savedExercise.id;

                // Save Exercise Pattern
                dao2.insertSavedExercise(savedExercise);

                // Save Exercise in Workout
                dao.insertExercise(exercise);
            }
        }).start();

        db.close();
    }


    // ---------------------------------------------------------------------------------------------------
    public void insertExercise(Context context, Exercise exercise) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        ExerciseDao dao = db.exerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Save Exercise in Workout
                dao.insertExercise(exercise);
            }
        }).start();

        db.close();
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

        db.close();
    }


    // ---------------------------------------------------------------------------------------------------
    public void deleteExercise(Context context, Exercise exercise) {

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

                                savedExercise.name = exercise.name;
                                savedExercise.description = exercise.description;
                                savedExercise.image = exercise.image;
                                savedExercise.sets = exercise.sets;
                                savedExercise.reps = exercise.reps;
                                savedExercise.rest = exercise.rest;
                                savedExercise.load = exercise.load;
                                savedExercise.type = exercise.type;
                                savedExercise.modDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"));
                                secondDao.updateSavedExercise(item);
                            }
                        }
                    }
                }

                // Delete Exercise from Workout
                dao.deleteExercise(exercise);
            }
        }).start();

        db.close();
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
    public Exercise convertExercise(SavedExercise savedExercise, int workoutOrder, int planId, int workoutId) {
        ExerciseService exerciseService = new ExerciseService();
        // --------------------------------------
        newExercise = new Exercise();
        newExercise.name = savedExercise.name;
        newExercise.description = savedExercise.description;
        newExercise.sets = savedExercise.sets;
        newExercise.reps = savedExercise.reps;
        newExercise.rest = savedExercise.rest;
        newExercise.load = savedExercise.load;
        newExercise.type = savedExercise.type;
        newExercise.image = savedExercise.image;
        newExercise.order = workoutOrder;
        newExercise.plan_Id = planId;
        newExercise.workout_Id = workoutId;
        newExercise.savedExercise_Id = savedExercise.id;
        newExercise.lastModified = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"));

        return newExercise;
    }

    // ---------------------------------------------------------------------------------------------------
    public void applyExerciseType(Exercise exercise, String item){
        switch(item)  {
            case "Chest":
                exercise.type = WorkoutType.Chest;
                break;
            case "Back":
                exercise.type = WorkoutType.Back;
                break;
            case "Shoulder":
                exercise.type = WorkoutType.Shoulder;
                break;
            case "Arms":
                exercise.type = WorkoutType.Arm;
                break;
            case "Biceps":
                exercise.type = WorkoutType.Biceps;
                break;
            case "Triceps":
                exercise.type = WorkoutType.Triceps;
                break;
            case "Legs":
                exercise.type = WorkoutType.Leg;
                break;
        }
    }


    // ---------------------------------------------------------------------------------------------------
    public void switchAllExercisesName(Context context, String  language) {
        // switch(item)  ???

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        SavedExerciseDao dao = db.savedExerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                String[] newNames = new String[0];
                /////////// Language String ///////////////
                List<SavedExercise> allExercises = dao.listSavedExercise();
                List<SavedExercise> dbExercises = dao.listSavedExercise();

                // -------------------------------------
                if (!allExercises.isEmpty()) {

                    for (SavedExercise item : allExercises) {
                        if (!item.userCreated) {
                            dbExercises.add(item);
                        }
                    }

                    for (int i = 0; i < dbExercises.size(); i++) {
                        dbExercises.get(i).name = newNames[i];
                        // Update Exercise Name
                        dao.updateSavedExercise(allExercises.get(i));
                    }
                }
            }
        }).start();

        db.close();
    }

}
