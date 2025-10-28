package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mygymplan.Adapters.SavedExerciseRVAdapter;
import com.example.mygymplan.Adapters.ExerciseRVAdapter;
import com.example.mygymplan.Adapters.WorkoutRVAdapterHorizontal;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.SavedExerciseDao;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.UserData;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Services.ExerciseService;
import com.example.mygymplan.Services.NavigationBar;
import com.example.mygymplan.Services.PopupService;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    UserData user;
    public Plan thisPlan;
    public Workout thisWorkout;

    // UI Elements
    TextView showName;
    List<Exercise> displayedExercises;
    List<Workout> displayedWorkouts;

// -------------------------------------------------------------
    // Add Exercise Teste
    List<SavedExercise> myExercises;
    List<SavedExercise> databaseExercises;
    SavedExerciseRVAdapter myExerciseAdapter;
    SavedExerciseRVAdapter databaseAdapter;
    Exercise newExercise;

    // -------------------------------------------------------------


    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    // Recycler Views
    RecyclerView recyclerView;                  // Exercise Recycler View (Vertical)
    RecyclerView horizontalRecyclerView;        // Workout Recycler View (Horizontal)
    ExerciseRVAdapter exerciseAdapter;          // Needed for Delete Exercise
    TextView emptyView;                         // Show Text When Recycler View is Empty

    ItemTouchHelper mIth;

    // --> For Tests Only
    Button testButton;

    PopupService popupService;


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
        drawerLayout = findViewById(R.id.DrawerLayout);                                       // Find DrawerLayout
        navigationView = findViewById(R.id.NavView);                                          // Find Navigation View
        NavigationBar naviBar = new NavigationBar();                                          // Set Navigation Drawer
        navigationView.setNavigationItemSelectedListener(naviBar);                            // Only Works if class: implements NavigationView.OnNavigationItemSelectedListener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);            // Set ActionBar (Hamburger Menu)
        drawerLayout.addDrawerListener(toggle);                                               // Set Click on ActionBar
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);  // Lock NaviBar Swipe Right Gesture to Open
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        toggle.syncState();                                                                      // Sync with drawer state (Open/Close)


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


        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        0) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {

                        final int fromPos = viewHolder.getBindingAdapterPosition();
                        final int toPos = target.getBindingAdapterPosition();

                        Exercise exercise1 = displayedExercises.get(fromPos);
                        Exercise exercise2 = displayedExercises.get(toPos);

                        Collections.swap(displayedExercises, fromPos, toPos);
                        exerciseAdapter.notifyItemMoved(fromPos, toPos);

                        ExerciseService exerciseService = new ExerciseService();
                        exerciseService.changeExerciseOrder(getApplicationContext(), exercise1, exercise2);

                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        // Has no Swipe action

                    }
                });


    }

    // ------------------------------------------------------
    // ------- Switch for Navigation Bar Item List ----------
    // ------------------------------------------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // -----------------------------------------------------------------------------
        if (menuItem.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        // -----------------------------------------------------------------------------
        if (menuItem.getItemId() == R.id.nav_change_plan) {
            // ChangePlan();
        }
        // -----------------------------------------------------------------------------
        if (menuItem.getItemId() == R.id.nav_settings) {
            // Open Settings
        }
        // -----------------------------------------------------------------------------
        if (menuItem.getItemId() == R.id.nav_info) {
            popupService.AboutUsPopup(this);
        }
        // -----------------------------------------------------------------------------
        if (menuItem.getItemId() == R.id.TestButton) {
            Intent intent = new Intent(this, TesteActivity.class);
            startActivity(intent);
        }

        // Close Drawer after Click
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    // -------------------------------------------------------------------
    // -------------------------------------------------------------------
    // ------------------------- FUNCTIONS -------------------------------
    // -------------------------------------------------------------------
    // -------------------------------------------------------------------

    // ----------------------------------------------
    // --------- Add Exercise From Database ---------
    // ----------------------------------------------
    public void AddExercise() {
        // Open new Popup where user create a new Plan
        // -------------------------------------------------------
        // Inflate Activity with a new View
        View popupView = View.inflate(this, R.layout.popup_add_exercise, null);

        ExerciseService exerciseService = new ExerciseService();

        // Popup View UI Content
        Button MyExercisesButton = popupView.findViewById(R.id.MyExercisesButton);
        Button DatabaseButton = popupView.findViewById(R.id.DatabaseExercisesButton);
        Button closeButton = popupView.findViewById(R.id.CloseAddExercise);
        RecyclerView addExerciseRV = popupView.findViewById(R.id.AddExerciseRV);

        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        SavedExerciseDao dao = db.savedExerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Initialize List
                myExercises = new ArrayList<>();
                List<SavedExercise> allExercises;

                // List All Exercises
                allExercises = dao.listSavedExercise();

                // ---------- MY EXERCISES ----------
                if (!allExercises.isEmpty()) {
                    for (SavedExercise item : allExercises) {
                        if (item.userCreated) {
                            myExercises.add(item);
                        }
                    }
                }

                // ---------- DATABASE ----------
                if (!allExercises.isEmpty()) {
                    for (SavedExercise item : allExercises) {
                        if (!item.userCreated) {
                            myExercises.add(item);
                        }
                    }
                }


                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // ------ Show Recycler View (My Exercises when Open) ------
                        myExerciseAdapter = new SavedExerciseRVAdapter(WorkoutActivity.this, myExercises, new SavedExerciseRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(SavedExercise item) {
                                AddExerciseToWorkout(item);
                                popupWindow.dismiss();
                            }
                        });
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Display Exercises inside the Recycler View
                                addExerciseRV.setAdapter(myExerciseAdapter);
                                addExerciseRV.setLayoutManager(new LinearLayoutManager(WorkoutActivity.this));
                            }
                        }, 500); // 3000 milliseconds = 3 seconds

                    }
                });
            }
        }).start();

        db.close();


        // ------ Buttons ------
        MyExercisesButton.setOnClickListener(v -> {
            // ------ Show Recycler View (My Exercises when Open) ------
            SavedExerciseRVAdapter myExerciseAdapter = new SavedExerciseRVAdapter(WorkoutActivity.this, myExercises, new SavedExerciseRVAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SavedExercise item) {
                    AddExerciseToWorkout(item);
                    popupWindow.dismiss();
                }
            });
            // Display Exercises inside the Recycler View
            addExerciseRV.setAdapter(myExerciseAdapter);
            addExerciseRV.setLayoutManager(new LinearLayoutManager(WorkoutActivity.this));
        });

        DatabaseButton.setOnClickListener(v -> {
            // ------ Show Recycler View (My Exercises when Open) ------
            databaseAdapter = new SavedExerciseRVAdapter(WorkoutActivity.this, databaseExercises, new SavedExerciseRVAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SavedExercise item) {
                    AddExerciseToWorkout(item);
                    popupWindow.dismiss();
                }
            });
            // Display Exercises inside the Recycler View
            addExerciseRV.setAdapter(databaseAdapter);
            addExerciseRV.setLayoutManager(new LinearLayoutManager(WorkoutActivity.this));
        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }


    // --------------------------------------------
    // ----------- Load Data and Display ----------
    // ------------- on Recycler Views -------------
    // --------------------------------------------
    public void LoadData(Workout workout) {
        WorkoutsHorizontalRecyclerView(workout.order);
        UpdateRecyclerView(workout);
    }


    // ----------------------------------------------
    // -------- Reload Workouts Recycler View -------
    // ----------------------------------------------
    public void WorkoutsHorizontalRecyclerView(int position) {
        // Access Database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Initialize List
                displayedWorkouts = new ArrayList<>();
                List<Workout> allWorkouts = new ArrayList<>();

                // List All Workouts
                allWorkouts = dao.listWorkouts();

                // Remove Workouts from other Plans
                if (!allWorkouts.isEmpty()) {
                    for (Workout item : allWorkouts) {
                        if (item.plan_Id == thisPlan.id) {
                            displayedWorkouts.add(item.order, item);
                        }
                    }
                }

                // Sort by Order
                displayedWorkouts.sort((w1, w2) -> Integer.compare(w1.order, w2.order));

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // -------- Set Recycler View Horizontal --------
                        WorkoutRVAdapterHorizontal workoutAdapter = new WorkoutRVAdapterHorizontal(WorkoutActivity.this, displayedWorkouts, new WorkoutRVAdapterHorizontal.OnItemClickListener() {
                            @Override
                            public void onItemClick(Workout item) {
                                UpdateRecyclerView(item);
                                WorkoutsHorizontalRecyclerView(item.order);  // Change Color
                            }
                        }, position);
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
                List<Exercise> newList = dao.listExercise();

                // Add to List only the Exercises from this Workout
                for (Exercise e : newList) {
                    if (e.workout_Id == workout.id && e.plan_Id == thisPlan.id) {
                        displayedExercises.add(e);
                    }
                }

                // Sort by Order
                displayedExercises.sort((e1, e2) -> Integer.compare(e1.order, e2.order));

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
                        }, new ExerciseRVAdapter.OnItemLongClickSwapPositions() {
                            @Override
                            public void swapButtonLongClick(int position) {

                            }
                        });
                        // Display Exercises inside the Recycler View
                        recyclerView.setAdapter(exerciseAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(WorkoutActivity.this));
                        // Then Check if Need to Change the UI...
                        ChangeUIVisibility();
                        // Attach Item Touch to RecyclerView
                        mIth.attachToRecyclerView(recyclerView);


                    }
                });
            }
        }).start();

        db.close();
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
        newExercise.order = displayedExercises.size();

        // Change Activity
        ChangeToExercise(newExercise);
    }


    // ----------------------------------------------
    // -------- Delete a Specific Row From ----------
    // ---------- Exercises Recycler View -----------
    // ----------------------------------------------
    public void AddExerciseToWorkout(SavedExercise savedExercise) {
        ExerciseService exerciseService = new ExerciseService();
        // --------------------------------------
        newExercise = exerciseService.ConvertExercise(savedExercise,
                displayedExercises.size() + 1,thisPlan.id, thisWorkout.id);
        // --------------------------------------
        exerciseService.addExercise(getApplicationContext(), newExercise);
        exerciseAdapter.notifyItemRangeRemoved(0, exerciseAdapter.getItemCount());
        UpdateRecyclerView(thisWorkout);
        ChangeUIVisibility();
    }


    // ----------------------------------------------
    // -------- Delete a Specific Row From ----------
    // ---------- Exercises Recycler View -----------
    // ----------------------------------------------
    public void DeleteFromRecyclerView(int position) {
        ExerciseService exerciseService = new ExerciseService();
        // --------------------------------------
        exerciseService.deleteExercise(getApplicationContext(), newExercise);
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