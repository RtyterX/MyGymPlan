package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateExerciseActivity extends AppCompatActivity {

    private EditText eName;
    private ImageView eImage;
    private EditText eDescription;
    private EditText eSets;
    private EditText eReps;
    private EditText eRest;  // Time
    private EditText eLoad;

    private UserData user;
    public Plan thisPlan;
    public Workout thisWorkout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_exercise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisPlan = intent.getParcelableExtra("SelectedPlan");
        thisWorkout = intent.getParcelableExtra("SelectedWorkout");

        // Components
        eName = findViewById(R.id.NewExerciseName);
        eImage = findViewById(R.id.NewExerciseImage);
        eDescription = findViewById(R.id.NewExerciseDescription);
        eSets = findViewById(R.id.NewExerciseSets);
        eReps = findViewById(R.id.NewExerciseReps);
        eRest = findViewById(R.id.NewExerciseRest);
        eLoad = findViewById(R.id.NewExerciseLoad);

        // Save New Exercise
        Button saveExercise = (Button) findViewById(R.id.SaveNewExercise);
        saveExercise.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Save new Exercise
                user.myExercises = new Exercise[]{new Exercise(
                        eName.getText(),
                        eDescription.getText(),
                        eSets.getText(),
                        eReps.getText(),
                        eRest.getText(),
                        eLoad.getText()
                )};

                // Change Activity
                startActivity(new Intent(CreateExerciseActivity.this, CreateWorkoutActivity.class));
            }
        });

    }

}