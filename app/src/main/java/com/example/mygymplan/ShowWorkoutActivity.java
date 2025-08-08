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

import java.util.ArrayList;

public class ShowWorkoutActivity extends AppCompatActivity {

    private EditText wName;

    public UserData user;

    public Plan thisPlan;
    public Workout thisWorkout;
    ArrayList<Exercise> wExercises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // ----- Received Data From Another Activity -----
        //Intent intent = getIntent();
        //thisPlan = intent.getParcelableExtra("SelectedPlan");


        // Set Workout Name based on Received Data
        // wName = findViewById(R.id.WorkoutName);
        // wName.setText(intent.getParcelableExtra(thisPlan.planName));

        // Components
        //RecyclerView recyclerView = findViewById(R.id.RecycleViewAddExercises);
        Button nExerciseButton = findViewById(R.id.CreateNewExercise);
        Button addExerciseButton = findViewById(R.id.AddExercise);
        Button saveWorkoutButton = findViewById(R.id.SaveWorkout);

        // Recycler View
        //RV_MyWorkoutAdapter adapter = new RV_MyWorkoutAdapter(this, thisPlan.planWorkouts);
        // recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // ----- BUTTONS -----

        // Create New Exercise
        //nExerciseButton.setOnClickListener(new View.OnClickListener(){
        //public void onClick(View v){
        // Intent intent = new Intent(ShowWorkoutActivity.this, ShowExerciseActivity.class);
        // intent.putExtra("SelectedPlan", (Parcelable) thisPlan);
        // intent.putExtra("SelectedWorkout", (Parcelable) thisWorkout);
        // startActivity(intent);

        // startActivity(new Intent(ShowWorkoutActivity.this, ShowExerciseActivity.class));
        // }
        // });


        // Add a Already Created Exercise to Workout
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });


        // Save Workout
        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Save new Exercise
                user.myWorkouts = new Workout[]{new Workout(
                        wName.getText().toString(),
                        wExercises

                )};
                // Change Activity
                startActivity(new Intent(ShowWorkoutActivity.this, MainActivity.class));
            }

        });
    }

}