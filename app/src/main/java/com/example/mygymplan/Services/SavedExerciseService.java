package com.example.mygymplan.Services;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.SavedExerciseDao;
import com.example.mygymplan.Entitys.SavedExercise;

public class SavedExerciseService extends AppCompatActivity {

    public void ConvertStringToSavedExercises() {
        // Convert All String Exercises to Saved Exercises
        ////////// Only on First Entered the App //////////
    }

    public void insertSavedExercise(Context context, SavedExercise savedExercise) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        SavedExerciseDao dao = db.savedExerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insertSavedExercise(savedExercise);
            }
        }).start();

        db.close();
    }

    public void updateSavedExercise(Context context, SavedExercise savedExercise) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        SavedExerciseDao dao = db.savedExerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.updateSavedExercise(savedExercise);
            }
        }).start();

        db.close();
    }

    public void deleteSavedExercise(Context context, SavedExercise savedExercise) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        SavedExerciseDao dao = db.savedExerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() { dao.deleteSavedExercise(savedExercise);}
        }).start();

        db.close();
    }

}
