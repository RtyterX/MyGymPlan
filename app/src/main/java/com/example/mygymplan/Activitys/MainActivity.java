package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDelegate;
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

import com.example.mygymplan.Adapters.PlanRVAdapter;
import com.example.mygymplan.Adapters.WorkoutRVAdapter;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.R;
import com.example.mygymplan.Services.ImageConverter;
import com.example.mygymplan.Services.PlanService;
import com.example.mygymplan.Services.PopupService;
import com.google.android.material.navigation.NavigationView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Entity's
    Plan activePlan;
    Workout todaysWorkout;
    List<Plan> planList = new ArrayList<>();
    List<Workout> workoutsList = new ArrayList<>();


    // Shared Preferences
    String username;
    String email;
    String userImageString;
    int activePlanId;
    int todaysWorkoutId;


    // UI Elements
    TextView mainText;
    Button createPlan;
    TextView noWorkout;
    ItemTouchHelper mIth;

    // RecyclerView
    RecyclerView plansRV;
    RecyclerView workoutRV;
    WorkoutRVAdapter workoutAdapter;

    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView naviBarImage;
    ActivityResultLauncher<Intent> resultLauncher;


    // Others
    Button testButton;
    PopupService popupService = new PopupService();
    ImageConverter imageConverter = new ImageConverter();

    PlanRVAdapter planAdapter;

    //------------------------------------------------------------------------


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

        // Night Mode Off **** because its not Implemented yet *****
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        // --- Components ---
        mainText = findViewById(R.id.MainTitle);
        workoutRV = findViewById(R.id.MainRVWorkouts);
        plansRV = findViewById(R.id.MainRVPlans);
        createPlan = findViewById(R.id.MainCreatePlanButton);
        noWorkout = findViewById(R.id.MainNoWorkoutText);
        ImageView info = findViewById(R.id.MainInfoButton);
        ImageView planImport = findViewById(R.id.ImportMainIcon);
        Button otherPlans = findViewById(R.id.OtherPlansButton);
        // Set View Flipper
        ViewFlipper flipper = findViewById(R.id.MainFlipperView);
        flipper.setVisibility(View.VISIBLE);
        flipper.startFlipping();
        noWorkout.setVisibility(View.GONE);


        // ---- Show Workouts in Recycle View or -----
        // ---- Display Create From Scratch button --\
        CheckUser();


        // ----- Set UI Data-----
        mainText.setText("Bem vindo, " + username);
        String dayOfWeek2 = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        TextView dayOfWeekText = findViewById(R.id.DayOfWeekText);
        dayOfWeekText.setText(dayOfWeek2);


        // --- Drawer Layout ---
        Toolbar toolbar = findViewById(R.id.toolbar2);                                         // Find Toolbar
        setSupportActionBar(toolbar);                                                          // Set Toolbar as ActionBar
        drawerLayout = findViewById(R.id.DrawerLayout);                                        // Find DrawerLayout
        navigationView = findViewById(R.id.NavView);                                           // Find Navigation View
        navigationView.setNavigationItemSelectedListener(this);                                // Only Works if class: implements NavigationView.OnNavigationItemSelectedListener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);                       // Set ActionBar (Hamburger Menu)
        drawerLayout.addDrawerListener(toggle);                                                // Set Click on ActionBar
        toggle.syncState();                                                                    // Sync with drawer state (Open/Close)


        //  --- Go to Test Activity ---
        testButton = navigationView.findViewById(R.id.TestButton);
        // JUST FOR DEBUG ------------------------------------
        // JUST FOR DEBUG ------------------------------------


        // NaviBar Values
        View headerView = navigationView.getHeaderView(0);
        TextView userNameText = headerView.findViewById(R.id.UsernameNaviBar);
        TextView userEmailText = headerView.findViewById(R.id.UserEmailNaviBar);
        naviBarImage = headerView.findViewById(R.id.UserPhotoNaviBar);
        userNameText.setText(username);
        userEmailText.setText(email);
        // Set Image
        Bitmap bitmap2 = imageConverter.ConvertToBitmap(userImageString);
        naviBarImage.setImageBitmap(bitmap2);


        // ----- Buttons -----
        createPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlan();
            }
        });

        otherPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToPlanSelection();
            }
        });

    }

    //--------------------------------------------------------------------------------------

    public void createPlan() {
        popupService.NewPlanMainPopup(this, MainActivity.this, username);
    }

    public void changeToPlanSelection() {
        Intent intent = new Intent(this, SelectPlanActivity.class);
        startActivity(intent);
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
        if (menuItem.getItemId() == R.id.nav_plans) {
            Intent intent = new Intent(this, SelectPlanActivity.class);
            startActivity(intent);
        }
        // -----------------------------------------------------------------------------
        if (menuItem.getItemId() == R.id.nav_settings) {
            // Open Settings
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
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

        // --- Close Drawer after Click ---
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
        // Check if its user First time opening App
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        email = sharedPreferences.getString("email", "");
        userImageString = sharedPreferences.getString("userImageString", "");
        todaysWorkoutId = sharedPreferences.getInt("todaysWorkoutId", 0);
        activePlanId = sharedPreferences.getInt("activePlanId", 0);

        Log.d("RV Plans", "Active Plan ID = " + sharedPreferences.getInt("activePlanId", 0));

        // If has no User, go to Welcome Page
        if (Objects.equals(username, "")) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

        if (activePlanId == 0) {
            Log.d("Active Plan", "User have no Plans");

            noWorkout.setVisibility(View.VISIBLE);
            noWorkout.setText("You have no Active Plan.\n Create one or choose from list!");
        }
        else {
            CheckPlan();
        }

        //////////////// NOT IMPLEMENTED //////////////
        // Check if User is Pro
        // CheckPro();
    }

    // --------------------------------------------
    // ----------- Set User Active Plan -----------
    // ------------- in Recycler View -------------
    // --------------------------------------------
    public void CheckPlan() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();

                // -----------------------------------------
                // ----- Check if User has Active Plan -----
                // -----------------------------------------
                PlanDao daoPlan = db.planDao();
                List<Plan> allPlans = daoPlan.listPlans();

                // Find Active Plan First
                if (!allPlans.isEmpty()) {
                    for (Plan item : allPlans) {
                        if (item.id == activePlanId) {
                            activePlan = item;
                            planList.add(item);
                            break;
                        }
                    }
                }

                // Add other Plans from Database
                if (!allPlans.isEmpty()) {
                    for (Plan item : allPlans) {
                        if (!item.userCreated) {
                            planList.add(item);
                        }
                    }
                }

                db.close();
            }
        }).start();


        // ----------------------------------------------------------------------
        // Wait before Apply Plans RV and Get Today Workout
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ApplyPlanRV();
                GetTodaysWorkout();
            }
        }, 500);

    }


    public void ApplyPlanRV() {

        if (!planList.isEmpty()) {
            Log.d("RV Plans", " Applying Plan Recycler View... ");

            // Change UI
            plansRV.setVisibility(View.VISIBLE);

            // Inflate Plan Recycler View
            planAdapter = new PlanRVAdapter(MainActivity.this, planList, new PlanRVAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Plan item) {
                    Intent intent = new Intent(MainActivity.this, PlanActivity.class);
                    intent.putExtra("SelectedPlan", item);
                    startActivity(intent);
                }
            }, new PlanRVAdapter.OnItemClickDelete() {
                @Override
                public void deleteButtonClick(int position) {

                }
            }, new PlanRVAdapter.OnItemClickSetActive() {
                @Override
                public void setActiveButtonClick(Plan plan) {

                }
            }, new PlanRVAdapter.OnClickEditPlanListener() {
                @Override
                public void editButtonClick(Plan plan) {

                }
            });
            // Display Workouts in Recycler View
            plansRV.setAdapter(planAdapter);
            //plansRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            plansRV.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

        } else {
            Log.d("RV Plans", " No Plans in Recycler View");
        }

    }


    // --------------------------------------------
    // ------------ Get todays Workout ------------
    // --------------------------------------------
    private void GetTodaysWorkout() {

        if (activePlan != null) {

            // Check if Active Plan haven't Fixed Days
            if (activePlan.fixedDays) {
                Log.d("Teste", "Active Plan " + activePlan.id + " has fixed days");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // ------------------------------------------------------
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                        WorkoutDao dao = db.workoutDao();
                        List<Workout> allWorkouts = dao.listWorkouts();

                        for (Workout item : allWorkouts) {
                            if (item.plan_Id == activePlan.id) {

                                if (item.dayOfWeek == LocalDate.now().getDayOfWeek()) {

                                    todaysWorkout = item;
                                    workoutsList.add(item);
                                    break;
                                }
                            }
                        }

                        db.close();
                    }
                }).start();

            } else {  // --------------------------------------------------------------------------------------------------------
                Log.d("Teste", "Active Plan " + activePlan.id + " HASN'T fixed days");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // ------------------------------------------------------
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                        WorkoutDao dao = db.workoutDao();
                        List<Workout> allWorkouts = dao.listWorkouts();

                        for (Workout item : allWorkouts) {
                            if (item.plan_Id == activePlan.id) {
                                if (item.id == todaysWorkoutId) {
                                    todaysWorkout = item;
                                    workoutsList.add(item);
                                    break;
                                }
                            }
                        }

                        db.close();
                    }
                }).start();
            }
        }

        if (todaysWorkout == null) {

        }

        // Wait before Apply Recycler View
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ApplyWorkoutRV();
            }
        }, 50);
    }


    // ---------------------------------------------------
    // ------------- Apply Workout in RV -----------------
    // ---------------------------------------------------
    private void ApplyWorkoutRV() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!workoutsList.isEmpty()) {
                            Log.d("Teste RV", "Workout encontrado");

                            noWorkout.setVisibility(View.GONE);

                            //Applie Recycler View
                            workoutAdapter = new WorkoutRVAdapter(MainActivity.this, workoutsList, new WorkoutRVAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Workout item) {
                                    Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
                                    intent.putExtra("SelectedPlan", activePlan);
                                    intent.putExtra("SelectedWorkout", item);
                                    startActivity(intent);
                                }
                            }, new WorkoutRVAdapter.OnClickEditListener() {
                                @Override
                                public void editButtonClick(int position) {
                                    // EditWorkout(item);
                                }
                            });
                            // Display Workouts in Recycler View
                            workoutRV.setAdapter(workoutAdapter);
                            workoutRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        } else {
                            Log.d("Teste RV", "Todays Workout NOT found");

                            noWorkout.setVisibility(View.VISIBLE);
                            noWorkout.setText("No Workout for Today");
                        }
                    }
                });

            }
        }).start();

    }


    // ---------------------------------------------------
    // ------------- Change to Plan Activity -------------
    // ---------------------------------------------------
    public void changeToPlan(Plan plan) {
        Intent intent = new Intent(this, PlanActivity.class);
        intent.putExtra("SelectedPlan", plan);
        startActivity(intent);
    }


    //////////////////////// END ////////////////////////
}