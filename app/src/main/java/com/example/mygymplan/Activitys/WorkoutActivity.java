package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.mygymplan.Adapters.ExerciseRVAdapter;
import com.example.mygymplan.Adapters.WorkoutRVAdapterHorizontal;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Services.ExerciseService;
import com.example.mygymplan.Services.ImageConverter;
import com.example.mygymplan.Services.PopupService;
import com.example.mygymplan.Services.WorkoutService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Entity's
    public Plan thisPlan;
    public Workout thisWorkout;

    // Shared Preferences
    String username;
    String email;

    // -------------------------------------------------------------

    // UI Elements
    TextView showName;
    List<Exercise> displayedExercises;
    List<Workout> displayedWorkouts;
    Button newButton;
    Button addButton;
    ItemTouchHelper mIth;

    // -------------------------------------------------------------

    // Recycler Views
    RecyclerView recyclerView;                  // Exercise Recycler View (Vertical)
    RecyclerView horizontalRecyclerView;        // Workout Recycler View (Horizontal)
    ExerciseRVAdapter exerciseAdapter;          // Needed for Delete Exercise
    Button emptyButton1;                         // Show Button when Recycler View is Empty
    Button emptyButton2;                         // Show Button when Recycler View is Empty

    // -------------------------------------------------------------

    // Drawer NaviBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView naviBarImage;
    ImageConverter imageConverter = new ImageConverter();
    String userImageString;
    ActivityResultLauncher<Intent> resultLauncher;

    // -------------------------------------------------------------

    // Others
    PopupService popupService = new PopupService();
    Exercise newExercise;  // -->  Add Exercise Teste
    Button testButton;  // --> For Tests Only


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_workout);
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
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");

        Log.d("Selected Plan", "Selected Plan ID is: " + thisPlan.id);
        Log.d("Selected Workout", "Selected Workout ID is: " + thisWorkout.id);

        // --- UI Elements ---
        showName = findViewById(R.id.WorkoutName);
        newButton = findViewById(R.id.CreateNewExercise);
        addButton = findViewById(R.id.AddExercise);
        Button backButton = findViewById(R.id.BackButtonWorkout);
        recyclerView = findViewById(R.id.RV_WorkoutsMain);
        horizontalRecyclerView = findViewById(R.id.RV_WorkoutsHorizontal);
        emptyButton1 = findViewById(R.id.CreateExerciseButton2);
        emptyButton2 = findViewById(R.id.AddExerciseButton2);


        // -------------------------------------------------------------------
        // ---  Load Data Recycler View on Create the Activity  ---
        LoadData(thisWorkout);
        LoadPrefs();


        // --- Drawer Layout ---
        Toolbar toolbar = findViewById(R.id.toolbar2);                                        // Find Toolbar
        setSupportActionBar(toolbar);                                                         // Set Toolbar as ActionBar
        drawerLayout = findViewById(R.id.DrawerLayout);                                       // Find DrawerLayout
        navigationView = findViewById(R.id.NavView);                                          // Find Navigation View
        navigationView.setNavigationItemSelectedListener(this);                               // Only Works if class: implements NavigationView.OnNavigationItemSelectedListener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);            // Set ActionBar (Hamburger Menu)
        drawerLayout.addDrawerListener(toggle);                                               // Set Click on ActionBar
        toggle.syncState();                                                                           // Sync with drawer state (Open/Close)

        // Get URI from Pick Image
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
        //Set Image Click
        naviBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });


        // -------------------------------------------------------------------
        // ---  Set Values based on Received Data  ---
        showName.setText(thisPlan.name);


        // ---------------------------------
        // ------------ BUTTONS ------------
        // ---------------------------------

        // --- Create New Exercise ---
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateNewExercise();
            }
        });

        // --- Add Already Created Exercises ---
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { AddExercise(); }
        });

        emptyButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { CreateNewExercise(); }
        });

        emptyButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { AddExercise(); }
        });


        // --- Back Button ---
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Change back to Main Activity
                Intent intent = new Intent(WorkoutActivity.this, PlanActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);
                startActivity(intent);
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        0) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {

                        final int fromPos = viewHolder.getBindingAdapterPosition();
                        final int toPos = target.getBindingAdapterPosition();

                        Exercise exercise1 = displayedExercises.get(fromPos);
                        Exercise exercise2 = displayedExercises.get(toPos);

                        Collections.swap(displayedExercises, fromPos, toPos);
                        exerciseAdapter.notifyItemMoved(fromPos, toPos);

                        ExerciseService exerciseService = new ExerciseService();
                        exerciseService.changeExerciseOrder(getApplicationContext(), exercise1, exercise2);

                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        // Has no Swipe action

                    }
                });

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

    // ----------------------------------------------
    // --------- Add Exercise From Database ---------
    // ----------------------------------------------
    public void AddExercise() {
        popupService.AddExercisePopup(this, WorkoutActivity.this);
    }


    // --------------------------------------------
    // ----------- Load Data and Display ----------
    // ------------- on Recycler Views -------------
    // --------------------------------------------
    public void LoadData(Workout workout) {
        WorkoutsHorizontalRecyclerView(workout.sequence);
        UpdateRecyclerView(workout);
    }


    // --------------------------------------------
    // ---------- Load Shared Preferences ---------
    // --------------------------------------------
    private void LoadPrefs() {
        // Check if its user First time opening App
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        email = sharedPreferences.getString("email", "");
        userImageString = sharedPreferences.getString("userImageString", "");
    }


    // ----------------------------------------------
    // -------- Reload Workouts Recycler View -------
    // ----------------------------------------------
    public void WorkoutsHorizontalRecyclerView(int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Access Database
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                WorkoutDao dao = db.workoutDao();

                // Initialize List
                displayedWorkouts = new ArrayList<>();

                // List All Workouts
                List<Workout>  allWorkouts = dao.listWorkouts();

                db.close();

                // Remove Workouts from other Plans
                if (!allWorkouts.isEmpty()) {
                    for (Workout item : allWorkouts) {
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
                    displayedWorkouts.sort((w1, w2) -> Integer.compare(w1.sequence, w2.sequence));
                }

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // -------- Set Recycler View Horizontal --------
                        WorkoutRVAdapterHorizontal workoutAdapter = new WorkoutRVAdapterHorizontal(WorkoutActivity.this, displayedWorkouts, new WorkoutRVAdapterHorizontal.OnItemClickListener() {
                            @Override
                            public void onItemClick(Workout item) {
                                thisWorkout = item;
                                UpdateRecyclerView(thisWorkout);
                                WorkoutsHorizontalRecyclerView(item.sequence);  // Change Color
                                displayedExercises.clear();
                            }
                        }, position - 1);
                        // Display Workouts inside the Recycler View
                        horizontalRecyclerView.setAdapter(workoutAdapter);
                        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(WorkoutActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    }
                });
            }
        }).start();
    }


    // ----------------------------------------------
    // -------- Reload Exercise Recycler View -------
    // ----------------------------------------------
    public void UpdateRecyclerView(Workout workout) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Access Database
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();

                // Initialize List
                displayedExercises = new ArrayList<>();
                displayedExercises.clear();
                List<Exercise> newList = dao.listExercises();

                db.close();

                // Add to List only the Exercises from this Workout
                for (Exercise e : newList) {
                    if (e.workout_Id == workout.id && e.plan_Id == thisPlan.id) {
                        displayedExercises.add(e);
                    }
                }

                // Sort by Order
                displayedExercises.sort((e1, e2) -> Integer.compare(e1.sequence, e2.sequence));

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (displayedExercises != null) {
                            // ----- Set Exercise Adapter -----
                            exerciseAdapter = new ExerciseRVAdapter(WorkoutActivity.this, displayedExercises, new ExerciseRVAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Exercise item) {
                                    ChangeToExercise(item);
                                }
                            }, new ExerciseRVAdapter.OnItemClickDelete() {
                                @Override
                                public void deleteButtonClick(int position) {
                                    DeleteFromRecyclerView(position);
                                }
                            });
                            // Display Exercises inside the Recycler View
                            recyclerView.setAdapter(exerciseAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(WorkoutActivity.this));
                            // Attach item Helper
                            mIth.attachToRecyclerView(recyclerView);
                            // Then Check if Need to Change the UI...
                            ChangeUIVisibility();
                        }
                    }
                });
            }
        }).start();
    }


    // ----------------------------------------------
    // ------- Change UI Elements Visibility --------
    // ------- Based On Recycler View State ---------
    // ----------------------------------------------
    private void ChangeUIVisibility() {
        Log.d("Change UI Visibility Teste", "Number os Displayed Exercises: " + displayedExercises.size());
        if (displayedExercises.isEmpty()) {
            Log.d("Change UI Visibility Teste", "Displayed Exercises is Empty");
            recyclerView.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
            newButton.setVisibility(View.GONE);
            emptyButton1.setVisibility(View.VISIBLE);
            emptyButton2.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyButton1.setVisibility(View.GONE);
            emptyButton2.setVisibility(View.GONE);
        }
    }


    // ----------------------------------------------
    // ------ Create New Exercise From Scratch ------
    // ----------------------------------------------
    public void CreateNewExercise() {
        Exercise newExercise = new Exercise();

        // New Exercise default values
        newExercise.plan_Id = thisPlan.id;
        newExercise.workout_Id = thisWorkout.id;
        newExercise.name = "1";
        newExercise.description = "Description Here...";
        newExercise.sets = 0;
        newExercise.reps = 0;
        newExercise.rest = 0;
        newExercise.load = 0;
        newExercise.sequence = displayedExercises.size();
        // Set Default Image
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.logo_fundoazul);
        ImageConverter imageConverter = new ImageConverter();
        newExercise.image = imageConverter.ConvertToString(bitmap1);

        // Change Activity
        ChangeToExercise(newExercise);
    }


    // ----------------------------------------------
    // -------- Delete a Specific Row From ----------
    // ---------- Exercises Recycler View -----------
    // ----------------------------------------------
    public void AddExerciseToWorkout(SavedExercise savedExercise) {
        ExerciseService exerciseService = new ExerciseService();
        // --------------------------------------
        // Convert Saved Exercise to Exercise
        newExercise = exerciseService.convertExercise(savedExercise,
                displayedExercises.size() + 1, thisPlan.id, thisWorkout.id);
        // --------------------------------------
        // Insert new Exercise in Database
        exerciseService.insertExercise(getApplicationContext(), newExercise);
        // Update Recycler View and List
        displayedExercises.add(newExercise);
        exerciseAdapter.notifyItemInserted(displayedExercises.size());
        ChangeUIVisibility();
        // Action Feedback
        Log.d( "Workout Activity", "Exercise " + newExercise.name + " ID nº: " + newExercise.id +" - Added");
        Toast.makeText(getApplicationContext(), "Exercise Added",Toast.LENGTH_SHORT).show();

        // CheckAuthor();
    }


    // ----------------------------------------------
    // -------- Delete a Specific Row From ----------
    // ---------- Exercises Recycler View -----------
    // ----------------------------------------------
    public void DeleteFromRecyclerView(int position) {
        // Delete Exercise in Database
        ExerciseService exerciseService = new ExerciseService();
        exerciseService.deleteExercise(getApplicationContext(), displayedExercises.get(position));
        // --------------------------------------
        Log.d( "Workout Activity", "Exercise " + displayedExercises.get(position).name + " ID nº: " + displayedExercises.get(position).id + " - Deleted");

        Log.d("Teste", "deletado Exercisio daLista - " + displayedExercises.get(position).name + " na posição " + position);
        // Update Recycler View and List
        displayedExercises.remove(position);
        exerciseAdapter.notifyItemRemoved(position);
        exerciseAdapter.notifyItemRangeChanged(position, displayedExercises.size());
        ChangeUIVisibility();
        // Action Feedback
        Toast.makeText(getApplicationContext(), "Exercise Deleted",Toast.LENGTH_SHORT).show();

        // CheckAuthor();
    }


    private void CheckAuthor() {
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                WorkoutService workoutService = new WorkoutService();

                // Check if Plan is from Database...
                if (thisPlan.userCreated == false) {
                    // If is from Database... Create Variant
                    workoutService.createVarient(getApplicationContext(), thisWorkout);
                } else {
                    // If NOT just Update Duration Time
                    workoutService.updateWorkout(getApplicationContext(), thisWorkout);
                }
            }
        }, 500); // 3000 milliseconds = 3 seconds
    }

    // ----------------------------------------------
    // ------ Change Activity to Show Exercise ------
    // ----------------------------------------------
    public void ChangeToExercise(Exercise item) {
        Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class);
        intent.putExtra("SelectedPlan", thisPlan);
        intent.putExtra("SelectedWorkout", thisWorkout);
        intent.putExtra("SelectedExercise", item);
        startActivity(intent);
    }

    //////////////////////// END ////////////////////////
}