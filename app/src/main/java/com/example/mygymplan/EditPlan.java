package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    UserData user;
    Plan thisPlan;

    public EditText planName;
    ArrayList<Workout> thisPlanWorkouts;
    public boolean isActive;

    int  i;
    String NewPlanCompareString;


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
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");

        // Components
        planName = findViewById(R.id.NewPlanName);
        Button activePlanButton = findViewById(R.id.ActivePlan);
        Button createNewWorkoutButton = findViewById(R.id.CreateNewWorkout);
        Button backButton = findViewById(R.id.BackButton);
        Button savePlanButton = findViewById(R.id.SavePlanButton);
        RecyclerView recyclerView = findViewById(R.id.PlanWorkoutsRecyclerView);

        // Set Plan Name based on Received Data
        planName.setText(thisPlan.planName);
        i = thisPlan.id;
        NewPlanCompareString = thisPlan.planName;

        // Recycler View
        //RV_MyWorkoutAdapter adapter = new RV_MyWorkoutAdapter(this, thisPlan.planWorkouts);
        //recyclerView.setAdapter(adapter);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));



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

                Workout newWorkout = new Workout(
                        0,
                        "New Workout test"
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
                            thisPlanWorkouts,
                            false

                    );
                    user.myPlans.add(newPlan);
                }
                // Save if Workout is already created
                else {
                    Plan savePlan = new Plan(
                            i,
                            planName.getText().toString(),
                            thisPlanWorkouts,
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
                        thisPlanWorkouts,
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

    }


}