package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mygymplan.Adapters.WorkoutRVAdapter;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.R;
import com.example.mygymplan.Services.PopupService;
import com.example.mygymplan.Services.WorkoutService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    Plan thisPlan;
    List<Workout> displayedWorkouts;

    // Shared Preferences
    String username;

    // UI Elements
    TextView planName;
    Button emptyButton;
    Button emptyWorkoutButton;

    // RecyclerView
    RecyclerView recyclerView;
    TextView emptyText;
    TextView count;
    WorkoutRVAdapter workoutAdapter;

    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button testButton;

    ItemTouchHelper mIth;

    PopupService popupService = new PopupService();


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Can't Rotate the Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");


        // --- Components ---
        planName = findViewById(R.id.PlanNameText);
        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyText = findViewById(R.id.EmptyRVText);
        emptyButton = findViewById(R.id.EmptyButton);
        count = findViewById(R.id.RVCount);
        // Buttons
        Button newPlan = findViewById(R.id.NewPlanButton);
        Button newWorkout = findViewById(R.id.NewWorkout);


        // --- Drawer Layout ---
        Toolbar toolbar = findViewById(R.id.toolbar2);                                         // Find Toolbar
        setSupportActionBar(toolbar);                                                          // Set Toolbar as ActionBar
        drawerLayout = findViewById(R.id.DrawerLayout);                                        // Find DrawerLayout
        // drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
        navigationView = findViewById(R.id.NavView);                                           // Find Navigation View
        navigationView.setNavigationItemSelectedListener(this);                                // Only Works if class: implements NavigationView.OnNavigationItemSelectedListener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);             // Set ActionBar (Hamburger Menu)
        drawerLayout.addDrawerListener(toggle);                                                // Set Click on ActionBar


        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // For example, you might want to navigate up or perform a custom action
                //drawerLayout.openDrawer(GravityCompat.START);
                planName.setText("Deu bom");
                //onBackPressed(); // Example: Go back to the previous screen
            }
        });

        toggle.syncState();                                                                   // Sync with drawer state (Open/Close)


        //  --- Go to Test Activity ---
        testButton = navigationView.findViewById(R.id.TestButton);
        // JUST FOR DEBUG ------------------------------------
        // JUST FOR DEBUG ------------------------------------


        // ---- Show Workouts in Recycle View or -----
        // ---- Display Create From Scratch button --\
        CheckUser();
        // CheckPlan();


        // -------------------------------------------
        // ----------------- BUTTONS -----------------
        // -------------------------------------------

        newPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { NewPlan(); }
        });

        newWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewWorkout();
            }
        });


        // ---------------------------------------------
        // --- Swap Recycler View List Item Position ---
        // ---------------------------------------------
        mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        0) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {

                        final int fromPos = viewHolder.getBindingAdapterPosition();
                        final int toPos = target.getBindingAdapterPosition();

                        Workout workout1 = displayedWorkouts.get(fromPos);
                        Workout workout2 = displayedWorkouts.get(toPos);

                        Collections.swap(displayedWorkouts, fromPos, toPos);
                        workoutAdapter.notifyItemMoved(fromPos, toPos);

                        WorkoutService workoutService = new WorkoutService();
                        workoutService.changeWorkoutOrder(getApplicationContext(), workout1, workout2);

                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        // Has no Swipe action
                    }
                });

    }


    // ------------------------------------------------------
    // ---------------- Create New Plan  --------------------
    // ------------------------------------------------------
    public void NewPlan() {
        popupService.NewPlanPopup(this, this, username);
    }


    // ------------------------------------------------------
    // ---------------- Create New Workout ------------------
    // ------------------------------------------------------

    public void NewWorkout() {
        popupService.NewWorkoutPopup(this, this, thisPlan.id);
    }


    // ------------------------------------------------------
    // ---------------- Edit Workout ------------------------
    // ------------------------------------------------------
    private void EditWorkout(Workout workout) {
        popupService.EditWorkoutPopup(this, this, workout);
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
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
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

    // --------------------------------------------
    // ----------- Load Data and Display ----------
    // ------------- on Recycler View -------------
    // --------------------------------------------
    public void CheckUser() {
        // Check if is First time opening App
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        username = sharedPreferences.getString("username", "");
        planName.setText(username);

        if (Objects.equals(username, "")) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }

        //////////////// NOT IMPLEMENTED //////////////
        // Check if User is Pro
        // CheckPro();
    }


    public void CheckPlan() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // ------------------------------------
                // ----- Check if User has 1 Plan  ----
                // ------------------------------------
                List<Plan> plansList = new ArrayList<>();
                PlanDao daoPlan = db.planDao();
                plansList = daoPlan.listPlans();

                if (!plansList.isEmpty()) {
                    for (Plan item : plansList) {
                        if (item.active == true) {
                            thisPlan = item;
                        }
                    }
                }

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newReloadRecyclerView();
                    }
                });
            }
        }).start();

        db.close();

    }



    // ---------------------------------------------------
    // ---------------- Reload Workouts ------------------
    // ---------------------------------------------------
    public void newReloadRecyclerView() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        WorkoutDao daoW = db.workoutDao();
        displayedWorkouts = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Workout> workoutList = new ArrayList<>();

                workoutList = daoW.listWorkouts();

                if (!workoutList.isEmpty()) {
                    for (Workout item : workoutList) {
                        if (item.plan_Id == thisPlan.id) {
                            displayedWorkouts.add(item);
                        }
                    }
                }
                // Sort by Order
                displayedWorkouts.sort((w1, w2) -> Integer.compare(w1.order, w2.order));

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count.setText(displayedWorkouts.size() + "/10");
                        ChangeUIVisibility2();
                        Teste();
                    }
                });

            }
        }).start();

        db.close();
    }


    public void Teste() {
        workoutAdapter = new WorkoutRVAdapter(MainActivity.this, displayedWorkouts, new WorkoutRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Workout item) {
                Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);
                intent.putExtra("SelectedWorkout", item);
                startActivity(intent);
            }
            // --------------------------------------
            @Override
            public void onItemLongClick(Workout item) {
                // Do nothing
            }
        }, new WorkoutRVAdapter.OnItemLongClickSwapPositions() {
            @Override
            public void swapButtonLongClick(int position) {
                mIth.attachToRecyclerView(recyclerView);
            }
        }, new WorkoutRVAdapter.OnClickEditListener() {
            @Override
            public void editButtonClick(Workout item) {
                EditWorkout(item);
            }
        });
        // Display Workouts in Recycler View
        recyclerView.setAdapter(workoutAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }


    // ----------------------------------------------
    // ------- Change UI Elements Visibility --------
    // ------- Based On Recycler View State ---------
    // ----------------------------------------------
    private void ChangeUIVisibility2() {
        Button newWorkout = findViewById(R.id.NewWorkout);
        Button newPlan = findViewById(R.id.NewPlanButton);
        emptyWorkoutButton = findViewById(R.id.EmptyButton);

        // -------------------
        // ----- NO PLAN -----
        // -------------------
        if (thisPlan == null) {
            // -- Recycler View --
            recyclerView.setVisibility(View.GONE);
            emptyButton.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.VISIBLE);
            // -- Buttons --
            newWorkout.setVisibility(View.GONE);
            newPlan.setVisibility(View.GONE);
            emptyWorkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewPlan();
                }
            });
            // -- Workout Number Count --
            count.setVisibility(View.GONE);
            // -- Plan Name --
            planName.setText("No Plan");
        } else {
            // ----------------------
            // ----- NO WORKOUT -----
            // ----------------------
            if (displayedWorkouts.isEmpty()) {
                // -- Recycler View --
                recyclerView.setVisibility(View.GONE);
                emptyButton.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.VISIBLE);
                // -- Buttons --
                newPlan.setVisibility(View.GONE);
                newWorkout.setVisibility(View.GONE);
                emptyWorkoutButton.setText("Create New Workout");
                emptyWorkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewWorkout();
                    }
                });
                // -- Workout Number Count --
                count.setVisibility(View.GONE);
            } else {
                // -----------------
                // ----- ELSE ------
                // -----------------
                recyclerView.setVisibility(View.VISIBLE);
                // -- Buttons --
                emptyButton.setVisibility(View.GONE);
                emptyText.setVisibility(View.GONE);

                CheckWorkoutLimit();
            }
            // Set Plan Name with or without workouts
            planName.setText(thisPlan.planName);
        }
    }


    // ---------------------------------------------------------------------
    // --------------- Disable New Workout Button if -----------------------
    // --------------- Number of Workouts has Reached ----------------------
    // --------------- The Limit (Above 10)   ------------------------------
    // ---------------------------------------------------------------------
    private void CheckWorkoutLimit() {
        count.setText(displayedWorkouts.size() + "/10");

        if (displayedWorkouts.size() >= 10) {
            Button newWorkout = findViewById(R.id.NewWorkout);
            newWorkout.setVisibility(View.GONE);
        }
    }


    // -------------------------------------------------------
    // ---------- Display Another Plan on Screen  ------------
    // -------------------------------------------------------
    public void ShowAnotherPlan(Plan plan) {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Plan> plansList = new ArrayList<>();
                plansList = dao.listPlans();

                for (Plan item : plansList) {
                    if (item == plan) {
                        thisPlan = item;
                    }
                }

                // ReloadRecyclerView();
            }
        }).start();

        db.close();

    }

    //////////////////////// END ////////////////////////
}