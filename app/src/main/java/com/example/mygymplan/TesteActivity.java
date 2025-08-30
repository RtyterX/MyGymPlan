package com.example.mygymplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.List;

public class TesteActivity extends AppCompatActivity {

    Workout thisWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teste);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button createNewExercise = findViewById(R.id.CreateNewExercise1);
        Button backButton = findViewById(R.id.BackTestButton);

        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");


        createNewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Exercise newExercise = new Exercise();
                newExercise.eName = "New Exercise";

                Intent intent = new Intent(TesteActivity.this, ShowExerciseActivity.class);
                intent.putExtra("SelectedExercise", newExercise);

                startActivity(intent);

            }
        });

        // Go Back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TesteActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }


    public void show(View view) {

        LinearLayout lnl = findViewById(R.id.lnl);
        lnl.removeAllViews();

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();

                List<Exercise> list = dao.listExercise();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Exercise e : list) {
                            if (thisWorkout.id == e.workout_Id) {
                                TextView t = new TextView(TesteActivity.this);
                                t.setText(e.eName);

                                lnl.addView(t);

                            }
                        }
                    }
                });
            }
        }).start();

    }

    public void DeleteDataBase(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Context context = getApplicationContext();    // Or your activity context
                context.deleteDatabase("workouts");   // Just Change the Name

                //recreate();
            }
        }).start();
    }
}