package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlinx.coroutines.scheduling.WorkQueueKt;


public class ShowWorkoutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Data
    UserData user;
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


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");
        user = (UserData) intent.getSerializableExtra("SelectedUser");


        // --- Components ---
        showName = findViewById(R.id.WorkoutName);
        workoutId = findViewById(R.id.WorkoutIdText);
        Button newExerciseButton = findViewById(R.id.CreateNewExercise);
        Button saveWorkoutButton = findViewById(R.id.SaveWorkout);
        //Button backButton = findViewById(R.id.BackButton2);
        TextView showDescription = findViewById(R.id.WorkoutDescription);
        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyView = findViewById(R.id.EmptyRVWorkouts);


        // --- Drawer Layout  ---
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
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
        showName.setText(thisWorkout.wName);
        showDescription.setText(thisWorkout.wDescription);
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
                newExercise.eSets = 4;
                newExercise.eReps = 8;
                newExercise.eRest = 1;
                newExercise.eLoad = 1;

                // Change Activity
                Intent intent = new Intent(ShowWorkoutActivity.this, ShowExerciseActivity.class);
                intent.putExtra("SelectedExercise", newExercise);
                intent.putExtra("SelectedWorkout", thisWorkout);
                intent.putExtra("SelectedUser", user);
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
                        thisWorkout.wDescription = showDescription.getText().toString();

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
                Intent intent = new Intent(ShowWorkoutActivity.this, FirstPage.class);
                intent.putExtra("SelectedUser", user);
                startActivity(intent);

            }
        });


        // Back Button
       // backButton.setOnClickListener(new View.OnClickListener() {
           // public void onClick(View v) {
               // finish();
          //  }
       // });

    }

    // -------------------------------------------------------------------


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
                                intent.putExtra("SelectedUser", user);
                                intent.putExtra("SelectedWorkout", thisWorkout);
                                intent.putExtra("SelectedExercise", item);
                                startActivity(intent);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (menuItem.getItemId() == R.id.nav_settings) {
            // Open Settings
        }

        if (menuItem.getItemId() == R.id.nav_info) {
            // Open a Popup talking about the app
            // -------------------------------------------------------
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

        if (menuItem.getItemId() == R.id.TestButton) {

            Intent intent = new Intent(this, TesteActivity.class);
            startActivity(intent);

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}


