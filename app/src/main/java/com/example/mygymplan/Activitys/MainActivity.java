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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.mygymplan.Services.PopupService;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Entity's
    Plan thisPlan;
    Workout todaysWorkout;
    PopupService popupService = new PopupService();

    // Shared Preferences
    String username;
    String email;
    String userImageString;


    // UI Elements
    ItemTouchHelper mIth;


    // RecyclerView
    RecyclerView plansRV;
    RecyclerView workoutRV;


    List<Plan> planList = new ArrayList<>();;

    List<Workout> workoutsList = new ArrayList<>();;

    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Button createPlan;
    TextView noWorkout;

    // Others
    Button testButton;

    // Test
    ActivityResultLauncher<Intent> resultLauncher;
    ImageView naviBarImage;

    ImageConverter imageConverter = new ImageConverter();

    WorkoutRVAdapter workoutAdapter;

    

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Can't Rotate the Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // --- Components ---
        TextView mainText = findViewById(R.id.MainTitle);
        workoutRV = findViewById(R.id.MainRVWorkouts);
        plansRV = findViewById(R.id.MainRVPlans);
        createPlan = findViewById(R.id.MainCreatePlanButton);
        noWorkout = findViewById(R.id.MainNoWorkoutText);
        // ViewFlipper flipper = findViewById(R.id.MainFilterView);
        ImageView info = findViewById(R.id.MainInfoButton);
        ImageView planImport = findViewById(R.id.ImportMainIcon);


        // ---- Show Workouts in Recycle View or -----
        // ---- Display Create From Scratch button --\
        CheckUser();
        CheckPlan();
        CheckTodaysWorkout();


        // ----- Set UI Data-----
        mainText.setText("Bem vindo! " + username);
        String dayOfWeek2 = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());



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


        RegisterResult();


        // NaviBar Values
        View headerView = navigationView.getHeaderView(0);
        TextView userNameText = headerView.findViewById(R.id.UsernameNaviBar);
        TextView userEmailText = headerView.findViewById(R.id.UserEmailNaviBar);
        naviBarImage = headerView.findViewById(R.id.UserPhotoNaviBar);
        userNameText.setText(username);
        userEmailText.setText(email);
        // Set Image
        // Bitmap bitmap2 = imageConverter.ConvertToBitmap(userImageString);
        // naviBarImage.setImageBitmap(bitmap2);


        naviBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        createPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teste();
            }
        });

    }

    //--------------------------------------------------------------------------------------


    public void teste() {
        popupService.NewPlanMainPopup(this, MainActivity.this, username);

    }
    private void CheckTodaysWorkout() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        WorkoutDao daoW = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // List all Workouts from Active Plan
                List<Workout> allWorkouts = daoW.listWorkouts();

                // --------------------------------------------------------------
                if (thisPlan != null) {

                    // If Plan workouts is set to fixed Days
                    if (thisPlan.fixedDays) {
                        if (!allWorkouts.isEmpty()) {
                            for (Workout item : allWorkouts) {
                                if (item.plan_Id == thisPlan.id) {
                                    // Check Days of Week
                                    if (item.dayOfWeek == LocalDate.now().getDayOfWeek()) {
                                        todaysWorkout = item;
                                    } else {
                                        Log.d("Todays Workout", "Active Plan doesnt have workout for this day");
                                    }
                                }
                            }
                        }
                    } else {
                        // Plans set NOT to Fixed Days
                        if (!allWorkouts.isEmpty()) {
                            for (Workout item : allWorkouts) {
                                todaysWorkout = item;
                            }
                        }
                    }
                }
                else {
                    //Toast.makeText(getApplicationContext(), "Active Plan is Null", Toast.LENGTH_SHORT).show();
                }

                // -------------------------------------------------------------------------------
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //----------------------------------------------------------------
                        // Set Todays Workout in List to use in Recycler View
                        if (todaysWorkout != null) {
                            workoutsList.add(todaysWorkout);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Todays Workout is Null", Toast.LENGTH_SHORT).show();
                        }
                        // ------------------
                        ApplyWorkoutRV();
                    }
                });
            }
        }).start();

        db.close();
    }



    // --------------------------------------------------------------------------------------------

    public void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    public void RegisterResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //if (false) { //&& data != null && data.getData() != null) {
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
           // }
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

        // If has no User, go to Welcome Page
        if (Objects.equals(username, "")) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
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
                List<Plan> planList = new ArrayList<>();

                PlanDao daoPlan = db.planDao();
                List<Plan> allPlans = daoPlan.listPlans();

                if (!allPlans.isEmpty()) {
                    for (Plan item : allPlans) {
                        if (item.active == true) {
                            thisPlan = item;
                            planList.add(item);
                        }
                    }
                }

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (thisPlan != null) {
                            Log.d("teste", "Opa, chegamos aqui - " + thisPlan.planName);
                            // -------- Set Recycler View Horizontal --------
                            PlanRVAdapter planAdapter = new PlanRVAdapter(new MainActivity(), planList, new PlanRVAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Plan item) {

                                    Intent intent = new Intent(MainActivity.this, PlanActivity.class);
                                    intent.putExtra("SelectedPlan", thisPlan);
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
                            // Display Workouts inside the Recycler View
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    plansRV.setAdapter(planAdapter);
                                    //plansRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                }
                            }, 1000); // 3000 milliseconds = 3 seconds
                        } else {
                            // Show Create New Button
                            createPlan.setVisibility(View.VISIBLE);
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
    private void ApplyWorkoutRV() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        WorkoutDao daoW = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Workout> allWorkouts = daoW.listWorkouts();

                if (!allWorkouts.isEmpty()) {
                    for (Workout item : allWorkouts) {
                        if (item.id == todaysWorkout.id) {
                            workoutsList.add(item);
                        }
                    }
                }
                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!workoutsList.isEmpty()) {
                            Log.d("Teste RV", "Workout encontrado");
                            workoutAdapter = new WorkoutRVAdapter(MainActivity.this, workoutsList, new WorkoutRVAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Workout item) {
                                    Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
                                    //intent.putExtra("SelectedPlan", thisPlan);
                                    intent.putExtra("SelectedWorkout", item);
                                    startActivity(intent);
                                }
                                // --------------------------------------
                                @Override
                                public void onItemLongClick(Workout item) {
                                    // Do nothing
                                }
                            }, new WorkoutRVAdapter.OnClickEditListener() {
                                @Override
                                public void editButtonClick(Workout item) {
                                    // EditWorkout(item);
                                }
                            });
                            // Display Workouts in Recycler View
                            workoutRV.setAdapter(workoutAdapter);
                            workoutRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            // Attach Item Helper
                            mIth.attachToRecyclerView(workoutRV);
                        } else {
                            Toast.makeText(getApplicationContext(), "Todays Workout NOT found",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }).start();

        db.close();
    }

    public void changeToPlan(Plan plan) {
        Intent intent = new Intent(this, PlanActivity.class);
        intent.putExtra("SelectedPlan", plan);
        startActivity(intent);
    }


    //////////////////////// END ////////////////////////
}