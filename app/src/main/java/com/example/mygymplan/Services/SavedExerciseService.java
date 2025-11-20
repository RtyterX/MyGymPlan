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
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                SavedExerciseDao dao = db.savedExerciseDao();

                dao.insertSavedExercise(savedExercise);

                db.close();
            }
        }).start();
    }

    public void updateSavedExercise(Context context, SavedExercise savedExercise) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                SavedExerciseDao dao = db.savedExerciseDao();

                dao.updateSavedExercise(savedExercise);

                db.close();
            }
        }).start();
    }

    public void deleteSavedExercise(Context context, SavedExercise savedExercise) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                SavedExerciseDao dao = db.savedExerciseDao();

                dao.deleteSavedExercise(savedExercise);

                db.close();
            }
        }).start();
    }

}
