package com.example.mygymplan.Services;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.SavedWorkoutDao;
import com.example.mygymplan.Entitys.SavedWorkout;

public class SavedWorkoutService extends AppCompatActivity {

        public void insertSavedWorkout(Context context, SavedWorkout savedWorkout) {

            AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
            SavedWorkoutDao dao = db.savedWorkoutDao();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    dao.insertSavedWorkout(savedWorkout);
                }
            }).start();

            db.close();
        }

        public void updateSavedWorkout(Context context, SavedWorkout savedWorkout) {

            AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
            SavedWorkoutDao dao = db.savedWorkoutDao();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    dao.updateSavedWorkout(savedWorkout);
                }
            }).start();

            db.close();
        }

        public void deleteSavedWorkout(Context context, SavedWorkout savedWorkout) {

            AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
            SavedWorkoutDao dao = db.savedWorkoutDao();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    dao.deleteSavedWorkout(savedWorkout);
                }
            }).start();

            db.close();
        }
}
