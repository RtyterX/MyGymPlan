package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EditPlan extends AppCompatActivity {

    UserData user;
    Plan thisPlan;
    Plan alteredPlan;

    public EditText planName;
    ArrayList<Workout> thisPlanWorkouts;
    public Boolean isActive;


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
        thisPlan = intent.getParcelableExtra("SelectedPlan");

        // Components
        planName = findViewById(R.id.NewPlanName);
        Button activePlanButton = findViewById(R.id.ActivePlan);
        Button createNewWorkoutButton = findViewById(R.id.CreateNewWorkout);
        Button backButton = findViewById(R.id.BackButton);
        Button savePlanButton = findViewById(R.id.SavePlanButton);
        RecyclerView recyclerView = findViewById(R.id.PlanWorkoutsRecyclerView);

        // Recycler View
        RV_MyWorkoutAdapter adapter = new RV_MyWorkoutAdapter(this, thisPlan.planWorkouts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Change Active Plan Button text to Deactivate, if is already Active
        if (thisPlan.active)
        {
            activePlanButton.setText("Deactive");
        }


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

                Intent intent = new Intent(EditPlan.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedPlan", (Parcelable) thisPlan);
                startActivity(intent);

            }
        });

        // Save This Plan
        savePlanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                alteredPlan = new Plan(planName.toString(), thisPlanWorkouts, isActive);
                thisPlan = alteredPlan;
            }
        });

        // Return to Main Activity
        backButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (thisPlan == alteredPlan) {
                    startActivity(new Intent(EditPlan.this, MainActivity.class));
                }
                else {
                    // Show Warning
                }
            }
        });

    }
}