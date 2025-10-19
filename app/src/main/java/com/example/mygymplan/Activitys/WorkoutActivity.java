package com.example.mygymplan.Activitys;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
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
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class WorkoutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Data
    UserData user;
    public Plan thisPlan;
    public Workout thisWorkout;
    String NewWorkoutCompareString;   // Check if Workout is a New Workout

    // UI Elements
    TextView showName;
    List<Exercise> displayedExercises;  //
    List<Workout> displayedWorkouts;  //
    TextView workoutId;   //  Jut for Tests

    // RecyclerView
    ExerciseRVAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView recyclerViewHorizontal;
    TextView emptyView;

    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button testButton;


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


        // --- Components ---
        showName = findViewById(R.id.WorkoutName);
        workoutId = findViewById(R.id.WorkoutIdText);
        Button newExerciseButton = findViewById(R.id.CreateNewExercise);
        Button backButton = findViewById(R.id.BackButtonWorkout);
        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        recyclerViewHorizontal = findViewById(R.id.RecyclerView2);
        emptyView = findViewById(R.id.EmptyRVWorkouts);


        // --- Drawer Layout  ---
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.mygymplan_logo);                  // Set Logo on Toolbar
        drawerLayout = findViewById(R.id.DrawerLayout);
        navigationView = findViewById(R.id.NavView);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // --- Just for Tests ---
        testButton = navigationView.findViewById(R.id.TestButton);


        // -------------------------------------------------------------------
        //  ---  Set Values based on Received Data  ---
        NewWorkoutCompareString = thisWorkout.wName;
        showName.setText(thisPlan.planName);
        workoutId.setText(String.valueOf(thisWorkout.id));      // Just for Teste


        // -------------------------------------------------------------------
        LoadData();
        // -------------------------------------------------------------------


        // ------------ BUTTONS ------------

        // Create New Exercise
        newExerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Exercise newExercise = new Exercise();

                // New Exercise default values
                newExercise.workout_Id = thisWorkout.id;
                newExercise.eName = "New Exercise";
                newExercise.eDescription = "Description Here...";
                newExercise.eSets = 0;
                newExercise.eReps = 0;
                newExercise.eRest = 0;
                newExercise.eLoad = 0;

                // Change Activity
                Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class);
                intent.putExtra("SelectedExercise", newExercise);
                intent.putExtra("SelectedWorkout", thisWorkout);
                intent.putExtra("SelectedUser", user);
                startActivity(intent);
            }
        });


        // Back Button
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Change to Main Activity
                Intent intent = new Intent(WorkoutActivity.this, MainActivity.class);
                intent.putExtra("SelectedUser", user);
                intent.putExtra("SelectedPlan", thisPlan);
                startActivity(intent);
            }
        });

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // --------------------------------------------------------------------------------
        if (menuItem.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        // --------------------------------------------------------------------------------
        if (menuItem.getItemId() == R.id.nav_settings) {
            // Open Settings
        }
        // --------------------------------------------------------------------------------
        if (menuItem.getItemId() == R.id.nav_info) {
            // ------------------------------------
            // Open a Popup talking about the app
            // ------------------------------------
            // Inflate Activity with a new View
            View popupView = View.inflate(this, R.layout.popup_warning, null);

            // Popup View UI Content
            TextView popupWarning = popupView.findViewById(R.id.WarningMessage);
            Button confirmButton = popupView.findViewById(R.id.ConfirmWarningButton);
            Button closeButton = popupView.findViewById(R.id.CloseWarningButton);

            // Set height and width as WRAP_CONTENT
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;

            // Create the New View
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

            // Set Text Warning
            popupWarning.setText("About Us. App desenvolvido por Ricardo Thiago Firmino :)");

            // Close Buttons
            confirmButton.setVisibility(View.GONE);
            closeButton.setOnClickListener(v -> {
                popupWindow.dismiss();
            });

        }
        // --------------------------------------------------------------------------------
        if (menuItem.getItemId() == R.id.TestButton) {

            Intent intent = new Intent(this, TesteActivity.class);
            startActivity(intent);

        }
        // --------------------------------------------------------------------------------

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    // -------------------------------------------------------------------
    // -------------------------------------------------------------------
    // ------------------------- FUNCTIONS -------------------------------
    // -------------------------------------------------------------------
    // -------------------------------------------------------------------

    // --------------------------------------------
    // ----------- Load Data and display ----------
    // ------------- on Recycler View -------------
    // --------------------------------------------
    public void LoadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();

                displayedExercises = new ArrayList<>();
                List<Exercise>  newList = dao.listExercise();

                displayedWorkouts = new ArrayList<>();
                WorkoutDao daoW = db.workoutDao();
                List<Workout> workoutList = new ArrayList<>();
                 workoutList = daoW.listWorkouts();

                if (!workoutList.isEmpty()) {

                    for (Workout item : workoutList) {
                        if (item.plan_Id == thisPlan.id) {
                            displayedWorkouts.add(item);
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Exercise e : newList) {
                            if (e.workout_Id == thisWorkout.id) {
                                displayedExercises.add(e);
                            }
                        }

                        // Recycler View Adapter
                        ExerciseRVAdapter exerciseAdapter = new ExerciseRVAdapter(WorkoutActivity.this, displayedExercises, new ExerciseRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Exercise item) {
                                Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class);
                                intent.putExtra("SelectedUser", user);
                                intent.putExtra("SelectedPlan", thisPlan);
                                intent.putExtra("SelectedWorkout", thisWorkout);
                                intent.putExtra("SelectedExercise", item);
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(exerciseAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(WorkoutActivity.this));


                        // -------- Recycler View Horizontal --------

                        WorkoutRVAdapterHorizontal workoutAdapter = new WorkoutRVAdapterHorizontal(WorkoutActivity.this, displayedWorkouts, new WorkoutRVAdapterHorizontal.OnItemClickListener() {
                            @Override
                            public void onItemClick(Workout item) {

                            }
                        });
                        //displayedWorkouts
                        recyclerViewHorizontal.setAdapter(workoutAdapter);
                        recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(WorkoutActivity.this, LinearLayoutManager.HORIZONTAL, false));

                        ChangeUIVisibility();
                    }
                });
            }
        }).start();

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

}


