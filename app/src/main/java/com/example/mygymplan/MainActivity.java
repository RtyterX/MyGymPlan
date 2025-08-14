package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
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

import kotlinx.coroutines.scheduling.WorkQueueKt;

// Alt + Enter = Import Classes

public class MainActivity extends AppCompatActivity {

    // Data
    UserData user;
    Plan thisPlan;
    ArrayList<Workout> displayedWorkouts;

    // UI


    // RecyclerView
    RV_MyWorkoutAdapter adapter;
    RecyclerView recyclerView;
    TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CheckIfHasUserData();

        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");
        

        // Components
        Button editActualPlanButton = findViewById(R.id.EditActualPlan);
        Button createNewPlanButton = findViewById(R.id.CreateNewPlanButton);
        TextView teste = findViewById(R.id.TesteUser2);


        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyView = findViewById(R.id.EmptyRVWorkouts2);


        // Set Values based on Received Data
        ThisEqualsNewPlan();
        teste.setText(thisPlan.planName);

        // assert thisPlan != null;  // Test
        // planWorkouts = thisPlan.getPlanWorkouts();


        // Recycler View
        //adapter = new RV_MyWorkoutAdapter(this, displayedWorkouts);
        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));



        // ----- BUTTONS -----

        // Change to Create New Workout Plan
        createNewPlanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                ThisEqualsNewPlan();
                ArrayList<Exercise> newExerciseList = new ArrayList<>();
                Workout newWorkout = new Workout(
                        0,
                        "New Workout",
                        newExerciseList
                );

                Intent intent = new Intent(MainActivity.this, EditPlan.class);
                intent.putExtra("UserData", user);
                intent.putExtra("SelectedPlan", thisPlan);
                intent.putExtra("SelectedWorkout", newWorkout);

                startActivity(intent);
            }
        });

        // Edit Active Workout Plan
        editActualPlanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(MainActivity.this, EditPlan.class);
                intent.putExtra("SelectedPlan", thisPlan);

                startActivity(intent);
            }
        });

         checkEmptyState();

    }

    public void ThisEqualsNewPlan() {
        ArrayList<Workout> newWorkoutList = new ArrayList<Workout>();
        thisPlan = new Plan(
                0,
                "Tyter é foda",
                newWorkoutList,
                true
        );

    }

    private void checkEmptyState(){
        if (thisPlan.planWorkouts.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void CheckIfHasUserData() {
        if (user == null) {

            ArrayList<Plan> basePlan = new ArrayList<>();
            ArrayList<Workout> baseWork = new ArrayList<>();
            ArrayList<Exercise> baseExercise = new ArrayList<>();

            user = new UserData("New user", basePlan, baseWork, baseExercise);
        }
        else {
            // Find Active Plan
            // thisPlan
        }
    }

}