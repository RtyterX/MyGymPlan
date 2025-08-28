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
    String NewWorkoutCompareString;  // Check if Workout is a New Workout

    // UI Elements
    private EditText wName;
    List<Exercise> displayedExercises;

    // RecyclerView
    RV_MyExercisesAdapter adapter;
    RecyclerView recyclerView;
    TextView emptyView;
    TextView workoutId;


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
        wName = findViewById(R.id.WorkoutName);
        workoutId = findViewById(R.id.WorkoutIdText);
        Button newExerciseButton = findViewById(R.id.CreateNewExercise);
        Button backButton = findViewById(R.id.BackButton2);

        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyView = findViewById(R.id.EmptyRVWorkouts);


        // Set Values based on Received Data
        wName.setText(thisWorkout.wName);
        workoutId.setText(String.valueOf(thisWorkout.id));
        //NewWorkoutCompareString = thisWorkout.wName;

        LoadData();


        // ----- BUTTONS -----

        // Create New Exercise
        newExerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Exercise newExercise = new Exercise();
                newExercise.eName = "New Exercise";
                newExercise.workout_Id = thisWorkout.id;

                Intent intent = new Intent(ShowWorkoutActivity.this, ShowExerciseActivity.class);
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
                        RV_MyExercisesAdapter adapter = new RV_MyExercisesAdapter(ShowWorkoutActivity.this, displayedExercises, new RV_MyExercisesAdapter.OnItemClickListener() {
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


