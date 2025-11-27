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
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.mygymplan.Services.ImageConverter;
import com.example.mygymplan.Services.PlanService;
import com.example.mygymplan.Services.PopupService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectPlanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Plan> dbPlanList = new ArrayList<>();
    List<Plan> userPlanList = new ArrayList<>();
    PlanRVAdapter adapter;

    // Shared Preferences
    String username;
    String email;

    // UI
    RecyclerView RVDbPlans;
    RecyclerView RVUserPlans;
    TextView emptyText;
    TextView count1;
    TextView count2;


    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView naviBarImage;
    ImageConverter imageConverter = new ImageConverter();
    String userImageString;
    ActivityResultLauncher<Intent> resultLauncher;

    PopupService popupService = new PopupService();


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_plan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Can't Rotate the Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // --- Components ---
        RVDbPlans = findViewById(R.id.RecycleViewDbPlans);
        RVUserPlans = findViewById(R.id.RecycleViewMyPlans);
        emptyText = findViewById(R.id.EmptyPlanRVText);
        count1 = findViewById(R.id.DbPlansCount);
        count2 = findViewById(R.id.MyPlansCount);
        Button myPlans = findViewById(R.id.MyPlansButton);
        Button databasePlans = findViewById(R.id.DatabasePlansButton);
        Button backButton = findViewById(R.id.BackButton2);
        Button newPlan = findViewById(R.id.CreateNewPlanButton);

        // -------------------------------
        LoadPlans();
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


        RegisterResult();

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


        naviBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });


        // ----- Buttons -----
        newPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPlan();
            }
        });

        // -----------------------------------------------------
        // Only for Testing
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plan emptyPlan = new Plan(); // Just to not Crash
                Intent intent = new Intent(SelectPlanActivity.this, MainActivity.class);
                intent.putExtra("SelectedPlan", emptyPlan);
                startActivity(intent);
            }
        });

    }



    // --------------------------------------------------------------------------------------------------

    public void NewPlan() {
        popupService.NewPlanActivityPopup(this, this, username);
    }

    private void LoadPrefs() {
        // Check if its user First time opening App
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        email = sharedPreferences.getString("email", "");
        userImageString = sharedPreferences.getString("userImageString", "");
    }


    // ------------------------------------------------------
    // --------- Pick Image From Gallery (NaviBar) ----------
    // ------------------------------------------------------
    public void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    public void RegisterResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Uri imageUri = result.getData().getData();
                naviBarImage.setImageURI(imageUri);

                // Convert Image to Bitmap
                Drawable drawable = naviBarImage.getDrawable();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();

                // Convert to String
                userImageString = imageConverter.ConvertToString(bitmap);

                // Check if its user First time opening App
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();        // Insert in Shared Preferences
                editor.putString("userImageString", userImageString);
                editor.apply();

                // Re Create App
                recreate();
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

        // Close Drawer after Click
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void LoadPlans() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                PlanDao daoW = db.planDao();

                // List All Plans
                List<Plan> allPlans = daoW.listPlans();

                // Load Database Plans
                for (Plan item : allPlans) {
                    if (Objects.equals(item.author, "MyGymPlan")) {
                        dbPlanList.add(item);
                    }
                }

                // Load User Plans
                for (Plan item : allPlans) {
                    if (!Objects.equals(item.author, "MyGymPlan")) {
                        userPlanList.add(item);
                    }
                }

                db.close();

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SetRecyclerViewAdapter(dbPlanList, RVDbPlans);
                        SetRecyclerViewAdapter(userPlanList, RVUserPlans);
                        ChangeUIVisibility();
                    }
                });

            }
        }).start();
    }


    public void SetRecyclerViewAdapter(List<Plan> list, RecyclerView recyclerView) {

        adapter = new PlanRVAdapter(SelectPlanActivity.this, list, new PlanRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Plan item) {
                Intent intent = new Intent(SelectPlanActivity.this, PlanActivity.class);
                intent.putExtra("SelectedPlan", item);
                startActivity(intent);
            }
        }, new PlanRVAdapter.OnItemClickDelete() {
            @Override
            public void deleteButtonClick(int position) {
                PlanService planService = new PlanService();
                planService.deletePlan(getApplicationContext(), dbPlanList.get(position));
                dbPlanList.remove(dbPlanList.get(position));
                adapter.notifyItemRemoved(position);

                LoadPlans();
            }
        }, new PlanRVAdapter.OnItemClickSetActive() {
            @Override
            public void setActiveButtonClick(Plan plan) {
                PlanService planService = new PlanService();
                planService.setActivePlan(getApplicationContext(), plan);

                LoadPlans();
            }
        }, new PlanRVAdapter.OnClickEditPlanListener() {
            @Override
            public void editButtonClick(Plan plan) {
                Intent intent = new Intent(SelectPlanActivity.this, MainActivity.class);
                intent.putExtra("SelectedPlan", plan);
                startActivity(intent);
            }
        });
        // Display Workouts in Recycler View
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectPlanActivity.this, LinearLayoutManager.HORIZONTAL, false));

    }


    private void ChangeUIVisibility() {

        count1.setText("Total de Planos: " + dbPlanList.size());

        if (userPlanList != null) {
            count2.setText("Total de Planos: " + userPlanList.size());
            emptyText.setVisibility(View.GONE);
            RVUserPlans.setVisibility(View.VISIBLE);
        } else {
            // Change UI
            RVUserPlans.setVisibility(View.GONE);
            count2.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    public void AddPlanToSelection() {

    }

    //////////////////////// END ////////////////////////
}