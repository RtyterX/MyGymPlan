package com.example.mygymplan.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mygymplan.Activitys.MainActivity;
import com.example.mygymplan.Activitys.PlanActivity;
import com.example.mygymplan.Activitys.SelectPlanActivity;
import com.example.mygymplan.Activitys.WorkoutActivity;
import com.example.mygymplan.Adapters.PlanRVAdapter;
import com.example.mygymplan.Adapters.SavedExerciseRVAdapter;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Database.SavedExerciseDao;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.R;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class PopupService extends AppCompatActivity {
    Plan newPlan;
    Workout newWorkout;
    private List<SavedExercise> databaseExercises = new ArrayList<>();
    private List<SavedExercise> myExercises = new ArrayList<>();


    // --------------------------------------------------------------------------------------------
    public void NewPlanMainPopup(Context context, MainActivity activity, String username) {
        // Inflate Activity with a new View
        View popupView = View.inflate(context, R.layout.popup_new_plan, null);

        // Popup View UI Content
        EditText newPlanName = popupView.findViewById(R.id.NewPlanName);
        EditText newPlanDescription = popupView.findViewById(R.id.NewPlanDescription);
        TextView newPlanNameWarning = popupView.findViewById(R.id.NewPlanDescription);
        TextView newPlanDescripWarning = popupView.findViewById(R.id.NewPlanDescription);
        CheckBox fixedDays = popupView.findViewById(R.id.FixedDaysCheckBox);
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

                //--------------------------------------------------------------
                PlanService planService = new PlanService();
                // Plan newPlan = new Plan();
                // Check if plan has Fixed Days
                if (fixedDays.isChecked()) {
                    newPlan = planService.convertPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString(), username, true,true);
                } else {
                    newPlan = planService.convertPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString(), username, false,true);
                }

                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                int activePlanId = sharedPreferences.getInt("activePlanId", 0);
                if (activePlanId != 0) {

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
                        newPlan.active = true;
                        planService.insertPlan(context, newPlan);
                        // Show Text on Screen
                        Toast.makeText(context, "New Plan Created", Toast.LENGTH_SHORT).show();
                        // Close Popup
                        subPopupWindow.dismiss();
                        popupWindow.dismiss();
                        // Change to Plan Activity
                        activity.changeToPlan(newPlan);
                    });

                    noButton.setOnClickListener(subV -> {
                        // Create Plan but Not Active
                        newPlan.active = false;
                        planService.insertPlan(context, newPlan);
                        // Show Text on Screen
                        Toast.makeText(context, "New Plan Created", Toast.LENGTH_SHORT).show();
                        // Close Popup
                        popupWindow.dismiss();
                        subPopupWindow.dismiss();
                        // Change Activity
                        activity.CheckPlan();
                    });
                }
                else {
                    newPlan.active = true;
                    planService.insertPlan(context, newPlan);
                    // Show Text on Screen
                    Toast.makeText(context, "First Plan Created", Toast.LENGTH_SHORT).show();
                    // Change to Plan Activity
                    activity.changeToPlan(newPlan);
                    // Close Popup
                    popupWindow.dismiss();

                }
            }
            // --------------------------------------------------------------------
        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }


    // --------------------------------------------------------------------------------------------
    public void NewPlanActivityPopup(Context context, SelectPlanActivity activity, String username) {
        // Inflate Activity with a new View
        View popupView = View.inflate(context, R.layout.popup_new_plan, null);

        // Popup View UI Content
        EditText newPlanName = popupView.findViewById(R.id.NewPlanName);
        EditText newPlanDescription = popupView.findViewById(R.id.NewPlanDescription);
        TextView newPlanNameWarning = popupView.findViewById(R.id.NewPlanDescription);
        TextView newPlanDescripWarning = popupView.findViewById(R.id.NewPlanDescription);
        CheckBox fixedDays = popupView.findViewById(R.id.FixedDaysCheckBox);
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

                //--------------------------------------------------------------
                PlanService planService = new PlanService();
                // Plan newPlan = new Plan();
                // Check if plan has Fixed Days
                if (fixedDays.isChecked()) {
                    newPlan = planService.convertPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString(), username, true,true);
                } else {
                    newPlan = planService.convertPlan(newPlanName.getText().toString(), newPlanDescription.getText().toString(), username, false,true);
                }

                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                int activePlanId = sharedPreferences.getInt("activePlanId", 0);
                if (activePlanId != 0) {

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
                        newPlan.active = true;
                        planService.insertPlan(context, newPlan);
                        // Update Activity
                        activity.LoadMyPlans();
                        // Show Text on Screen
                        Toast.makeText(context, "New Plan Created", Toast.LENGTH_SHORT).show();
                        // Close Popup
                        subPopupWindow.dismiss();
                        popupWindow.dismiss();
                    });

                    noButton.setOnClickListener(subV -> {
                        // Create Plan but Not Active
                        newPlan.active = false;
                        planService.insertPlan(context, newPlan);
                        // Update Activity
                        activity.LoadMyPlans();
                        // Show Text on Screen
                        Toast.makeText(context, "New Plan Created", Toast.LENGTH_SHORT).show();
                        // Close Popup
                        subPopupWindow.dismiss();
                        popupWindow.dismiss();
                    });
                }
                else {
                    // Create Plan as Active
                    newPlan.active = true;
                    planService.insertPlan(context, newPlan);
                    // Update Activity
                    activity.LoadMyPlans();
                    // Show Text on Screen
                    Toast.makeText(context, "First Plan Created", Toast.LENGTH_SHORT).show();
                    // Close Popup
                    popupWindow.dismiss();
                }
            }
            // --------------------------------------------------------------------
        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }


    // --------------------------------------------------------------------------------------------
    public void NewWorkoutPopup(Context context, PlanActivity planActivity, Plan plan) {
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
        // Check Box
        CheckBox monday = popupView.findViewById(R.id.MondayCheckBox);
        CheckBox tuesday = popupView.findViewById(R.id.TuesdayCheckBox);
        CheckBox wednesday = popupView.findViewById(R.id.WednesdayCheckBox);
        CheckBox thursday = popupView.findViewById(R.id.ThurdayCheckBox);
        CheckBox friday = popupView.findViewById(R.id.FridayCheckBox);
        CheckBox saturday = popupView.findViewById(R.id.SaturdayCheckBox);
        CheckBox sunday = popupView.findViewById(R.id.SundayCheckBox);
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


        // ------------------- DAY OF WEEK ---------------------------
        //region Days of Week Check Listener
        monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                newWorkout.dayOfWeek = DayOfWeek.MONDAY;
                monday.setChecked(true);
                tuesday.setChecked(false);
                wednesday.setChecked(false);
                thursday.setChecked(false);
                friday.setChecked(false);
                saturday.setChecked(false);
                sunday.setChecked(false);
            }
        });

        tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                newWorkout.dayOfWeek = DayOfWeek.TUESDAY;
                monday.setChecked(false);
                tuesday.setChecked(true);
                wednesday.setChecked(false);
                thursday.setChecked(false);
                friday.setChecked(false);
                saturday.setChecked(false);
                sunday.setChecked(false);
            }
        });

        wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                newWorkout.dayOfWeek = DayOfWeek.WEDNESDAY;
                monday.setChecked(false);
                tuesday.setChecked(false);
                wednesday.setChecked(true);
                thursday.setChecked(false);
                friday.setChecked(false);
                saturday.setChecked(false);
                sunday.setChecked(false);
            }
        });

        thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                newWorkout.dayOfWeek = DayOfWeek.THURSDAY;
                monday.setChecked(false);
                tuesday.setChecked(false);
                wednesday.setChecked(false);
                thursday.setChecked(true);
                friday.setChecked(false);
                saturday.setChecked(false);
                sunday.setChecked(false);
            }
        });

        friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                newWorkout.dayOfWeek = DayOfWeek.FRIDAY;
                monday.setChecked(false);
                tuesday.setChecked(false);
                wednesday.setChecked(false);
                thursday.setChecked(false);
                friday.setChecked(true);
                saturday.setChecked(false);
                sunday.setChecked(false);
            }
        });

        saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                newWorkout.dayOfWeek = DayOfWeek.SATURDAY;
                monday.setChecked(false);
                tuesday.setChecked(false);
                wednesday.setChecked(false);
                thursday.setChecked(false);
                friday.setChecked(false);
                saturday.setChecked(true);
                sunday.setChecked(false);
            }
        });

        sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                newWorkout.dayOfWeek = DayOfWeek.SUNDAY;
                monday.setChecked(false);
                tuesday.setChecked(false);
                wednesday.setChecked(false);
                thursday.setChecked(false);
                friday.setChecked(false);
                saturday.setChecked(false);
                sunday.setChecked(true);
            }
        });
        //endregion
        // ----------------------------------------------------------

        // If Plan does NOT have Fixed Days
        if (!plan.fixedDays) {
            ConstraintLayout allCheckBoxes = popupView.findViewById(R.id.DaysOfWeekContainer);
            allCheckBoxes.setVisibility(View.GONE);
        }


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
            newWorkout = workoutService.converterWorkout(newWorkoutName.getText().toString(), newWorkoutDescription.getText().toString(), newWorkout.wType, newWorkout.dayOfWeek, plan.id);
            workoutService.insertWorkout(context, newWorkout);
            // Show Text on Screen
            Toast.makeText(context, "Workout Created",Toast.LENGTH_SHORT).show();
            // Need to wait for animation when is the last Exercise in List
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    planActivity.GetWorkoutList();
                    popupWindow.dismiss();     // Close Popup
                }
            }, 300); // 3000 milliseconds = 3 seconds

        });

        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }


    // --------------------------------------------------------------------------------------------
    public void EditWorkoutPopup(Context context, PlanActivity planActivity, Workout workout) {
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
            planActivity.GetWorkoutList();
            Toast.makeText(context, "Workout Modified",Toast.LENGTH_SHORT).show();
            // Close Popup
            popupWindow.dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            // Delete Workout
            WorkoutService workoutService = new WorkoutService();
            workoutService.deleteWorkout(context, workout);
            // Change Activity
            planActivity.GetWorkoutList();
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
                }, new PlanRVAdapter.OnClickEditPlanListener() {
                    @Override
                    public void editButtonClick(Plan plan) {

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
        View popupView = View.inflate(context, R.layout.popup_about_us, null);

        // Popup View UI Content
        TextView popupWarning = popupView.findViewById(R.id.AboutUsMessage);
        Button closeButton = popupView.findViewById(R.id.CloseAboutUsButton);

        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // Set Text Warning
        popupWarning.setText("Aplicativo desenvolvido por \nRicardo Thiago Firmino");

        // Button
        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }


    // --------------------------------------------------------------------------------------------
    public void addExercisePopup(Context context, WorkoutActivity activity) {
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
                                activity.AddExerciseToWorkout(item);
                                // Show Text on Screen
                                Toast.makeText(context, "Exercise Add", Toast.LENGTH_SHORT).show();

                                popupWindow.dismiss();
                            }
                        }, new SavedExerciseRVAdapter.OnShareClickListener() {
                            @Override
                            public void onItemShare(SavedExercise item) {
                                ShareService shareService = new ShareService();
                                Toast.makeText(context, "Work in progress", Toast.LENGTH_SHORT).show();
                            }
                        }, new SavedExerciseRVAdapter.OnEditClickListener() {
                            @Override
                            public void onItemEdit(SavedExercise item) {
                                //Intent intent = new Intent(activity, ExerciseActivity.class);
                               //intent.putExtra("SelectedPlan", thisPlan);
                               // intent.putExtra("SelectedWorkout", thisWorkout);
                               // intent.putExtra("SelectedExercise", item);
                              //  startActivity(intent);
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
                    activity.AddExerciseToWorkout(item);
                    // Show Text on Screen
                    Toast.makeText(context, "Exercise Add", Toast.LENGTH_SHORT).show();

                    popupWindow.dismiss();
                }
            }, new SavedExerciseRVAdapter.OnShareClickListener() {
                @Override
                public void onItemShare(SavedExercise item) {
                    ShareService shareService = new ShareService();
                    Toast.makeText(context, "Work in progress", Toast.LENGTH_SHORT).show();
                }
            }, new SavedExerciseRVAdapter.OnEditClickListener() {
                @Override
                public void onItemEdit(SavedExercise item) {
                    //Intent intent = new Intent(activity, ExerciseActivity.class);
                    //intent.putExtra("SelectedPlan", thisPlan);
                    // intent.putExtra("SelectedWorkout", thisWorkout);
                    // intent.putExtra("SelectedExercise", item);
                    //  startActivity(intent);
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
                    activity.AddExerciseToWorkout(item);
                    // Show Text on Screen
                    Toast.makeText(context, "Exercise Add", Toast.LENGTH_SHORT).show();

                    popupWindow.dismiss();
                }
            }, new SavedExerciseRVAdapter.OnShareClickListener() {
                @Override
                public void onItemShare(SavedExercise item) {
                    ShareService shareService = new ShareService();
                    Toast.makeText(context, "Work in progress", Toast.LENGTH_SHORT).show();
                }
            }, new SavedExerciseRVAdapter.OnEditClickListener() {
                @Override
                public void onItemEdit(SavedExercise item) {
                    //Intent intent = new Intent(activity, ExerciseActivity.class);
                    //intent.putExtra("SelectedPlan", thisPlan);
                    // intent.putExtra("SelectedWorkout", thisWorkout);
                    // intent.putExtra("SelectedExercise", item);
                    //  startActivity(intent);
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


    // --------------------------------------------------------------------------------------------
    public void editUserPlanName(Context context, Activity activity, Plan plan) {
        // Open new Popup where user create a new Plan
        // -------------------------------------------------------
        // Inflate Activity with a new View
        View popupView = View.inflate(context, R.layout.popup_edit_plan_name, null);

        // Popup View UI Content
        EditText newPlanName = popupView.findViewById(R.id.EditUserPlanName);
        Button confirmButton = popupView.findViewById(R.id.ConfirmEditUserPlanName);

        newPlanName.setText(plan.planName);

        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        PlanDao dao = db.planDao();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                plan.planName = newPlanName.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() { dao.updatePlan(plan); }
                }).start();
                activity.recreate();
                popupWindow.dismiss();
            }
        });

    }

}
