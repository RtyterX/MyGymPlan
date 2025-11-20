package com.example.mygymplan.Services;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.SavedWorkoutDao;
import com.example.mygymplan.Entitys.SavedWorkout;

public class SavedWorkoutService extends AppCompatActivity {

        public void insertSavedWorkout(Context context, SavedWorkout savedWorkout) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                    SavedWorkoutDao dao = db.savedWorkoutDao();

                    dao.insertSavedWorkout(savedWorkout);

                    db.close();
                }
            }).start();
        }

        public void updateSavedWorkout(Context context, SavedWorkout savedWorkout) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                    SavedWorkoutDao dao = db.savedWorkoutDao();

                    dao.updateSavedWorkout(savedWorkout);

                    db.close();
                }
            }).start();
        }

        public void deleteSavedWorkout(Context context, SavedWorkout savedWorkout) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                    SavedWorkoutDao dao = db.savedWorkoutDao();

                    dao.deleteSavedWorkout(savedWorkout);

                    db.close();
                }
            }).start();
        }
}
