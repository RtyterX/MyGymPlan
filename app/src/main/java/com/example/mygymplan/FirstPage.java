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
import androidx.activity.OnBackPressedDispatcher;
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

import com.example.mygymplan.Adapters.WorkoutRVAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class FirstPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Data
    UserData user;
    Plan thisPlan;
    List<Workout> displayedWorkouts;
    List<UserData> userList;


    // UI
    TextView planName;
    Button emptyPlanButton;
    Button emptyWorkoutButton;


    // RecyclerView
    WorkoutRVAdapter adapter;
    RecyclerView recyclerView;
    TextView emptyView;
    TextView count;


    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button testButton;



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


        // ----------------------------------
        // ---------- NOT WORKING -----------
        // ----------------------------------

        // Edit Active Workout Plan
        ChangePlanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(FirstPage.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);

                startActivity(intent);
            }
        });

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
            }
            else if (newPlanDescription.getText().toString().isEmpty()) {
                newPlanDescripWarning.setVisibility(View.VISIBLE);
            }
            else {

                NewPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString());

            }

        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }


    // ---------------------------------------------------------------------
    // ------------- Check if user Already has Workouts --------------------
    // ---------------------------------------------------------------------

    private void ChangeEmptyState(){
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
        }
        else {
            if (displayedWorkouts.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyPlanButton.setVisibility(View.VISIBLE);
                emptyPlanButton.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.VISIBLE);
                Button editActualPlanButton = findViewById(R.id.ChangePlan);
                editActualPlanButton.setVisibility(View.GONE);
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
            }
            else {
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

        if (displayedWorkouts.size() >= 10)
        {
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

                for(Plan item : plansList){
                    if (item.active == true) {
                        thisPlan = item;
                    }
                }

                displayedWorkouts = new ArrayList<>();
                List<Workout> workoutsList = new ArrayList<>();

                WorkoutDao daoW = db.workoutDao();
                workoutsList = daoW.listWorkouts();

                for(Workout item : workoutsList){
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

    public void NewPlan(String name, String description) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Create New Workout DataBase
                Plan newPlan = new Plan();
                newPlan.planName = name;
                newPlan.planDescription = description;
                newPlan.pro = false;
                newPlan.author = user.name;
                newPlan.active = true;

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                PlanDao dao = db.planDao();

                dao.insertPlan(newPlan);

                // Create New Workout DataBase
                Workout newWorkout = new Workout();
                newWorkout.wName = "New Plan Workout";
                newWorkout.wDescription = "New Plan Description";
                newWorkout.plan_Id = thisPlan.id;

                WorkoutDao dao2 = db.workoutDao();
                dao2.insertWorkout(newWorkout);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Change Activity
                        Intent intent = new Intent(FirstPage.this, FirstPage.class);
                        intent.putExtra("SelectedUser", user);
                        intent.putExtra("SelectedPlan", newPlan);

                        startActivity(intent);
                    }
                });

            }
        }).start();

    }


    // ------------------------------------------------------
    // ---------------- Create New Workout ------------------
    // ---------------------------------------------------

    public void NewWorkout(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Create New Workout DataBase
                Workout newWorkout = new Workout();
                newWorkout.wName = "New Workout";
                newWorkout.plan_Id = thisPlan.id;

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                WorkoutDao dao = db.workoutDao();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Change Activity
                        Intent intent = new Intent(FirstPage.this, ShowWorkoutActivity.class);
                        intent.putExtra("SelectedWorkout", newWorkout);

                        startActivity(intent);
                    }
                });

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