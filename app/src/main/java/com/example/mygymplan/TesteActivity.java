package com.example.mygymplan;

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

import java.util.ArrayList;
import java.util.List;

public class TesteActivity extends AppCompatActivity {

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
                finish();
            }
        });
    }


    public void show(View view) {

        LinearLayout lnl = findViewById(R.id.lnl);
        lnl.removeAllViews();

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Exercises").build();
                ExerciseDao dao = db.exerciseDao();

                List<Exercise> exercises = dao.listExercise();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Exercise e : exercises) {
                            TextView t = new TextView(TesteActivity.this);
                            t.setText(e.eName);

                            lnl.addView(t);
                        }
                    }
                });
            }
        }).start();

    }
}