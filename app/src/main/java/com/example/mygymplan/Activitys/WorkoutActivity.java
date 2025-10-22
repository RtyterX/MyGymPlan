package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mygymplan.Adapters.ExerciseRVAdapter;
import com.example.mygymplan.Adapters.WorkoutRVAdapterHorizontal;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.UserData;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Services.ExerciseService;
import com.example.mygymplan.Services.NavigationBar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutActivity extends AppCompatActivity  {

    // Entity's Data
    UserData user;
    public Plan thisPlan;
    public Workout thisWorkout;

    // UI Elements
    TextView showName;
    List<Exercise> displayedExercises;
    List<Workout> displayedWorkouts;

    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    // Recycler Views
    RecyclerView recyclerView;                  // Exercise Recycler View (Vertical)
    RecyclerView horizontalRecyclerView;        // Workout Recycler View (Horizontal)
    ExerciseRVAdapter exerciseAdapter;          // Needed for Delete Exercise
    TextView emptyView;                         // Show Text When Recycler View is Empty

    // --> For Tests Only
    Button testButton;


    @SuppressLint("SourceLockedOrientationActivity")
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

        // Can't Rotate the Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        user = (UserData) intent.getSerializableExtra("SelectedUser");
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");


        // --- UI Elements ---
        showName = findViewById(R.id.WorkoutName);
        Button newButton = findViewById(R.id.CreateNewExercise);
        Button addButton = findViewById(R.id.AddExercise);
        Button backButton = findViewById(R.id.BackButtonWorkout);
        recyclerView = findViewById(R.id.RV_WorkoutsMain);
        horizontalRecyclerView = findViewById(R.id.RV_WorkoutsHorizontal);
        emptyView = findViewById(R.id.EmptyRVWorkouts);


        // --- Drawer Layout ---
        Toolbar toolbar = findViewById(R.id.toolbar2);                                        // Find Toolbar
        setSupportActionBar(toolbar);                                                         // Set Toolbar as ActionBar
        Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.mygymplan_logo);     // Set Logo on Toolbar (Not Null enforceable by IDE)
        drawerLayout = findViewById(R.id.DrawerLayout);                                       // Find DrawerLayout
        navigationView = findViewById(R.id.NavView);
        NavigationBar naviBar = new NavigationBar();
        navigationView.setNavigationItemSelectedListener(naviBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // -------------------------------------------------------------------
        // ---  Set Values based on Received Data  ---
        showName.setText(thisPlan.planName);


        // -------------------------------------------------------------------
        // ---  Load Data Recycler View on Create the Activity  ---
        LoadData(thisWorkout);


        // ---------------------------------
        // ------------ BUTTONS ------------
        // ---------------------------------

        // --- Create New Exercise ---
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateNewExercise();
            }
        });

        // --- Add Already Created Exercises ---
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddExercise();
            }
        });

        // --- Back Button ---
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Change back to Main Activity
                Intent intent = new Intent(WorkoutActivity.this, MainActivity.class);
                intent.putExtra("SelectedUser", user);
                intent.putExtra("SelectedPlan", thisPlan);
                startActivity(intent);
            }
        });

    }


    // -------------------------------------------------------------------
    // -------------------------------------------------------------------
    // ------------------------- FUNCTIONS -------------------------------
    // -------------------------------------------------------------------
    // -------------------------------------------------------------------

    // --------------------------------------------
    // ----------- Load Data and Display ----------
    // ------------- on Recycler Views -------------
    // --------------------------------------------
    public void LoadData(Workout workout) {
        WorkoutsHorizontalRecyclerView();
        UpdateRecyclerView(workout);
    }


    // ----------------------------------------------
    // -------- Reload Workouts Recycler View -------
    // ----------------------------------------------
    public void WorkoutsHorizontalRecyclerView() {
        // Access Database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Initialize List
                displayedWorkouts = new ArrayList<>();
                List<Workout> workoutList = new ArrayList<>();

                // List All Workouts
                workoutList = dao.listWorkouts();

                // Remove Workouts from other Plans
                if (!workoutList.isEmpty()) {
                    for (Workout item : workoutList) {
                        if (item.plan_Id == thisPlan.id) {
                            displayedWorkouts.add(item);
                        }
                    }
                }

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // -------- Set Recycler View Horizontal --------
                        WorkoutRVAdapterHorizontal workoutAdapter = new WorkoutRVAdapterHorizontal(WorkoutActivity.this, displayedWorkouts, new WorkoutRVAdapterHorizontal.OnItemClickListener() {
                            @Override
                            public void onItemClick(Workout item) {
                                UpdateRecyclerView(item);
                                showName.setText(item.wName);           // Just for Tests
                            }
                        });
                        // Display Workouts inside the Recycler View
                        horizontalRecyclerView.setAdapter(workoutAdapter);
                        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(WorkoutActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    }
                });
            }
        }).start();
    }


    // ----------------------------------------------
    // -------- Reload Exercise Recycler View -------
    // ----------------------------------------------
    public void UpdateRecyclerView(Workout workout) {
        // Access Database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        ExerciseDao dao = db.exerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Initialize List
                displayedExercises = new ArrayList<>();
                List<Exercise>  newList = dao.listExercise();

                // Add to List only the Exercises from this Workout
                for (Exercise e : newList) {
                    if (e.workout_Id == workout.id && e.plan_Id == thisPlan.id) {
                        displayedExercises.add(e);
                    }
                }

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // ----- Set Exercise Adapter -----
                        exerciseAdapter = new ExerciseRVAdapter(WorkoutActivity.this, displayedExercises, new ExerciseRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Exercise item) {
                                ChangeToExercise(item);
                            }
                        }, new ExerciseRVAdapter.OnItemClickDelete() {
                            @Override
                            public void deleteButtonClick(int position) {
                                DeleteFromRecyclerView(position);
                            }
                        });
                        // Display Exercises inside the Recycler View
                        recyclerView.setAdapter(exerciseAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(WorkoutActivity.this));
                        // Then Check if Need to Change the UI...
                        ChangeUIVisibility();
                    }
                });
            }
        }).start();
    }


    // ----------------------------------------------
    // ------ Create New Exercise From Scratch ------
    // ----------------------------------------------
    public void CreateNewExercise() {
        Exercise newExercise = new Exercise();

        // New Exercise default values
        newExercise.plan_Id = thisPlan.id;
        newExercise.workout_Id = thisWorkout.id;
        newExercise.eName = "1";
        newExercise.eDescription = "Description Here...";
        newExercise.eSets = 0;
        newExercise.eReps = 0;
        newExercise.eRest = 0;
        newExercise.eLoad = 0;
        newExercise.order = displayedExercises.size() + 1;

        // Change Activity
        ChangeToExercise(newExercise);
    }


    // ----------------------------------------------
    // --------- Add Exercise From Database ---------
    // ----------------------------------------------
    public void AddExercise() {
        // Not Created
    }


    // ----------------------------------------------
    // ------- Change UI Elements Visibility --------
    // ------- Based On Recycler View State ---------
    // ----------------------------------------------
    private void ChangeUIVisibility() {
        if (displayedExercises.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }


    // ----------------------------------------------
    // -------- Delete a Specific Row From ----------
    // ---------- Exercises Recycler View -----------
    // ----------------------------------------------
    public void DeleteFromRecyclerView(int position) {
        ExerciseService exerciseService = new ExerciseService();
        exerciseService.deleteExercise(getApplicationContext(), displayedExercises.get(position));
        displayedExercises.remove(position);
        exerciseAdapter.notifyItemRemoved(position);
        // Need to wait for animation when is the last Exercise in List
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ChangeUIVisibility();
            }
        }, 500); // 3000 milliseconds = 3 seconds

    }


    // ----------------------------------------------
    // ------ Change Activity to Show Exercise ------
    // ----------------------------------------------
    public void ChangeToExercise(Exercise item) {
        Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class);
        intent.putExtra("SelectedUser", user);
        intent.putExtra("SelectedPlan", thisPlan);
        intent.putExtra("SelectedWorkout", thisWorkout);
        intent.putExtra("SelectedExercise", item);
        startActivity(intent);
    }

    //////////////////////// END ////////////////////////
}