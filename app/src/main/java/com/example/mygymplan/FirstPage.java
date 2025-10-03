package com.example.mygymplan;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.mygymplan.Adapters.PlanRVAdapter;
import com.example.mygymplan.Adapters.WorkoutRVAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirstPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Data
    UserData user;
    Plan thisPlan;
    List<Workout> displayedWorkouts;


    // UI
    TextView planName;
    Button emptyPlanButton;
    Button emptyWorkoutButton;


    // RecyclerView
    RecyclerView recyclerView;
    TextView emptyView;
    TextView count;


    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button testButton;


    // TESTE
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
        setContentView(R.layout.activity_first_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");
        user = (UserData) intent.getSerializableExtra("SelectedUser");


        // --- Components ---
        planName = findViewById(R.id.MyWorkoutPlanText);
        Button ChangePlanButton = findViewById(R.id.ChangePlan);
        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyView = findViewById(R.id.EmptyRVWorkouts2);
        emptyPlanButton = findViewById(R.id.EmptyPlanButton);
        count = findViewById(R.id.RVCount);


        // --- Drawer Layout ---
        Toolbar toolbar = findViewById(R.id.toolbar2);                             // Find Toolbar
        setSupportActionBar(toolbar);                                              // Set Toolbar as ActionBar
        getSupportActionBar().setIcon(R.drawable.mygymplan_logo);                  // Set Logo on Toolbar
        drawerLayout = findViewById(R.id.DrawerLayout);                            // Find DrawerLayout
        navigationView = findViewById(R.id.NavView);                               // Find Navigation View
        navigationView.setNavigationItemSelectedListener(this);                    // Only Works if class: implements NavigationView.OnNavigationItemSelectedListener

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.app_name, R.string.app_name);      // Set ActionBar Toggle
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


    // ----------------------------------------------------------------
    // ---------------- If User Has no Workout Plans ------------------
    // ----------------------------------------------------------------
    public void CreateFromScratch(View view) {
        // Open new Popup where user create a new Plan
        // -------------------------------------------------------
        // Inflate Activity with a new View
        View popupView = View.inflate(this, R.layout.new_plan_popup, null);

        // Popup View UI Content
        EditText newPlanName = popupView.findViewById(R.id.NewPlanName);
        EditText newPlanDescription = popupView.findViewById(R.id.NewPlanDescription);
        TextView newPlanNameWarning = popupView.findViewById(R.id.NewPlanDescription);
        TextView newPlanDescripWarning = popupView.findViewById(R.id.NewPlanDescription);
        Button confirmButton = popupView.findViewById(R.id.ConfirmWarningButton);
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);

        // Set height and width as WRAP_CONTENT
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;

        // Create the New View
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
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
                PopupWindow subPopupWindow = new PopupWindow(subView, width, height, true);
                subPopupWindow.showAtLocation(subView, Gravity.CENTER, 0, 0);

                // Set Text Warning
                popupText.setText("Gostaria de deixar o Plano como Ativo?");
                yesButton.setText("Yes");
                noButton.setText("No");

                // ------ Buttons ------
                yesButton.setOnClickListener(subV -> {

                    NewPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString(), true);
                    popupWindow.dismiss();
                    subPopupWindow.dismiss();

                });

                noButton.setOnClickListener(subV -> {

                    NewPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString(), false);
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


    // ---------------------------------------------------------------------
    // ------------- Check if user Already has Workouts --------------------
    // ---------------------------------------------------------------------

    private void ChangeEmptyState() {
        if (thisPlan == null) {
            recyclerView.setVisibility(View.GONE);
            emptyPlanButton.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
            Button editActualPlanButton = findViewById(R.id.ChangePlan);
            editActualPlanButton.setVisibility(View.GONE);
            Button newWorkout = findViewById(R.id.NewWorkout);
            newWorkout.setVisibility(View.GONE);
            count.setVisibility(View.GONE);
            planName.setText("No Plans");
        } else {
            if (displayedWorkouts.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyPlanButton.setVisibility(View.VISIBLE);
                emptyPlanButton.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.VISIBLE);
                Button newWorkout = findViewById(R.id.NewWorkout);
                newWorkout.setVisibility(View.GONE);
                count.setVisibility(View.GONE);
                emptyWorkoutButton = findViewById(R.id.EmptyPlanButton);
                emptyWorkoutButton.setText("Create New Workout");
                emptyWorkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewWorkout(emptyWorkoutButton);
                    }
                });
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyPlanButton.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                CheckWorkoutLimit();
            }
            planName.setText(thisPlan.planName);
        }
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


    // --------------------------------------------------------------------
    // ----------- Load Data and display on Recycler View -----------------
    // --------------------------------------------------------------------

    public void LoadData() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                List<Plan> plansList = new ArrayList<>();

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                PlanDao dao = db.planDao();

                plansList = dao.listPlans();

                for (Plan item : plansList) {
                    if (item.active == true) {
                        thisPlan = item;
                    }
                }

                displayedWorkouts = new ArrayList<>();
                List<Workout> workoutsList = new ArrayList<>();

                WorkoutDao daoW = db.workoutDao();
                workoutsList = daoW.listWorkouts();

                for (Workout item : workoutsList) {
                    if (item.plan_Id == thisPlan.id) {
                        displayedWorkouts.add(item);
                    }
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //  Set Recycler View Adapter
                        WorkoutRVAdapter adapter = new WorkoutRVAdapter(FirstPage.this, displayedWorkouts, new WorkoutRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Workout item) {
                                Intent intent = new Intent(FirstPage.this, ShowWorkoutActivity.class);
                                intent.putExtra("SelectedUser", user);
                                intent.putExtra("SelectedWorkout", item);
                                startActivity(intent);
                            }
                        });

                        // Display Recycler View
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FirstPage.this));


                        ChangeEmptyState();
                    }

                });

            }
        }).start();

    }


    // ---------------------------------------------------
    // ---------------- Create New Plan ------------------
    // ---------------------------------------------------

    public void NewPlan(String name, String description, boolean isActiveOrNot) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Create New Workout DataBase
                Plan newPlan = new Plan();
                newPlan.planName = name;
                newPlan.planDescription = description;
                newPlan.pro = false;
                newPlan.author = user.name;
                newPlan.active = isActiveOrNot;

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                        PlanDao dao = db.planDao();

                        dao.insertPlan(newPlan);

                        ShowPlan(newPlan);

                    }
                }).start();


                // Reload Recycle View
                LoadData();

            }
        }).start();

    }


    // ------------------------------------------------------
    // ---------------- Create New Workout ------------------
    // ---------------------------------------------------

    public void NewWorkout(View view) {
        // -------------------------------------------------------
        // Open new Popup where user create a new Plan
        // -------------------------------------------------------
        // Inflate Activity with a new View
        View popupView = View.inflate(this, R.layout.new_workout_popup, null);

        // --- Popup View UI Content ---
        // Values
        EditText newWorkoutName = popupView.findViewById(R.id.NewWorkoutName);
        EditText newWorkoutDescription = popupView.findViewById(R.id.NewWorkoutDescription);
        // Warnings
        TextView nameWarning = popupView.findViewById(R.id.NewWorkoutName);
        TextView descriptionWarning = popupView.findViewById(R.id.NewWorkoutDescription);
        // Buttons
        Button confirmButton = popupView.findViewById(R.id.ConfirmWarningButton);
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);
        // Type Selection
        autoComplete = popupView.findViewById(R.id.AutoCompleteNewWorkouts);

        // --- Set height and width as WRAP_CONTENT ---
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;

        // --- Create the New View ---
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
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
                // Apply Type
                switch (item) {
                    case "Chest":
                        newWorkout.wType = WorkoutType.Chest;
                        break;
                    case "Back":
                        newWorkout.wType = WorkoutType.Back;
                        break;
                    case "Shoulder":
                        newWorkout.wType = WorkoutType.Shoulder;
                        break;
                    case "Arms":
                        newWorkout.wType = WorkoutType.Arms;
                        break;
                    case "Biceps":
                        newWorkout.wType = WorkoutType.Biceps;
                        break;
                    case "Triceps":
                        newWorkout.wType = WorkoutType.Triceps;
                        break;
                    case "Legs":
                        newWorkout.wType = WorkoutType.Legs;
                        break;
                }
            }
        });

        // ------------- Buttons -------------
        confirmButton.setOnClickListener(v -> {
            if (newWorkoutName.getText().toString().isEmpty()) {
                nameWarning.setVisibility(View.VISIBLE);
            } else if (newWorkoutDescription.getText().toString().isEmpty()) {
                descriptionWarning.setVisibility(View.VISIBLE);
            } else {
                // Inset New Workout in Database
                CreateNewWorkout(newWorkoutName.getText().toString(), newWorkoutDescription.getText().toString(), newWorkout.wType);
                popupWindow.dismiss();     // Close Popup
            }

        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }

    public void CreateNewWorkout(String name, String description, WorkoutType type) {
        // Create New Workout DataBase
        Workout newWorkout = new Workout();
        newWorkout.plan_Id = thisPlan.id;
        newWorkout.wName = name;
        newWorkout.wDescription = description;
        newWorkout.wType = Objects.requireNonNullElse(type, WorkoutType.NA);

        // Access Database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insertWorkout(newWorkout);
            }
        }).start();

        // Reload Recycle View
        LoadData();

    }


    //----------------------------------------------------
    //----------------= Change Plan ----------------
    //----------------------------------------------------
    public void ChangePlan(View view) {
        // -------------------------------------------------------
        // Open new Popup where user create a new Plan
        // -------------------------------------------------------
        // --- Inflate Activity with a new View ---
        View popupView = View.inflate(this, R.layout.change_plan, null);

        // --- Popup View UI Content ---
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);

        // RecyclerView
        RecyclerView recyclerView1 = popupView.findViewById(R.id.RecyclerViewChangePlans);



        // --- Set height and width as WRAP_CONTENT ---
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;

        // --- Create the New View ---
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
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
                PlanRVAdapter adapterPlan = new PlanRVAdapter(FirstPage.this, plansList, new PlanRVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Plan item) {
                        ShowPlan(item);
                        popupWindow.dismiss();
                    }
                });

                // Display Recycler View
                recyclerView1.setAdapter(adapterPlan);
                recyclerView1.setLayoutManager(new LinearLayoutManager(FirstPage.this));


            }
        }).start();



    }





    private void ShowPlan(Plan plan) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                displayedWorkouts = new ArrayList<>();
                List<Workout> workoutsList = new ArrayList<>();

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                WorkoutDao daoW = db.workoutDao();

                workoutsList = daoW.listWorkouts();
                for (Workout item : workoutsList) {
                    if (item.plan_Id == plan.id) {
                        displayedWorkouts.add(item);
                    }
                }

                //  Set Recycler View Adapter
                WorkoutRVAdapter adapter = new WorkoutRVAdapter(FirstPage.this, displayedWorkouts, new WorkoutRVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Workout item) {
                        Intent intent = new Intent(FirstPage.this, ShowWorkoutActivity.class);
                        intent.putExtra("SelectedUser", user);
                        intent.putExtra("SelectedWorkout", item);
                        startActivity(intent);
                    }
                });

                // Display Recycler View
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(FirstPage.this));

                ChangeEmptyState();

            }
        }).start();

    }




    // ---------------------------------------------------
    // ------- Switch for Navigation Bar Item List -------
    // ---------------------------------------------------

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(this, FirstPage.class);
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



    // ----------------------------------------
    // ------------ Active Plan ---------------
    // ----------------------------------------
     public void ActivePlan(Plan plan) {
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

         // Change Active between plans
         plan.active = true;
         deactivePlan.active = false;

         // Update plans in database
         dao.updatePlan(plan);
         dao.updatePlan(deactivePlan);


     }





    // -------------------------------------------------------------------------------------
    // -------------------------------------
    // -------- Back Button Teste ----------
    // -------------------------------------
    // -------------------------------------------------------------------------------------
    OnBackPressedDispatcher teste;

    public OnBackPressedDispatcher getTeste() {
        Intent intent = new Intent(this, TesteActivity.class);
        startActivity(intent);
        return teste;
    }









}