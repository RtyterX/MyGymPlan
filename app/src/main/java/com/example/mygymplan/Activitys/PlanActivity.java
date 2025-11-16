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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mygymplan.Adapters.WorkoutRVAdapter;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.R;
import com.example.mygymplan.Services.ImageConverter;
import com.example.mygymplan.Services.PopupService;
import com.example.mygymplan.Services.WorkoutService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PlanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Entity's
    Plan thisPlan;
    List<Workout> displayedWorkouts = new ArrayList<>();
    PopupService popupService = new PopupService();

    // Shared Preferences
    String username;
    String email;
    String userImageString;

    // UI Elements
    TextView planName;
    Button noWorkoutButton;
    ItemTouchHelper mIth;

    // Buttons
    Button newWorkout;
    Button changePlan;
    Button changePlan2;

    // RecyclerView
    RecyclerView recyclerView;
    TextView count;
    WorkoutRVAdapter workoutAdapter;

    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActivityResultLauncher<Intent> resultLauncher;
    ImageView naviBarImage;
    ImageConverter imageConverter = new ImageConverter();

    // Others
    Button testButton;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_plan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_show_plan), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Can't Rotate the Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");

        Log.d("Selected Plan", "Selected Plan ID is: " + thisPlan.id);

        // --- Components ---
        planName = findViewById(R.id.PlanNameText);
        ImageView dbPlanIcon = findViewById(R.id.PlanFromDBIcon);
        count = findViewById(R.id.RVCount);
        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        // Buttons
        noWorkoutButton = findViewById(R.id.StartButton);
        newWorkout = findViewById(R.id.NewWorkout);
        changePlan = findViewById(R.id.ChangePlanButton);
        changePlan2 = findViewById(R.id.ChangePlan2Button);


        // ---- Show Workouts in Recycle View or -----
        // ---- Display Create From Scratch button ---
        GetWorkoutList();


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
        //Bitmap bitmap2 = imageConverter.ConvertToBitmap(userImageString);
        //naviBarImage.setImageBitmap(bitmap2);


        naviBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });


        // Check if plan is from app DB
        if (thisPlan != null) {
            if (Objects.equals(thisPlan.author, "My Gym Plan")) {
                dbPlanIcon.setVisibility(View.VISIBLE);
            } else {
                dbPlanIcon.setVisibility(View.GONE);
            }
        }


        //  --- Go to Test Activity ---
        testButton = navigationView.findViewById(R.id.TestButton);
        // JUST FOR DEBUG ------------------------------------
        // JUST FOR DEBUG ------------------------------------



        // -------------------------------------------
        // ----------------- BUTTONS -----------------
        // -------------------------------------------
        noWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewWorkout();
            }
        });

        newWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewWorkout();
            }
        });

        changePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ChangePlan(); }
        });

        changePlan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ChangePlan(); }
        });

        planName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if plan inst from app DB
                if (thisPlan != null) {
                    if (!Objects.equals(thisPlan.author, "My Gym Plan")) {
                        EditPlanName();
                    }
                }
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
        //------------------
    }

    // ---------------------------------------------------------------------------------------------


    public void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    public void RegisterResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (false) { //&& data != null && data.getData() != null) {
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

                    // } else if (resultCode == RESULT_CANCELED) {

                }
            }
        });

    }

    // ------------------------------------------------------
    // ---------------- Change Plan  ------------------------
    // ------------------------------------------------------
    public void ChangePlan() {
        Intent intent = new Intent(PlanActivity.this, SelectPlanActivity.class);
        startActivity(intent);
    }

    // ------------------------------------------------------
    // ---------------- Create New Workout ------------------
    // ------------------------------------------------------
    public void NewWorkout() {
        popupService.NewWorkoutPopup(this, this, thisPlan);
    }

    // ------------------------------------------------------
    // ---------------- Edit Workout ------------------------
    // ------------------------------------------------------
    private void EditWorkout(Workout workout) { popupService.EditWorkoutPopup(this, this, thisPlan, workout); }

    // ------------------------------------------------------
    // ------------- Edit User Plan Name  -------------------
    // ------------------------------------------------------
    public void EditPlanName() {
        popupService.editUserPlanName(this, this, thisPlan);
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
    public void GetWorkoutList() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        WorkoutDao daoW = db.workoutDao();
        displayedWorkouts = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Workout> workoutList = daoW.listWorkouts();

                if (!workoutList.isEmpty()) {
                    for (Workout item : workoutList) {
                        if (item.plan_Id == thisPlan.id) {
                            displayedWorkouts.add(item);
                        }
                    }
                }

                if (thisPlan.fixedDays) {
                    // Sort by Order
                    displayedWorkouts.sort((w1, w2) -> Integer.compare(w1.dayOfWeek.getValue(), w2.dayOfWeek.getValue()));
                }
                else {
                    // Sort by Order
                    displayedWorkouts.sort((w1, w2) -> Integer.compare(w1.order, w2.order));
                }

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!displayedWorkouts.isEmpty()) {
                            UpdateRecyclerView();
                        }
                        else {
                            ChangeUIVisibility();
                        }
                    }
                });

            }
        }).start();

        db.close();
    }


    // ---------------------------------------------------
    // ---------------- Reload Workouts ------------------
    // ---------------------------------------------------
    public void UpdateRecyclerView() {
        if (!displayedWorkouts.isEmpty()) {
            Log.d("Workouts RecyclerView", "Workouts Recycler View Ok");
            workoutAdapter = new WorkoutRVAdapter(PlanActivity.this, displayedWorkouts, new WorkoutRVAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Workout item) {
                    SetNextWorkout(item, thisPlan);
                    // Change Activity
                    Intent intent = new Intent(PlanActivity.this, WorkoutActivity.class);
                    intent.putExtra("SelectedPlan", thisPlan);
                    intent.putExtra("SelectedWorkout", item);
                    startActivity(intent);
                }
            }, new WorkoutRVAdapter.OnClickEditListener() {
                @Override
                public void editButtonClick(Workout item) {
                    EditWorkout(item);
                }
            });
            // Display Workouts in Recycler View
            recyclerView.setAdapter(workoutAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(PlanActivity.this));
            // Attach Item Helper
            mIth.attachToRecyclerView(recyclerView);

            ChangeUIVisibility();
        } else {
            Log.d("Workouts RecyclerView", "Recycler View has 0 Workouts");
        }

    }


    // ----------------------------------------------
    // ------- Change UI Elements Visibility --------
    // ------- Based On Recycler View State ---------
    // ----------------------------------------------
    private void ChangeUIVisibility() {
        // ----------------------
        // ----- NO WORKOUT -----
        // ----------------------
        if (displayedWorkouts.isEmpty()) {
            // -- Recycler View --
            recyclerView.setVisibility(View.GONE);
            // -- Workout Number Count --
            count.setVisibility(View.GONE);
            // -- Buttons --
            noWorkoutButton.setVisibility(View.VISIBLE);
            changePlan2.setVisibility(View.VISIBLE);
            changePlan.setVisibility(View.GONE);
            newWorkout.setVisibility(View.GONE);
        } else {
            // -------------------------
            // ----- WITH WORKOUT ------
            // -------------------------
            recyclerView.setVisibility(View.VISIBLE);
            // -- Workout Number Count --
            count.setVisibility(View.VISIBLE);
            // -- Buttons --
            changePlan2.setVisibility(View.GONE);
            noWorkoutButton.setVisibility(View.GONE);
            changePlan.setVisibility(View.VISIBLE);
            newWorkout.setVisibility(View.VISIBLE);

            CheckWorkoutLimit();
        }

        // Set Plan Name with or without workouts
        planName.setText(thisPlan.name);
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


    // --------------------------------------------------------
    // --------------- Decide what will be the  ---------------
    // ------------------ Next Day Workout  -------------------
    // --------------------------------------------------------
    private void SetNextWorkout(Workout workout, Plan plan) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int activePlanId = sharedPreferences.getInt("activePlanId", 0);

        // Block if Workout is not From Active Plan
        if (plan.id == activePlanId) {
            // Check if Active Plan haven't Fixed Days
            if (!plan.fixedDays) {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // ------------------------------------------------------
                        WorkoutDao dao = db.workoutDao();
                        List<Workout> allWorkouts = dao.listWorkouts();

                        for (Workout item : allWorkouts) {
                            if (item.plan_Id == activePlanId) {
                                if (item.order == (workout.order + 1)) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("nextWorkoutId", item.id);
                                    editor.apply();
                                    break;
                                }
                            }
                        }
                    }
                }).start();

                db.close();
            }
        }
    }

    //////////////////////// END ////////////////////////
}