package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateWorkoutActivity extends AppCompatActivity {

    private EditText wName;
    public Exercise[] exercises;

    public UserData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        wName = findViewById(R.id.NewWorkoutName);

        // Create New Exercise
        Button nExerciseButton = (Button) findViewById(R.id.CreateNewExercise);
        nExerciseButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(CreateWorkoutActivity.this, CreateExerciseActivity.class));
            }
        });

        // Show All Exercises
        Button sExercisesButton = (Button) findViewById(R.id.ShowExercises);
        // The Add one Exercise to the List

        // Save Workout
        Button saveWorkoutButton = (Button) findViewById(R.id.SaveWorkout);
        saveWorkoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Save new Exercise
                user.myWorkouts = new Workout[]{new Workout(
                        wName.getText().toString(),
                        exercises
                )};
                // Change Activity
                startActivity(new Intent(CreateWorkoutActivity.this, MainActivity.class));
            }
        });

    }

// Show workouts int database
// Create new Exercise
// Save Workout

}