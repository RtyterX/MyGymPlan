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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.example.mygymplan.Adapters.PlanRVAdapter;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.R;
import com.example.mygymplan.Services.PlanService;
import com.example.mygymplan.Services.PopupService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Plan> planList = new ArrayList<>();
    PlanRVAdapter adapter;

    // Shared Preferences
    String username;
    String email;

    // UI
    RecyclerView recyclerView;
    TextView emptyText;
    TextView count;

    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    PopupService popupService = new PopupService();


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_plan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Can't Rotate the Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // --- Components ---
        recyclerView = findViewById(R.id.RecycleViewPlans);
        emptyText = findViewById(R.id.EmptyPlanRVText);
        count = findViewById(R.id.TotalPlansCount);
        Button myPlans = findViewById(R.id.MyPlansButton);
        Button databasePlans = findViewById(R.id.DatabasePlansButton);
        Button backButton = findViewById(R.id.BackButton2);

        // -------------------------------
        LoadDatabasePlans();
        LoadPrefs();

        // --- Drawer Layout ---
        Toolbar toolbar = findViewById(R.id.toolbar2);                                         // Find Toolbar
        setSupportActionBar(toolbar);                                                          // Set Toolbar as ActionBar
        drawerLayout = findViewById(R.id.DrawerLayout);                                        // Find DrawerLayout
        // drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
        navigationView = findViewById(R.id.NavView);                                           // Find Navigation View
        navigationView.setNavigationItemSelectedListener(this);                                // Only Works if class: implements NavigationView.OnNavigationItemSelectedListener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);                       // Set ActionBar (Hamburger Menu)
        drawerLayout.addDrawerListener(toggle);                                                // Set Click on ActionBar
        toggle.syncState();                                                                    // Sync with drawer state (Open/Close)

        // NaviBar Values
        View headerView = navigationView.getHeaderView(0);
        TextView userNameText = headerView.findViewById(R.id.UsernameNaviBar);
        TextView userEmailText = headerView.findViewById(R.id.UserEmailNaviBar);
        ImageView userPhoto = headerView.findViewById(R.id.UserPhotoNaviBar);
        userNameText.setText(username);
        userEmailText.setText(email);
        // userPhoto.setImageResource();



        // ----- Buttons -----
        myPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadMyPlans();
            }
        });

        databasePlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDatabasePlans();
            }
        });

        // Only for Testing
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plan emptyPlan = new Plan(); // Just to not Crash
                Intent intent = new Intent(PlanActivity.this, MainActivity.class);
                intent.putExtra("SelectedPlan", emptyPlan);
                startActivity(intent);
            }
        });

    }

    private void LoadPrefs() {
        // Check if its user First time opening App
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        email = sharedPreferences.getString("email", "");
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
            Intent intent = new Intent(this, PlanActivity.class);
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


    public void LoadMyPlans() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        PlanDao daoW = db.planDao();
        planList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Plan> allPlans = daoW.listPlans();

                if (!allPlans.isEmpty()) {
                    for (Plan item : allPlans) {
                        if (!Objects.equals(item.author, "MyGymPlan")) {
                            planList.add(item);
                        }
                    }
                }

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateRecyclerView(planList);
                    }
                });
            }
        }).start();

        db.close();
    }

    public void LoadDatabasePlans() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        PlanDao daoW = db.planDao();
        planList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Plan> allPlans = daoW.listPlans();

                if (!allPlans.isEmpty()) {
                    for (Plan item : allPlans) {
                        if (Objects.equals(item.author, "MyGymPlan")) {
                            planList.add(item);
                        }
                    }
                }

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateRecyclerView(planList);
                    }
                });
            }
        }).start();

        db.close();
    }

    public void UpdateRecyclerView (List<Plan> list) {
        if (!list.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new PlanRVAdapter(PlanActivity.this, list, new PlanRVAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Plan item) {
                    Intent intent = new Intent(PlanActivity.this, MainActivity.class);
                    intent.putExtra("SelectedPlan", item);
                    startActivity(intent);
                }
            }, new PlanRVAdapter.OnItemClickDelete() {
                @Override
                public void deleteButtonClick(int position) {
                    PlanService planService = new PlanService();
                    planService.deletePlan(getApplicationContext(), planList.get(position));
                    planList.remove(planList.get(position));
                    adapter.notifyItemRemoved(position);

                    LoadMyPlans();
                }
            }, new PlanRVAdapter.OnItemClickSetActive() {
                @Override
                public void setActiveButtonClick(Plan plan) {
                    PlanService planService = new PlanService();
                    planService.activePlan(getApplicationContext(), plan);

                    LoadMyPlans();
                }
            });
            // Display Workouts in Recycler View
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(PlanActivity.this));
            // Change UI
            count.setText("Total de Planos: " + planList.size());
            emptyText.setVisibility(View.GONE);
        } else {
            // Change UI
            recyclerView.setVisibility(View.GONE);
            count.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    //////////////////////// END ////////////////////////
}