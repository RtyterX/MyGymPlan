package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShowExerciseActivity extends AppCompatActivity {

    private UserData user;
     Plan thisPlan;
     Workout thisWorkout;


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
        thisPlan = intent.getParcelableExtra("SelectedPlan");
        thisWorkout = intent.getParcelableExtra("SelectedWorkout");

        // Examples to Test:
        //String receivedString = intent.getStringExtra("key_string");
        //int receivedInt = intent.getIntExtra("key_int", 0); // 0 is a default value
        // MyCustomObject receivedObject = (MyCustomObject) intent.getSerializableExtra("key_object");
        // Or: MyCustomObject receivedObject = intent.getParcelableExtra("key_object");


        // Components
        EditText eName = findViewById(R.id.ExerciseName);
        ImageView eImage = findViewById(R.id.ExerciseImage);
        EditText eDescription = findViewById(R.id.ExerciseDescription);
        EditText eSets = findViewById(R.id.ExerciseSets);
        EditText eReps = findViewById(R.id.ExerciseReps);
        EditText eRest = findViewById(R.id.ExerciseRest);
        EditText eLoad = findViewById(R.id.ExerciseLoad);
        Button saveExercise = findViewById(R.id.SaveExercise);


        // ----- Buttons -----

        // Save New Exercise
        saveExercise.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Save new Exercise
                user.myExercises = new Exercise[]{new Exercise(
                        eName,
                        eDescription.getText(),
                        eSets.getText(),
                        eReps.getText(),
                        eRest.getText(),
                        eLoad.getText()
                )};

                // Change Activity
                startActivity(new Intent(ShowExerciseActivity.this, ShowWorkoutActivity.class));
            }
        });

    }

}
