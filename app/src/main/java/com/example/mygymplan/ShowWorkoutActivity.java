package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ShowWorkoutActivity extends AppCompatActivity {

    // Data
    public Plan thisPlan;
    public Workout thisWorkout;
    String NewWorkoutCompareString;   // Check if Workout is a New Workout

    // UI Elements
    private EditText showName;
    List<Exercise> displayedExercises;  //
    TextView workoutId;   //  Jut for Tests

    // RecyclerView
    ExerciseRVAdapter adapter;
    RecyclerView recyclerView;
    TextView emptyView;



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
        Intent intent = getIntent();
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");


        // Components
        showName = findViewById(R.id.WorkoutName);
        workoutId = findViewById(R.id.WorkoutIdText);
        Button newExerciseButton = findViewById(R.id.CreateNewExercise);
        Button saveWorkoutButton = findViewById(R.id.SaveWorkout);
        Button backButton = findViewById(R.id.BackButton2);

        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyView = findViewById(R.id.EmptyRVWorkouts);


        // Set Values based on Received Data
        showName.setText(thisWorkout.wName);
        workoutId.setText(String.valueOf(thisWorkout.id));      // Just for Teste

        NewWorkoutCompareString = thisWorkout.wName;


        LoadData();


        // ----- BUTTONS -----

        // Create New Exercise
        newExerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Exercise newExercise = new Exercise();

                // New Exercise default values
                newExercise.workout_Id = thisWorkout.id;
                newExercise.eName = "New Exercise";
                newExercise.eDescription = "Description Here...";
                newExercise.eSets = 4;
                newExercise.eReps = 8;
                newExercise.eRest = 1;
                newExercise.eLoad = 1;

                // Change Activity
                Intent intent = new Intent(ShowWorkoutActivity.this, ShowExerciseActivity.class);
                intent.putExtra("SelectedExercise", newExercise);

                startActivity(intent);
            }
        });

        // Save or Update Workout
        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // Get Workout Values
                        thisWorkout.wName = showName.getText().toString();

                        // Database
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                        WorkoutDao dao = db.workoutDao();

                        // Create if Workout is New
                        if (Objects.equals(NewWorkoutCompareString, "New Workout")) {
                            // Save new Workout
                            dao.insertWorkout(thisWorkout);
                        }
                        // Update if Workout is already created
                        else {
                            // Save new Workout
                           dao.updateWorkout(thisWorkout);
                        }

                    }

                }).start();

                // Change to Main Activity
                Intent intent = new Intent(ShowWorkoutActivity.this, MainActivity.class);

                startActivity(intent);

            }
        });


        // Back Button
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void checkEmptyState(){
        if (displayedExercises.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

    }

    public void LoadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();

                displayedExercises = new ArrayList<>();
                List<Exercise>  newList = dao.listExercise();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Exercise e : newList) {
                            if (e.workout_Id == thisWorkout.id) {

                                displayedExercises.add(e);
                            }
                        }

                        // Recycler View Adapter
                        ExerciseRVAdapter adapter = new ExerciseRVAdapter(ShowWorkoutActivity.this, displayedExercises, new ExerciseRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Exercise item) {
                                Intent intent = new Intent(ShowWorkoutActivity.this, ShowExerciseActivity.class);
                                intent.putExtra("SelectedExercise", item);
                                startActivity(intent);
                            }

                            @Override
                            public void deletebuttonClick(Exercise item) {
                                Exercise deletedExercise = item;
                                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                                ExerciseDao dao = db.exerciseDao();
                                dao.deleteExercise(deletedExercise);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ShowWorkoutActivity.this));

                        checkEmptyState();
                    }
                });
            }
        }).start();

    }


    // -------- NOT IN USE --------
    public void AddExercise(View view) {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        WorkoutDao dao2 = db.workoutDao();

       dao2.deleteWorkout(thisWorkout);


    }



    ///////////////////////////////////////////////////
    /////////////// JUST FOR TEST ////////////////////
    /////////////////////////////////////////////////

    // ------ Database ------

    public void ShowDatabase(View view) {
        LoadData();
    }

    public void deleteDatabase(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                db.clearAllTables();
                recreate();
            }
        }).start();
    }

}


