package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

// Alt + Enter = Import Classes

public class MainActivity extends AppCompatActivity {

    UserData user;
    private TextView yourName;
    public Plan actualPlan;
    // Workout[] planWorkouts;


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

        // Components
        Button editActualPlanButton = (Button) findViewById(R.id.EditActualPlan);
        Button createNewPlanButton = (Button) findViewById(R.id.CreateNewPlanButton);
        RecyclerView recyclerView = findViewById(R.id.RecycleViewWorkouts);

        // Show Actual Plan or Empty Recycler View
        //if (actualPlan == null) {
          //  actualPlan = user.findMyActualPlan();
       // }

        // Recycler View
       // RV_MyWorkoutAdapter adapter = new RV_MyWorkoutAdapter(this, actualPlan.planWorkouts);
       // recyclerView.setAdapter(adapter);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Click on RecycleView Workout
        // Intent intent = new Intent(MainActivity.this, ShowMyWorkoutActivity.class);
        // intent.putExtra("SelectedWorkout", Workout);
        //startActivity(intent);


        // Change to Create New Workout Plan
        createNewPlanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, ShowWorkoutActivity.class));
            }
        });

        // Edit Active Workout Plan
        editActualPlanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, EditPlan.class));
            }
        });



    }


}