package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class EditPlan extends AppCompatActivity {

    // Data
    UserData user;
    Plan thisPlan;
    int  i;
    String NewPlanCompareString;
     
    // UI Elements
    public EditText planName;
    ArrayList<Workout> displayedWorkouts;
    public boolean isActive;

    // Recycler View
    RV_MyWorkoutAdapter adapter;
    RecyclerView recyclerView;
    TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_plan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        user = (UserData) intent.getSerializableExtra("UserData");
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");


        // Components
        planName = findViewById(R.id.NewPlanName);
        Button activePlanButton = findViewById(R.id.ActivePlan);
        Button createNewWorkoutButton = findViewById(R.id.CreateNewWorkout);
        Button backButton = findViewById(R.id.BackButton);
        Button savePlanButton = findViewById(R.id.SavePlanButton);

        recyclerView = findViewById(R.id.PlanWorkoutsRecyclerView);
        emptyView = findViewById(R.id.EditPlanEmptyRVWorkouts);


        // Set Values based on Received Data
        planName.setText(thisPlan.planName);
        i = thisPlan.id;
        NewPlanCompareString = thisPlan.planName;
        displayedWorkouts = thisPlan.planWorkouts;


        // Recycler View
        RV_MyWorkoutAdapter adapter = new RV_MyWorkoutAdapter(this, displayedWorkouts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // ---- BUTTONS ----

        // Active this Plan as Main (show in Main Activity)
        activePlanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                thisPlan.active = !thisPlan.active;
            }
        });

        // Create New Workout
        createNewWorkoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ArrayList<Exercise> newExerciseList = new ArrayList<>();
                Workout newWorkout = new Workout(
                        0,
                        "New Workout test 2",
                        newExerciseList
                );

                Intent intent = new Intent(EditPlan.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);
                intent.putExtra("SelectedWorkout", newWorkout);
                startActivity(intent);

            }
        });

        // Save This Plan
        savePlanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Create if Workout is New
                if (Objects.equals(NewPlanCompareString, "New Plan")) {
                    Plan newPlan = new Plan(
                            user.myPlans.size(),
                            planName.getText().toString(),
                            displayedWorkouts,
                            false

                    );
                    user.myPlans.add(newPlan);
                }
                // Save if Workout is already created
                else {
                    Plan savePlan = new Plan(
                            i,
                            planName.getText().toString(),
                            displayedWorkouts,
                            false
                    );
                    user.myPlans.set(i, savePlan);
                }


                // Change Activity
                Intent intent = new Intent(EditPlan.this, MainActivity.class);
                intent.putExtra("SelectedPlan", (Parcelable) thisPlan);

                startActivity(intent);
            }
        });

        // Return to Main Activity
        backButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Plan alteredPlan = new Plan(
                        i,
                        planName.getText().toString(),
                        displayedWorkouts,
                        false
                );

                if (Objects.equals(thisPlan, alteredPlan)) {

                    Intent intent = new Intent(EditPlan.this, MainActivity.class);
                    intent.putExtra("SelectedPlan", (Parcelable) thisPlan);
                    startActivity(intent);
                }
                else {
                    // Show Warning
                }
            }
        });


        checkEmptyState();

    }

    private void checkEmptyState() {
        if (displayedWorkouts.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }


}