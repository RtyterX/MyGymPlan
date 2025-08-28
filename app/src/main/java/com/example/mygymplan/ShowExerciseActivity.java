package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.Objects;

public class ShowExerciseActivity extends AppCompatActivity {

    // Data
    Workout thisWorkout;
    Exercise thisExercise;

    String NewExerciseCompareString;    // Used to check if Exercise is new or not


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_exercise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");
        thisExercise = (Exercise) intent.getSerializableExtra("SelectedExercise");


        // Components
        EditText showName = findViewById(R.id.ExerciseName);
        //ImageView showImage = findViewById(R.id.ExerciseImage);
        EditText showDescription = findViewById(R.id.ExerciseDescription);
        EditText showSets = findViewById(R.id.ExerciseSets);
        EditText showReps = findViewById(R.id.ExerciseReps);
        EditText showRest = findViewById(R.id.ExerciseRest);
        EditText showLoad = findViewById(R.id.ExerciseLoad);
        Button saveExercise = findViewById(R.id.SaveExercise);
        Button deleteButton = findViewById(R.id.DeleteExerciseButton);    // Just for Test - Going to be in Recycle View
        Button backButton = findViewById(R.id.BackButton3);               // Just for Test - Going to be in Toolbar


        // Set UI Values
        showName.setText(thisExercise.eName);
        // showImage.setText(intent.getParcelableExtra(thisExercise.eName));
        showDescription.setText(thisExercise.eDescription);
        showSets.setText(intent.getParcelableExtra(String.valueOf((int) thisExercise.eSets)));
        showReps.setText(intent.getParcelableExtra(String.valueOf((int) thisExercise.eReps)));
        showRest.setText(intent.getParcelableExtra(String.valueOf((int) thisExercise.eRest)));
        showLoad.setText(intent.getParcelableExtra(String.valueOf((int) thisExercise.eLoad)));

        // Just to Know if it's a New Exercise or Not
        NewExerciseCompareString = thisExercise.eName;


        // ----- Buttons -----

        // Save New Exercise
        saveExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                thisExercise.eName = showName.getText().toString();
                thisExercise.eDescription = showDescription.getText().toString();
                //thisExercise.eSets = showSets.);
                //thisExercise.eReps = Integer.parseInt(String.valueOf(showReps));
               // thisExercise.eRest = Integer.parseInt(String.valueOf(showRest));
               // thisExercise.eLoad = Integer.parseInt(String.valueOf(showLoad));

                        thisExercise.eSets = 1;
                        thisExercise.eReps = 1;
                        thisExercise.eRest = 1;
                        thisExercise.eLoad = 1;

                        // Database
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                        ExerciseDao dao = db.exerciseDao();

                        // Create if Exercise is New
                        if (Objects.equals(NewExerciseCompareString, "New Exercise")) {
                            // Save new Exercise
                            dao.insertExercise(thisExercise);
                        }
                        // Update if Exercise is already created
                        else {
                            // Save new Exercise
                            dao.updateExercise(thisExercise);
                        }

                    }

                }).start();

                // Change Activity
                Intent intent = new Intent(ShowExerciseActivity.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedWorkout", thisWorkout);

                startActivity(intent);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                        ExerciseDao dao = db.exerciseDao();
                        dao.deleteExercise(thisExercise);
                    }
                }).start();

                // Change Activity
                Intent intent = new Intent(ShowExerciseActivity.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedWorkout", thisWorkout);

                startActivity(intent);

            }
        });

    }


    public void deleteButton(View view) {
        finish();
    }
}
