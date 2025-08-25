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
    Button emptyButton;


    // RecyclerView
    RV_MyWorkoutAdapter adapter;
    RecyclerView recyclerView;
    TextView emptyView;
    TextView planName;


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
        planName = findViewById(R.id.MyWorkoutPlanText);
        Button editActualPlanButton = findViewById(R.id.EditActualPlan);
        Button testButton = findViewById(R.id.TestButton);

        recyclerView = findViewById(R.id.RecycleViewWorkouts);
        emptyView = findViewById(R.id.EmptyRVWorkouts2);
        emptyButton = findViewById(R.id.EmptyPlanButton);


        // Set Values based on Received Data
        // assert thisPlan != null;  // Test
        // planWorkouts = thisPlan.getPlanWorkouts();


        // Recycler View
        //adapter = new RV_MyWorkoutAdapter(this, displayedWorkouts);
        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));


        LoadData();


        // ----- BUTTONS -----

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TesteActivity.class);
                startActivity(intent);

            }
        });

        //////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////


        // Edit Active Workout Plan
        editActualPlanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(MainActivity.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);

                startActivity(intent);
            }
        });


    }

    public void ThisEqualsNewPlan() {

    }

    private void checkEmptyState(){
        if (displayedWorkouts.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyButton.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
            planName.setText("No Plans");
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyButton.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            planName.setText("Tyter é foda");
        }
    }

    private void CheckIfHasUserData() {

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
                        // Recycler View Adapter
                        RV_MyWorkoutAdapter adapter = new RV_MyWorkoutAdapter(MainActivity.this, displayedWorkouts, new RV_MyWorkoutAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Workout item) {
                                Intent intent = new Intent(MainActivity.this, ShowWorkoutActivity.class);

                                intent.putExtra("SelectedWorkout", item);
                                startActivity(intent);
                            }
                        });

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        checkEmptyState();
                    }
                });

            }
        }).start();

    }


    public void NewWorkout(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Create New Workout DataBase
                Workout newWorkout = new Workout();
                newWorkout.wName = "New Workout 2";

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

}