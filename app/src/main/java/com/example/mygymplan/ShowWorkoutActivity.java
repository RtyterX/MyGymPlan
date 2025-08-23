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

public class ShowWorkoutActivity extends AppCompatActivity {

    // Data
    public UserData user;
    public Plan thisPlan;
    public Workout thisWorkout;
    int i;  // Id for exercise already created
    String NewWorkoutCompareString;  // Check if Workout is a New Workout

    // UI Elements
    private EditText wName;
    List<Exercise> displayedExercises;

    // RecyclerView
    RV_MyExercisesAdapter adapter;
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
        //Intent intent = getIntent();
        // thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");
        // thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");


        // Components
        wName = findViewById(R.id.WorkoutName);
        Button newExerciseButton = findViewById(R.id.CreateNewExercise);
        Button backButton = findViewById(R.id.BackButton2);

        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyView = findViewById(R.id.EmptyRVWorkouts);



        // Set Values based on Received Data
        // displayedExercises = new ArrayList<>();
        //displayedExercises = thisWorkout.wExercises;
        //wName.setText(thisWorkout.wName);
        //i = thisWorkout.id;
        //NewWorkoutCompareString = thisWorkout.wName;


        LoadData();


        // ----- BUTTONS -----

        // Create New Exercise
        newExerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Exercise newExercise = new Exercise();
                newExercise.eName = "New Exercise";

                ArrayList<Exercise> newExerciseList = new ArrayList<>();
                thisWorkout = new Workout(
                        1,
                        "New Workout teste 3",
                        newExerciseList
                );

                Intent intent = new Intent(ShowWorkoutActivity.this, ShowExerciseActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);
                intent.putExtra("SelectedWorkout", thisWorkout);
                intent.putExtra("SelectedExercise", newExercise);

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
        //if (thisWorkout.wExercises.isEmpty()) {
        if (displayedExercises.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

    }

    public void ShowDatabase(View view) {
        LoadData();
    }

    public void LoadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Exercises").build();
                ExerciseDao dao = db.exerciseDao();

                displayedExercises = dao.listExercise();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Recycler View Adapter
                        RV_MyExercisesAdapter adapter = new RV_MyExercisesAdapter(ShowWorkoutActivity.this, displayedExercises);
                        //recyclerView.setAdapter(new RV_MyExercisesAdapter(ShowWorkoutActivity.this, displayedExercises));

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ShowWorkoutActivity.this));
                        checkEmptyState();
                    }
                });

            }
        }).start();

    }

    public void deleteDatabase(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Exercises").build();
                db.clearAllTables();
                recreate();
            }
        }).start();
    }
}


