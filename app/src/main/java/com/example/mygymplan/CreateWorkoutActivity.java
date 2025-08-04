package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CreateWorkoutActivity extends AppCompatActivity {

    private EditText wName;
    public Exercise[] exercises;

    public UserData user;

    public Plan thisPlan;
    public Workout thisWorkout;


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

        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisPlan = intent.getParcelableExtra("SelectedPlan");

        // Components
        wName = findViewById(R.id.NewWorkoutName);
        RecyclerView recyclerView = findViewById(R.id.RecycleViewAddExercises);
        Button nExerciseButton = (Button) findViewById(R.id.CreateNewExercise);

        // Recycler View
        RV_MyWorkoutAdapter adapter = new RV_MyWorkoutAdapter(this, thisPlan.planWorkouts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // ----- BUTTONS -----

        // Create New Exercise
        nExerciseButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(CreateWorkoutActivity.this, CreateExerciseActivity.class);
                intent.putExtra("SelectedPlan", (Parcelable) thisPlan);
                intent.putExtra("SelectedWorkout", (Parcelable) thisWorkout);
                startActivity(intent);

                startActivity(new Intent(CreateWorkoutActivity.this, CreateExerciseActivity.class));
            }
        });

        // Show Database Exercises
        //Button showDatabaseExercisesButton = findViewById(R.id.ShowDatabaseExercises);
        // showDatabaseExercisesButton.setOnClickListener(new View.OnClickListener() {
        // public void ShowDatabaseWorkout() {

             //}
        // }


        // Show All Exercises
        Button sExercisesButton = (Button) findViewById(R.id.AddExercise);

        // The Add one Exercise to the List
        // Show My Saved Exercises
        Button showMyExercisesButton = findViewById(R.id.ShowMyExercisesButton);
        showMyExercisesButton.setOnClickListener(new View.OnClickListener() {
                                              public void ShowMyExercises() {
                                              }
                                          }



        // Save Workout
        Button saveWorkoutButton = (Button) findViewById(R.id.SaveWorkout);
        saveWorkoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Save new Exercise
                user.myWorkouts = new Workout[]{new Workout(
                        wName.getText().toString(),
                        exercises,

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