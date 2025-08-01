package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShowMyWorkoutActivity extends AppCompatActivity {

    private TextView showWName;
    // private List[] showWExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_my_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // ----- Received Data From Another Activity -----
        //Intent intent = getIntent();
        // Workout myActualWorkout = intent.getParcelableExtra("myActualWorkout");

        // Set Workout Name based on Recieved Data
        //showWName = findViewById(R.id.ShowExerciseName)
        //showWName.setText(intent.getParcelableExtra(myActualWorkout.wName));

        // Show Exercise
        // Intent intent2 = new Intent(ShowMyWorkoutActivity.this, ShowExerciseActivity.class);
        // intent2.putExtra("SelectedExercise", Exercise);

        // Examples:
        // intent.putExtra("key_string", "Hello from Source!");
        // intent.putExtra("key_int", 123);
        // For custom objects, implement Serializable or Parcelable
        // intent.putExtra("key_object", myCustomObject);

        // startActivity(intent);


        // Change Exercises Order


        // Add Exercise to Workout
        //Button addExerciseButton = findViewById(R.id.AddExerciseButton);
        //addExerciseButton.setOnClickListener(new View.OnClickListener() {
        //public void AddExercise() {

        //      }

        //}

    }
}