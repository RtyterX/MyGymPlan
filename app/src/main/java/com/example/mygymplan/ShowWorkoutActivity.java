package com.example.mygymplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import kotlinx.coroutines.scheduling.WorkQueueKt;

public class ShowWorkoutActivity extends AppCompatActivity {

    // Data
    public UserData user;
    public Plan thisPlan;
    public Workout thisWorkout;
    int i;  // Id for exercise already created

    String NewWorkoutCompareString;

    // UI Elements
    private EditText wName;
    ArrayList<Exercise> wExercises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");


        // Components
        wName = findViewById(R.id.WorkoutName);
        Button newExerciseButton = findViewById(R.id.CreateNewExercise);
        Button addExerciseButton = findViewById(R.id.AddExercise);
        Button saveWorkoutButton = findViewById(R.id.SaveWorkout);
        Button backButton = findViewById(R.id.BackButton2);
        Button deleteButton = findViewById(R.id.DeleteWorkoutButton);
        RecyclerView recyclerView = findViewById(R.id.RecycleViewWorkouts);


        // Set Workout Name based on Received Data
        wName.setText(thisWorkout.wName);
        i = thisWorkout.id;
        NewWorkoutCompareString = thisWorkout.wName;



        // Recycler View
        //RV_MyWorkoutAdapter adapter = new RV_MyWorkoutAdapter(this, thisPlan.planWorkouts);
        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // ----- BUTTONS -----

        // Create New Exercise
        newExerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Exercise newExercise = new Exercise(
                        user.myExercises.size(),
                        "New Exercise",
                        "Write your Description here...",
                        "4",
                        "8",
                        "1",
                        "0"
                );


                Intent intent = new Intent(ShowWorkoutActivity.this, ShowExerciseActivity.class);
 

                startActivity(intent);
            }
        });


        // Add a Already Created Exercise to Workout
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                ////////////////
                // Popup Test //
                ////////////////

                // Example of creating and showing a basic PopupWindow
               // LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               // View popupView = inflater2.inflate(R.layout.add_exercise_popup_layout, null); // Your custom layout for the popup
                //PopupWindow popupWindow = new PopupWindow(popupView,
                       // ViewGroup.LayoutParams.WRAP_CONTENT,
                        //ViewGroup.LayoutParams.WRAP_CONTENT,
                       // true); // true for focusable
                // Show the popup at a specific location, e.g., centered on the screen
                //popupWindow.showAtLocation(findViewById(R.id.activity_main), Gravity.CENTER, 0, 0);


                Exercise newExercise = new Exercise(
                        user.myExercises.size(),
                        "New Exercise",
                        "Write your description here...",
                        "4",
                        "8",
                        "1",
                        "0"
                );

                Intent intent = new Intent(ShowWorkoutActivity.this, ShowExerciseActivity.class);
                intent.putExtra("SelectedPlan", (Parcelable) thisPlan);
                intent.putExtra("SelectedWorkout", (Parcelable) thisWorkout);
                intent.putExtra("SelectedWorkout", (Parcelable) newExercise);

                startActivity(intent);

            }

        });
        // Criar um Layout add_exercise_popup_layout


        // Save Workout
        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Create if Workout is New
                if (Objects.equals(NewWorkoutCompareString, "New Workout")) {
                    Workout newWorkout = new Workout(
                            user.myWorkouts.size(),
                            wName.getText().toString(),
                            wExercises
                    );
                    user.myWorkouts.add(newWorkout);
                }
                // Save if Workout is already created
                else {
                    Workout saveWorkout = new Workout(
                            i,
                            wName.getText().toString(),
                            wExercises
                    );
                    user.myWorkouts.set(i, saveWorkout);
                }


                // Change Activity
                Intent intent = new Intent(ShowWorkoutActivity.this, MainActivity.class);
                intent.putExtra("SelectedPlan", (Parcelable) thisPlan);

                startActivity(intent);

            }
        });

        // Delete Exercise Button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                user.myWorkouts.remove(i);

                ///////////////////////////////////////
                // Carrega a Recycle View novamente //
                //////////////////////////////////////

            }
        });

        // Back Button
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ShowWorkoutActivity.this, MainActivity.class);
                intent.putExtra("SelectedPlan", (Parcelable) thisPlan);

                startActivity(intent);
            }

        });


        // Back Button 2 - Just for Tests
       // Button backButton2 = findViewById(R.id.BackButton2);
       // backButton2.setOnClickListener(new View.OnClickListener() {
           // public void onClick(View v) {

               // // Example of creating and showing a basic PopupWindow
              //  LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              //  View popupView = inflater2.inflate(R.layout.back_warning_layout, null); // Your custom layout for the popup
               // PopupWindow popupWindow = new PopupWindow(popupView,
                        //ViewGroup.LayoutParams.WRAP_CONTENT,
                        //ViewGroup.LayoutParams.WRAP_CONTENT,
                        //true); // true for focusable
                // Show the popup at a specific location, e.g., centered on the screen
                //popupWindow.showAtLocation(findViewById(R.id.show_workout_layout), Gravity.CENTER, 0, 0);


                //Intent intent = new Intent(ShowWorkoutActivity.this, MainActivity.class);
                //intent.putExtra("SelectedPlan", (Parcelable) thisPlan);

                //startActivity(intent);
            //}


        //});
    }

}


