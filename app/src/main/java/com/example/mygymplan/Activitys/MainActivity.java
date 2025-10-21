package com.example.mygymplan.Activitys;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mygymplan.Adapters.PlanRVAdapter;
import com.example.mygymplan.Adapters.WorkoutRVAdapter;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Database.UserDataDao;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.UserData;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Enums.WorkoutType;
import com.example.mygymplan.R;
import com.example.mygymplan.Services.WorkoutService;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Data
    UserData user;
    Plan thisPlan;
    List<Workout> displayedWorkouts;

    // UI
    TextView planName;
    Button emptyButton;
    Button emptyWorkoutButton;

    // RecyclerView
    RecyclerView recyclerView;
    TextView emptyText;
    TextView count;

    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button testButton;

    // Dropdown Menu
    AutoCompleteTextView autoComplete;
    ArrayAdapter<String> adapterItem;

    // ------------------------------------
    // ------------ Enum ------------------
    // ------------------------------------
    String[] typeInView = {
            "Chest",
            "Back",
            "Shoulder",
            "Arms",
            "Legs",
            "Biceps",
            "Triceps"};


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
        user = (UserData) intent.getSerializableExtra("SelectedUser");


        // --- Components ---
        planName = findViewById(R.id.PlanNameText);
        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyText = findViewById(R.id.EmptyRVText);
        emptyButton = findViewById(R.id.EmptyButton);
        count = findViewById(R.id.RVCount);


        // --- Drawer Layout ---
        Toolbar toolbar = findViewById(R.id.toolbar2);                             // Find Toolbar
        setSupportActionBar(toolbar);                                              // Set Toolbar as ActionBar
        getSupportActionBar().setIcon(R.drawable.mygymplan_logo);                  // Set Logo on Toolbar
        drawerLayout = findViewById(R.id.DrawerLayout);                            // Find DrawerLayout
        navigationView = findViewById(R.id.NavView);                               // Find Navigation View
        navigationView.setNavigationItemSelectedListener(this);                    // Only Works if class: implements NavigationView.OnNavigationItemSelectedListener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.app_name, R.string.app_name);              // Set ActionBar Toggle
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //  --- Go to Test Activity ---
        testButton = navigationView.findViewById(R.id.TestButton);
        // JUST FOR DEBUG ------------------------------------
        // JUST FOR DEBUG ------------------------------------


        // ---- Show Workouts in Recycle View or -----
        // ---- display Create From Scratch button --
        LoadData();
    }


    // ---------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    // ------------------------------------------ BUTTONS ------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------

    // ----------------------------------------------------------------
    // ---------------- If User Has no Workout Plans ------------------
    // ----------------------------------------------------------------
    public void NewPlan(View view) {
        // Open new Popup where user create a new Plan
        // -------------------------------------------------------
        // Inflate Activity with a new View
        View popupView = View.inflate(this, R.layout.popup_new_plan, null);

        // Popup View UI Content
        EditText newPlanName = popupView.findViewById(R.id.NewPlanName);
        EditText newPlanDescription = popupView.findViewById(R.id.NewPlanDescription);
        TextView newPlanNameWarning = popupView.findViewById(R.id.NewPlanDescription);
        TextView newPlanDescripWarning = popupView.findViewById(R.id.NewPlanDescription);
        Button confirmButton = popupView.findViewById(R.id.ConfirmWarningButton);
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);

        // Create the New View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // ------ Buttons ------
        confirmButton.setOnClickListener(v -> {
            if (newPlanName.getText().toString().isEmpty()) {
                newPlanNameWarning.setVisibility(View.VISIBLE);
            } else if (newPlanDescription.getText().toString().isEmpty()) {
                newPlanDescripWarning.setVisibility(View.VISIBLE);
            } else {

                // Inflate Activity with a new View
                View subView = View.inflate(this, R.layout.popup_warning, null);

                // Popup View UI Content
                TextView popupText = subView.findViewById(R.id.WarningMessage);
                Button yesButton = subView.findViewById(R.id.ConfirmWarningButton);
                Button noButton = subView.findViewById(R.id.CloseWarningButton);

                // Create the New View
                PopupWindow subPopupWindow = new PopupWindow(subView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                subPopupWindow.showAtLocation(subView, Gravity.CENTER, 0, 0);

                // Set Text Warning
                popupText.setText("Gostaria de deixar o Plano como Ativo?");
                yesButton.setText("Yes");
                noButton.setText("No");

                // ------ Buttons ------
                yesButton.setOnClickListener(subV -> {
                    CreateNewPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString(), true);
                    popupWindow.dismiss();
                    subPopupWindow.dismiss();
                });

                noButton.setOnClickListener(subV -> {
                    CreateNewPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString(), false);
                    // ShowPlan(plan);
                    popupWindow.dismiss();
                    subPopupWindow.dismiss();
                });
            }
        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }


    // ------------------------------------------------------
    // ---------------- Create New Workout ------------------
    // ------------------------------------------------------

    public void NewWorkout(View view) {
        // -------------------------------------------------------
        // Open new Popup where user create a new Plan
        // -------------------------------------------------------
        // Inflate Activity with a new View
        View popupView = View.inflate(this, R.layout.popup_new_workout, null);

        // --- Popup View UI Content ---
        // Values

        EditText newWorkoutName = popupView.findViewById(R.id.NewWorkoutName);
        EditText newWorkoutDescription = popupView.findViewById(R.id.NewWorkoutDescription);
        // Warnings
        TextView nameWarning = popupView.findViewById(R.id.NameWorkoutWarning);
        TextView descriptionWarning = popupView.findViewById(R.id.DescriptionWorkoutWarning);
        TextView typeWarning = popupView.findViewById(R.id.TypeWorkoutWarning);
        // Buttons
        Button confirmButton = popupView.findViewById(R.id.ConfirmWarningButton);
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);
        // Type Selection
        autoComplete = popupView.findViewById(R.id.AutoCompleteNewWorkouts);

        // --- Create the New View ---
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        Workout newWorkout = new Workout();

        // ------------------------------------------------------
        // ------------------ Dropdown Menu ---------------------
        // ------------------------------------------------------
        adapterItem = new ArrayAdapter<String>(this, R.layout.enum_list, typeInView);
        autoComplete.setAdapter(adapterItem);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                WorkoutService workoutService = new WorkoutService();
                workoutService.ApplyWorkoutType(newWorkout, item);

            }
        });

        // ------------- Buttons -------------
        confirmButton.setOnClickListener(v -> {

            boolean canCreate = false;

            if (!canCreate) {
                // ---------------------------------------------------------
                if (newWorkoutName.getText().toString().isEmpty() || newWorkoutName.getText().toString().length() >= 3) {
                    nameWarning.setVisibility(View.VISIBLE);
                    // ---------------------------------------------------------
                    if (!newWorkoutDescription.getText().toString().isEmpty() || newWorkoutDescription.getText().toString().length() >= 5) {
                        descriptionWarning.setVisibility(View.VISIBLE);
                    }
                    // ---------------------------------------------------------
                    if (newWorkout.wType == null) {
                        typeWarning.setVisibility(View.VISIBLE);
                    }
                }


                else {
                    nameWarning.setVisibility(View.GONE);
                    descriptionWarning.setVisibility(View.GONE);
                    typeWarning.setVisibility(View.GONE);

                    canCreate = true;
                }

            }


            if (canCreate) {
                // Inset New Workout in Database
                CreateNewWorkout(newWorkoutName.getText().toString(), newWorkoutDescription.getText().toString(), newWorkout.wType);
                ReloadRecyclerView();
                popupWindow.dismiss();     // Close Popup
            }

        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }


    //----------------------------------------------------
    //------------------- Change Plan --------------------
    //----------------------------------------------------
    public void ChangePlan(View view) {
        // -------------------------------------------------------
        // Open new Popup where user create a new Plan
        // -------------------------------------------------------
        // --- Inflate Activity with a new View ---
        View popupView = View.inflate(this, R.layout.popup_change_plan, null);

        // --- Popup View UI Content ---
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);
        // RecyclerView
        RecyclerView recyclerView1 = popupView.findViewById(R.id.RecyclerViewChangePlans);

        // --- Create the New View ---
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Database
                List<Plan> plansList = new ArrayList<>();
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                PlanDao dao = db.planDao();

                plansList = dao.listPlans();
                //plansList.add(thisPlan);

                // Set Recycler View Adapter
                PlanRVAdapter adapterPlan = new PlanRVAdapter(MainActivity.this, plansList, new PlanRVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Plan item) {
                        ShowAnotherPlan(item);
                        popupWindow.dismiss();
                    }
                });

                // Display Recycler View
                recyclerView1.setAdapter(adapterPlan);
                recyclerView1.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                ReloadRecyclerView();
            }
        }).start();

    }


    // --------------------------------------------------
    // ------------------ Active Plan -------------------
    // --------------------------------------------------
    public void ActivePlan(View view) {
        // PlanService planService = new PlanService;
        // planService.ActivePlan(thisPlan)
    }


    // -------------------------------------------------------
    // ------------------ Back Button Teste ------------------
    // -------------------------------------------------------




    // ------------------------------------------------------
    // ------- Switch for Navigation Bar Item List ----------
    // ------------------------------------------------------

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


    // -------------------------------------------------------------------
    // -------------------------------------------------------------------
    // ------------------------- FUNCTIONS -------------------------------
    // -------------------------------------------------------------------
    // -------------------------------------------------------------------

    // --------------------------------------------
    // ----------- Load Data and Display ----------
    // ------------- on Recycler View -------------
    // --------------------------------------------
    public void LoadData() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        UserDataDao dao = db.userDataDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // ------------------------------------
                // ----- Check if User is created -----
                // ------------------------------------
                List<UserData> userList = dao.listUserData();

                if (userList.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                } else {
                    user = userList.get(0);
                }

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

                //  Set Recycler View Adapter
                WorkoutRVAdapter adapter = new WorkoutRVAdapter(MainActivity.this, displayedWorkouts, new WorkoutRVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Workout item) {
                        Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
                        intent.putExtra("SelectedUser", user);
                        intent.putExtra("SelectedPlan", thisPlan);
                        intent.putExtra("SelectedWorkout", item);
                        startActivity(intent);
                    }
                });

                // Display Recycler View
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


                ChangeUIVisibility();

            }
        }).start();

    }


    // ---------------------------------------------------
    // ---------------- Reload Workouts ------------------
    // ---------------------------------------------------
    public void ReloadRecyclerView() {


        //count.setText(String.valueOf(displayedWorkouts.size()) + "/10");
        //ChangeUIVisibility();
    }


    // ----------------------------------------------
    // ------- Change UI Elements Visibility --------
    // ------- Based On Recycler View State ---------
    // ----------------------------------------------
    private void ChangeUIVisibility() {
        Button newWorkout = findViewById(R.id.NewWorkout);
        Button ActivePlanButton = findViewById(R.id.SetActiveButton2);
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
            ActivePlanButton.setVisibility(View.GONE);
            newWorkout.setVisibility(View.GONE);
            newPlan.setVisibility(View.GONE);
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
                newWorkout.setVisibility(View.GONE);
                emptyWorkoutButton.setText("Create New Workout");
                emptyWorkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewWorkout(emptyWorkoutButton);
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


    // -------------------------------------------------------
    // ---------- Display Another Plan on Screen  ------------
    // -------------------------------------------------------
    private void ShowAnotherPlan(Plan plan) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Plan> plansList = new ArrayList<>();

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                PlanDao dao = db.planDao();

                plansList = dao.listPlans();

                for (Plan item : plansList) {
                    if (item == plan) {
                        thisPlan = item;
                    }
                }

                ReloadRecyclerView();
            }
        }).start();

    }


    // ---------------------------------------------------------------------
    // --------------- Disable New Workout Button if -----------------------
    // --------------- Number of Workouts has Reached ----------------------
    // --------------- The Limit (Above 10)   ------------------------------
    // ---------------------------------------------------------------------
    private void CheckWorkoutLimit() {
        count.setText(String.valueOf(displayedWorkouts.size()) + "/10");

        if (displayedWorkouts.size() >= 10) {
            Button newWorkout = findViewById(R.id.NewWorkout);
            newWorkout.setVisibility(View.GONE);
        }
    }



    // -----------------------------------------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------- CHANGE TO SERVICE --------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------------------

    // ---------------------------------------------------
    // ---------------- Create New Plan ------------------
    // ---------------------------------------------------
    public void CreateNewPlan(String name, String description, boolean isActiveOrNot) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                LocalDate date = LocalDate.now();

                // Create New Workout DataBase
                Plan newPlan = new Plan();
                newPlan.planName = name;
                newPlan.planDescription = description;
                newPlan.pro = false;
                newPlan.author = user.name;
                newPlan.active = isActiveOrNot;
                newPlan.createdDate = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));


                ///////////////////////////////////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////////////////

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                        PlanDao dao = db.planDao();

                        dao.insertPlan(newPlan);

                        ShowAnotherPlan(newPlan);

                    }
                }).start();


                // Reload Recycle View
                ReloadRecyclerView();

            }
        }).start();

    }


    public void CreateNewWorkout(String name, String description, WorkoutType type) {
        // Create New Workout DataBase
        Workout newWorkout = new Workout();
        newWorkout.plan_Id = thisPlan.id;
        newWorkout.wName = name;
        newWorkout.wDescription = description;
        newWorkout.wType = Objects.requireNonNullElse(type, WorkoutType.NA);

        LocalDate date = LocalDate.now();
        newWorkout.lastModified = date.format(DateTimeFormatter.ofPattern("dd/MM"));

        // Access Database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insertWorkout(newWorkout);
            }
        }).start();

        ReloadRecyclerView();
    }


    // --------------------------------------------------
    // ------------------ Active Plan -------------------
    // --------------------------------------------------
    public void ActivePlan(Plan plan) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        // Variables
        Plan deactivePlan = new Plan();
        List<Plan> planList = new ArrayList<>();

        // Access Database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        // List All Plans from Databse
        planList = dao.listPlans();

        // Select only the Last Active Plan
        for (Plan item : planList) {
            if (item.active == true) {
                deactivePlan = item;
            }
        }
    }


    /////////////////////////////////////////
    /////////////// TESTE ///////////////////
    /////////////////////////////////////////

    public void teste(View view) {
        ChangeUIVisibility();
    }
}