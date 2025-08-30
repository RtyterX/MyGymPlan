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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

// Alt + Enter = Import Classes

public class MainActivity extends AppCompatActivity {

    // Data
    UserData user;
    Plan thisPlan;
    List<Workout> displayedWorkouts;

    // UI
    TextView planName;
    Button emptyButton;
    Button testButton;

    // RecyclerView
    WorkoutRVAdapter adapter;
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
        

        // Components
        planName = findViewById(R.id.MyWorkoutPlanText);
        Button editActualPlanButton = findViewById(R.id.EditActualPlan);
        testButton = findViewById(R.id.TestButton);
        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyView = findViewById(R.id.EmptyRVWorkouts2);
        emptyButton = findViewById(R.id.EmptyPlanButton);


        // Set Values based on Received Data
        // Needed??


        LoadData();
        //CheckIfHasUserData(); // For user Name or what ever


        // ----- BUTTONS -----

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TesteActivity.class);
                startActivity(intent);

            }
        });


        // Edit Active Workout Plan
        editActualPlanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(MainActivity.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);

                startActivity(intent);
            }
        });


    }


    // ---------- BUTTONS -----------

    public void NewWorkout(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Create New Workout DataBase
                Workout newWorkout = new Workout();
                newWorkout.wName = "New Workout";

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                WorkoutDao dao = db.workoutDao();

                dao.insertWorkout(newWorkout);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Change Activity
                        Intent intent = new Intent(MainActivity.this, ShowWorkoutActivity.class);
                        intent.putExtra("SelectedWorkout", newWorkout);

                        startActivity(intent);
                    }
                });

            }
        }).start();

    }


    // ---------- METHODS -----------

    private void CheckIfHasUserData() {

    }

    private void CheckEmptyState(){
        if (displayedWorkouts.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyButton.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
            Button editActualPlanButton = findViewById(R.id.EditActualPlan);
            editActualPlanButton.setVisibility(View.GONE);
            Button newWorkout = findViewById(R.id.NewWorkout);
            newWorkout.setVisibility(View.GONE);
            planName.setText("No Plans");
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyButton.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            planName.setText("Tyter é foda");
        }
    }


    public void LoadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                WorkoutDao dao = db.workoutDao();

                displayedWorkouts = dao.listWorkouts();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //  Set Recycler View Adapter
                        WorkoutRVAdapter adapter = new WorkoutRVAdapter(MainActivity.this, displayedWorkouts, new WorkoutRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Workout item) {
                                Intent intent = new Intent(MainActivity.this, ShowWorkoutActivity.class);

                                intent.putExtra("SelectedWorkout", item);
                                startActivity(intent);
                            }
                        });

                        // Display Recycler View
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                        CheckEmptyState();
                    }
                });

            }
        }).start();

    }

}