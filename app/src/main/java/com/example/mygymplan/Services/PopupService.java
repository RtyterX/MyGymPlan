package com.example.mygymplan.Services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mygymplan.Activitys.MainActivity;
import com.example.mygymplan.Activitys.WorkoutActivity;
import com.example.mygymplan.Adapters.PlanRVAdapter;
import com.example.mygymplan.Adapters.SavedExerciseRVAdapter;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Database.SavedExerciseDao;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.R;

import java.util.ArrayList;
import java.util.List;

public class PopupService extends AppCompatActivity {
    Workout newWorkout;
    private List<SavedExercise> databaseExercises;
    private List<SavedExercise> myExercises;

    public void NewPlanPopup(Context context, MainActivity mainActivity, String username) {
        // Inflate Activity with a new View
        View popupView = View.inflate(context, R.layout.popup_new_plan, null);

        // Popup View UI Content
        EditText newPlanName = popupView.findViewById(R.id.NewPlanName);
        EditText newPlanDescription = popupView.findViewById(R.id.NewPlanDescription);
        TextView newPlanNameWarning = popupView.findViewById(R.id.NewPlanDescription);
        TextView newPlanDescripWarning = popupView.findViewById(R.id.NewPlanDescription);
        Button confirmButton = popupView.findViewById(R.id.ConfirmWarningButton);
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);

        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // ------ Buttons ------
        confirmButton.setOnClickListener(v -> {
            // --------------------------------------------------------------------
            if (newPlanName.getText().toString().isEmpty()) {
                newPlanNameWarning.setVisibility(View.VISIBLE);
            } else if (newPlanDescription.getText().toString().isEmpty()) {
                newPlanDescripWarning.setVisibility(View.VISIBLE);
            } else {

                PlanService planService = new PlanService();

                // Inflate Activity with a new View
                View subView = View.inflate(context, R.layout.popup_warning, null);

                // Popup View UI Content
                TextView popupText = subView.findViewById(R.id.WarningMessage);
                Button yesButton = subView.findViewById(R.id.ConfirmWarningButton);
                Button noButton = subView.findViewById(R.id.CloseWarningButton);

                // Initialize new Popup View
                PopupWindow subPopupWindow = new PopupWindow(subView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                // Set Shadow
                subPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                subPopupWindow.setElevation(10.0f);
                // Set Popup Location on Screen
                subPopupWindow.showAtLocation(subView, Gravity.CENTER, 0, 0);

                // Set Text Warning
                popupText.setText("Gostaria de deixar o Plano como Ativo?");
                yesButton.setText("Yes");
                noButton.setText("No");

                // ------ Buttons ------
                yesButton.setOnClickListener(subV -> {
                    // Create Plan as Active
                    Plan newPlan = planService.convertPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString(), username, true);
                    planService.insertPlan(context, newPlan);
                    planService.activePlan(context, newPlan);
                    // Update Activity
                    mainActivity.recreate();
                    //mainActivity.CheckPlan();
                    // Show Text on Screen
                    Toast.makeText(context, "New Plan Created",Toast.LENGTH_SHORT).show();
                    // Close Popup
                    popupWindow.dismiss();
                    subPopupWindow.dismiss();
                });

                noButton.setOnClickListener(subV -> {
                    // Create Plan but Not Active
                    Plan newPlan = planService.convertPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString(), username, false);
                    planService.insertPlan(context, newPlan);
                    // Update Activity
                    mainActivity.CheckPlan();
                    // Show Text on Screen
                    Toast.makeText(context, "New Plan Created",Toast.LENGTH_SHORT).show();
                    // Close Popup
                    popupWindow.dismiss();
                    subPopupWindow.dismiss();
                });
            }
            // --------------------------------------------------------------------
        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }


