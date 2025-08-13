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

// Alt + Enter = Import Classes

public class MainActivity extends AppCompatActivity {

    // Data
    UserData user;
    Plan thisPlan;
    ArrayList<Workout> planWorkouts;



    // UI
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

        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");

        assert thisPlan != null;
        planWorkouts = thisPlan.getPlanWorkouts();


        // Components
        Button editActualPlanButton = (Button) findViewById(R.id.EditActualPlan);
        Button createNewPlanButton = (Button) findViewById(R.id.CreateNewPlanButton);

        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyView = findViewById(R.id.EmptyRVWorkouts2);


        // Recycler View
        //RV_MyWorkoutAdapter adapter = new RV_MyWorkoutAdapter(this, thisPlan.planWorkouts);
        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Show Actual Plan or Empty Recycler View
        //if (actualPlan == null) {
        //  actualPlan = user.findMyActualPlan();
        // }


        // ----- BUTTONS -----

        // Change to Create New Workout Plan
        createNewPlanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                ThisIgualNewPlan();

                Workout newWorkout = new Workout(
                        0,
                        "New Workout"
                );

                Intent intent = new Intent(MainActivity.this, EditPlan.class);
                intent.putExtra("SelectedPlan", thisPlan);
                // intent.putExtra("SelectedWorkout", newWorkout);

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

    public void ThisIgualNewPlan() {
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

}