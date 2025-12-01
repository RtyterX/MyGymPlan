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
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();
                SavedExerciseDao savedDao = db.savedExerciseDao();

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


                // Get Saved Exercise ID
                int id = 0;
                List<SavedExercise> allSavedExercises = savedDao.listSavedExercise();
                for (SavedExercise e : allSavedExercises) {
                    if (e.id > id) {
                        id = e.id;
                    }
                }
                savedExercise.id = id + 1;
                exercise.savedExercise_Id = savedExercise.id;

                exercise.userCreated = true;

                // Save Exercise saved Pattern
                dao2.insertSavedExercise(savedExercise);

                // Save Exercise in Workout
                dao.insertExercise(exercise);

                db.close();
            }
        }).start();
    }


    // ---------------------------------------------------------------------------------------------------
    public void insertExercise(Context context, Exercise exercise) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();

                // Save Exercise in Workout
                dao.insertExercise(exercise);

                db.close();
            }
        }).start();
    }


    // ---------------------------------------------------------------------------------------------------
    public void saveExercise(Context context, Exercise exercise) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();

                dao.updateExercise(exercise);

                db.close();
            }
        }).start();
    }


    // ---------------------------------------------------------------------------------------------------
    public void deleteExercise(Context context, Exercise exercise) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                savedExercise = new SavedExercise();
                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();
                SavedExerciseDao secondDao = db.savedExerciseDao();

                List<Exercise> allExercises = dao.listExercises();

                // Reorder Exercises in this Workout
                if (!allExercises.isEmpty()) {
                    for (Exercise item : allExercises) {
                        if (item.plan_Id == exercise.plan_Id) {
                            if (item.workout_Id == exercise.workout_Id) {
                                if (item.sequence > exercise.sequence) {
                                    item.sequence--;
                                    dao.updateExercise(item);
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

                db.close();
            }
        }).start();
    }

    // ---------------------------------------------------------------------------------------------------
    public void changeExerciseOrder(Context context, Exercise exercise1, Exercise exercise2) {

        int change = exercise1.sequence;
        exercise1.sequence = exercise2.sequence;
        exercise2.sequence = change;

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
        newExercise.sequence = workoutOrder;
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

        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                SavedExerciseDao dao = db.savedExerciseDao();

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

                db.close();
            }
        }).start();
    }

}