    public void NewWorkoutPopup(Context context, MainActivity mainActivity, int planId) {
        // Inflate Activity with a new View
        View popupView = View.inflate(context, R.layout.popup_new_workout, null);

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
        Button deleteButton = popupView.findViewById(R.id.DeleteWButton);
        deleteButton.setVisibility(View.INVISIBLE);   // Don't Show when creating a New Workout
        // Type Selection
        AutoCompleteTextView autoComplete = popupView.findViewById(R.id.AutoCompleteNewWorkouts);
        ArrayAdapter<String> adapterItem;
        // ------------ Enum ------------------
        String[] typeInView = {
                "Chest",
                "Back",
                "Shoulder",
                "Arms",
                "Legs",
                "Biceps",
                "Triceps"};


        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        newWorkout = new Workout();

        // ------------------------------------------------------
        // ------------------ Dropdown Menu ---------------------
        // ------------------------------------------------------
        adapterItem = new ArrayAdapter<String>(context, R.layout.enum_list, typeInView);
        autoComplete.setAdapter(adapterItem);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                WorkoutService workoutService = new WorkoutService();
                workoutService.applyWorkoutType(newWorkout, item);
            }
        });

        // ------------- Buttons -------------
        confirmButton.setOnClickListener(v -> {
            ///////////////////// NEED REWORK /////////////////////
            /////////////// Check Variables Inputs ////////////////
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
            } else {
                nameWarning.setVisibility(View.GONE);
                descriptionWarning.setVisibility(View.GONE);
                typeWarning.setVisibility(View.GONE);
            }
            ///////////////////////////////////////////////////

            // Inset New Workout in Database
            WorkoutService workoutService = new WorkoutService();
            newWorkout = workoutService.converterWorkout(newWorkoutName.getText().toString(), newWorkoutDescription.getText().toString(), newWorkout.wType, planId);
            workoutService.insertWorkout(context, newWorkout);
            // Show Text on Screen
            Toast.makeText(context, "Workout Created",Toast.LENGTH_SHORT).show();
            // Need to wait for animation when is the last Exercise in List
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainActivity.GetWorkoutList();
                    popupWindow.dismiss();     // Close Popup
                }
            }, 300); // 3000 milliseconds = 3 seconds

        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }

    public void EditWorkoutPopup(Context context, MainActivity mainActivity, Workout workout) {
        // Inflate Activity with a new View
        View popupView = View.inflate(context, R.layout.popup_new_workout, null);

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
        Button deleteButton = popupView.findViewById(R.id.DeleteWButton);
        // Type Selection
        AutoCompleteTextView autoComplete = popupView.findViewById(R.id.AutoCompleteNewWorkouts);
        ArrayAdapter<String> adapterItem;
        // ------------ Enum ------------------
        String[] typeInView = {
                "Chest",
                "Back",
                "Shoulder",
                "Arms",
                "Legs",
                "Biceps",
                "Triceps"};

        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


        // --- Set Values ---
        newWorkoutName.setText(workout.wName);
        newWorkoutDescription.setText(workout.wDescription);

        // ----------------------------------


        // ------------------------------------------------------
        // ------------------ Dropdown Menu ---------------------
        // ------------------------------------------------------
        autoComplete.setText(workout.wType.toString());
        adapterItem = new ArrayAdapter<String>(context, R.layout.enum_list, typeInView);
        autoComplete.setAdapter(adapterItem);

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                WorkoutService workoutService = new WorkoutService();
                workoutService.applyWorkoutType(workout, item);
            }
        });

        // ------------- Buttons -------------
        confirmButton.setOnClickListener(v -> {
            // Set Variables
            workout.wName = newWorkoutName.getText().toString();
            workout.wDescription = newWorkoutDescription.getText().toString();
            // Update Workout
            WorkoutService workoutService = new WorkoutService();
            workoutService.updateWorkout(context, workout);
            // Change Activity
            mainActivity.GetWorkoutList();
            Toast.makeText(context, "Workout Modified",Toast.LENGTH_SHORT).show();
            // Close Popup
            popupWindow.dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            // Delete Workout
            WorkoutService workoutService = new WorkoutService();
            workoutService.deleteWorkout(context, workout);
            // Change Activity
            mainActivity.GetWorkoutList();
            // Show Confirmation Text
            Toast.makeText(context, "Workout Deleted",Toast.LENGTH_SHORT).show();
            // Close Popup
            popupWindow.dismiss();
        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }


    ////////////////////////////////////////////////////////////////////
    ///////////////////////// NOT IN USE //////////////////////////////
    ///////////////////////////////////////////////////////////////////

    //----------------------------------------------------
    //------------------- Change Plan --------------------
    //----------------------------------------------------
    public void ChangePlan(Context context, MainActivity mainActivity) {
        // --- Inflate Activity with a new View ---
        View popupView = View.inflate(context, R.layout.popup_change_plan, null);

        // --- Popup View UI Content ---
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);
        // RecyclerView
        RecyclerView recyclerView1 = popupView.findViewById(R.id.RecyclerViewChangePlans);

        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Database
                List<Plan> planList = new ArrayList<>();
                planList = dao.listPlans();

                // Set Recycler View Adapter
                PlanRVAdapter adapterPlan = new PlanRVAdapter(context, planList, new PlanRVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Plan item) {
                        // ShowAnotherPlan(item);
                        popupWindow.dismiss();
                    }
                }, new PlanRVAdapter.OnItemClickDelete() {
                    @Override
                    public void deleteButtonClick(int position) {
                        PlanService planService = new PlanService();
                        // planService.deletePlan(getApplicationContext(), planList.get(postion));
                    }
                }, new PlanRVAdapter.OnItemClickSetActive() {
                    @Override
                    public void setActiveButtonClick(Plan plan) {

                    }
                });

                // Display Recycler View
                recyclerView1.setAdapter(adapterPlan);
                recyclerView1.setLayoutManager(new LinearLayoutManager(context));

            }
        }).start();

        db.close();
    }


    //----------------------------------------------------
    //-------------------- About Us ----------------------
    //----------------------------------------------------
    public void AboutUsPopup(Context context) {
        // Inflate Activity with a new View
        View popupView = View.inflate(context, R.layout.popup_warning, null);

        // Popup View UI Content
        TextView popupWarning = popupView.findViewById(R.id.WarningMessage);
        Button confirmButton = popupView.findViewById(R.id.ConfirmWarningButton);
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);

        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // Set Text Warning
        popupWarning.setText("About Us. App desenvolvido por Ricardo Thiago Firmino :)");

        // Buttons
        confirmButton.setVisibility(View.GONE);
        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }


    public void addExercisePopup(Context context, Activity activity) {
        // Open new Popup where user create a new Plan
        // -------------------------------------------------------
        // Inflate Activity with a new View
        View popupView = View.inflate(context, R.layout.popup_add_exercise, null);

        // Popup View UI Content
        Button MyExercisesButton = popupView.findViewById(R.id.MyExercisesButton);
        Button DatabaseButton = popupView.findViewById(R.id.DatabaseExercisesButton);
        Button closeButton = popupView.findViewById(R.id.CloseAddExercise);
        RecyclerView addExerciseRV = popupView.findViewById(R.id.AddExerciseRV);

        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        SavedExerciseDao dao = db.savedExerciseDao();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Initialize ListS
                List<SavedExercise> myExercises = new ArrayList<>();
                List<SavedExercise> databaseExercises = new ArrayList<>();
                List<SavedExercise> allExercises;

                // List All Exercises
                allExercises = dao.listSavedExercise();

                // ---------- MY EXERCISES ----------
                if (!allExercises.isEmpty()) {
                    for (SavedExercise item : allExercises) {
                        if (item.userCreated) {
                            myExercises.add(item);
                        }
                    }
                }

                // ---------- DATABASE ----------
                if (!allExercises.isEmpty()) {
                    for (SavedExercise item : allExercises) {
                        if (!item.userCreated) {
                            databaseExercises.add(item);
                        }
                    }
                }

                // Run On UI When the above injection is applied
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // ------ Show Recycler View (My Exercises when Open) ------
                        SavedExerciseRVAdapter savedExerciseAdapter = new SavedExerciseRVAdapter(activity, myExercises, new SavedExerciseRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(SavedExercise item) {
                                WorkoutActivity workoutActivity = new WorkoutActivity();
                                workoutActivity.AddExerciseToWorkout(item);
                                // Show Text on Screen
                                Toast.makeText(getApplicationContext(), "Exercise Add", Toast.LENGTH_SHORT).show();
                                ///////////// If Changed to Popup Service, Alter context //////////////
                                popupWindow.dismiss();
                            }
                        });
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Display Exercises inside the Recycler View
                                addExerciseRV.setAdapter(savedExerciseAdapter);
                                addExerciseRV.setLayoutManager(new LinearLayoutManager(activity));
                            }
                        }, 500); // 3000 milliseconds = 3 seconds

                    }
                });
            }
        }).start();

        db.close();


        // ------ Buttons ------
        MyExercisesButton.setOnClickListener(v -> {
            // ------ Show Recycler View (My Exercises when Open) ------
            SavedExerciseRVAdapter myExerciseAdapter = new SavedExerciseRVAdapter(activity, myExercises, new SavedExerciseRVAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SavedExercise item) {
                    WorkoutActivity workoutActivity = new WorkoutActivity();
                    workoutActivity.AddExerciseToWorkout(item);
                    // Show Text on Screen
                    Toast.makeText(getApplicationContext(), "Exercise Add", Toast.LENGTH_SHORT).show();
                    ///////////// If Changed to Popup Service, Alter context //////////////
                    popupWindow.dismiss();
                }
            });
            // Display Exercises inside the Recycler View
            addExerciseRV.setAdapter(myExerciseAdapter);
            addExerciseRV.setLayoutManager(new LinearLayoutManager(activity));
        });

        DatabaseButton.setOnClickListener(v -> {
            // ------ Show Recycler View (My Exercises when Open) ------
            SavedExerciseRVAdapter databaseAdapter = new SavedExerciseRVAdapter(activity, databaseExercises, new SavedExerciseRVAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SavedExercise item) {
                    WorkoutActivity workoutActivity = new WorkoutActivity();
                    workoutActivity.AddExerciseToWorkout(item);
                    // Show Text on Screen
                    Toast.makeText(getApplicationContext(), "Exercise Add", Toast.LENGTH_SHORT).show();
                    ///////////// If Changed to Popup Service, Alter context //////////////
                    popupWindow.dismiss();
                }
            });
            // Display Exercises inside the Recycler View
            addExerciseRV.setAdapter(databaseAdapter);
            addExerciseRV.setLayoutManager(new LinearLayoutManager(activity));
        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }

}
