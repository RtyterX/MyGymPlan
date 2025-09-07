package com.example.mygymplan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import com.google.android.material.navigation.NavigationView;

import java.util.List;

// Alt + Enter = Import Classes

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Data
    UserData user;
    Plan thisPlan;
    List<Workout> displayedWorkouts;


    // UI
    TextView planName;
    Button emptyButton;
    Button testButton;
    DrawerLayout drawerLayout;

    NavigationView navigationView;

    // RecyclerView
    WorkoutRVAdapter adapter;
    RecyclerView recyclerView;
    TextView emptyView;

    TextView count;


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


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");
        

        // Components
        planName = findViewById(R.id.MyWorkoutPlanText);
        Button editActualPlanButton = findViewById(R.id.EditActualPlan);
        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyView = findViewById(R.id.EmptyRVWorkouts2);
        emptyButton = findViewById(R.id.EmptyPlanButton);
        count = findViewById(R.id.RVCount);


        //  Drawer Layout
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.DrawerLayout);
        navigationView = findViewById(R.id.NavView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        testButton = navigationView.findViewById(R.id.TestButton);



        // Set Values based on Received Data
        // Needed??


        LoadData();
        //CheckIfHasUserData(); // For user Name or what ever


        // ----- BUTTONS -----

        // Edit Active Workout Plan
        editActualPlanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(MainActivity.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);

                startActivity(intent);
            }
        });


    }


    // ---------- BUTTONS -----------

    public void NewWorkout(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Create New Workout DataBase
                Workout newWorkout = new Workout();
                newWorkout.wName = "New Workout";

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                WorkoutDao dao = db.workoutDao();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Change Activity
                        Intent intent = new Intent(MainActivity.this, ShowWorkoutActivity.class);
                        intent.putExtra("SelectedWorkout", newWorkout);

                        startActivity(intent);
                    }
                });

            }
        }).start();

    }


    // ---------- METHODS -----------

    private void CheckIfHasUserData() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        UserDataDao dao = db.userDataDao();

        List<UserData> user = dao.listUserData();

        if (user.isEmpty())
        {
            // Open New User Window
        }

    }

    private void CheckEmptyState(){
        if (displayedWorkouts.isEmpty()) {
            // Hide all usable Workout Buttons
            recyclerView.setVisibility(View.GONE);
            Button editActualPlanButton = findViewById(R.id.EditActualPlan);
            editActualPlanButton.setVisibility(View.GONE);
            Button newWorkout = findViewById(R.id.NewWorkout);
            newWorkout.setVisibility(View.GONE);
            // Show Message and Create new Button
            emptyButton.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
            planName.setText("No Plans");            // Just for Tests
        }
        else {
            // Show Recycle View
            recyclerView.setVisibility(View.VISIBLE);
            // Hide Empty UI
            emptyButton.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            planName.setText("Tyter é foda");        // Just for Tests
        }
    }


    public void LoadData() {
        new Thread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                WorkoutDao dao = db.workoutDao();

                displayedWorkouts = dao.listWorkouts();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        CheckWorkoutLimit();

                        //  Set Recycler View Adapter
                        WorkoutRVAdapter adapter = new WorkoutRVAdapter(MainActivity.this, displayedWorkouts, new WorkoutRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Workout item) {
                                Intent intent = new Intent(MainActivity.this, ShowWorkoutActivity.class);

                                intent.putExtra("SelectedWorkout", item);
                                startActivity(intent);
                            }
                        });

                        // Display Recycler View
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                        CheckEmptyState();
                    }

                });

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

    private void CheckWorkoutLimit() {
        count.setText(String.valueOf(displayedWorkouts.size()) + "/10");

        if (displayedWorkouts.size() >= 10)
        {
            Button newWorkout = findViewById(R.id.NewWorkout);
            newWorkout.setVisibility(View.GONE);
        }
    }


}