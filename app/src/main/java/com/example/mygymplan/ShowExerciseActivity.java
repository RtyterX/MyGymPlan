package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class ShowExerciseActivity extends AppCompatActivity {

    // Data
    private UserData user;
    Plan thisPlan;
    Workout thisWorkout;
    Exercise thisExercise;
    int i;    // Id for exercise already created

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
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");
        // thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");
        thisExercise = (Exercise) intent.getSerializableExtra("SelectedExercise");
        //i = thisExercise.id;


        // Components
        EditText eName = findViewById(R.id.ExerciseName);
        // ImageView eImage = findViewById(R.id.ExerciseImage);
        EditText eDescription = findViewById(R.id.ExerciseDescription);
        EditText eSets = findViewById(R.id.ExerciseSets);
        EditText eReps = findViewById(R.id.ExerciseReps);
        EditText eRest = findViewById(R.id.ExerciseRest);
        EditText eLoad = findViewById(R.id.ExerciseLoad);
        Button saveExercise = findViewById(R.id.SaveExercise);
        Button deleteButton = findViewById(R.id.DeleteExerciseButton);    // Just for Test - Going to be in Recycle View
        Button backButton = findViewById(R.id.BackButton3);               // Just for Test - Going to be in Toolbar

        // Set UI Values
        //eName.setText(thisExercise.eName);
        // eImage.setText(intent.getParcelableExtra(thisExercise.eName));
        // eDescription.setText(intent.getParcelableExtra(thisExercise.eDescription));
        //eSets.setText(intent.getExtras(thisExercise.eSets));
        //Reps.setText(intent.get(String.valueOf((int) thisExercise.eReps)));
        // eRest.setText(intent.getParcelableExtra(String.valueOf((int) thisExercise.eRest)));
        // eLoad.setText(intent.getParcelableExtra(String.valueOf((int) thisExercise.eLoad)));

        // NewExerciseCompareString = thisExercise.eName;

        // ----- Buttons -----

        // Save New Exercise
        saveExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Create if Exercise is New
                if (Objects.equals(NewExerciseCompareString, "New Exercise")) {
                    Exercise newExercise = new Exercise(
                            user.myExercises.size(),
                            eName,
                            eDescription.getText(),
                            eSets.getText(),
                            eReps.getText(),
                            eRest.getText(),
                            eLoad.getText()
                    );
                    user.myExercises.add(newExercise);
                }
                // Save if Exercise is already created
                else {
                    Exercise saveExercise = new Exercise(
                            i,
                            eName,
                            eDescription.getText(),
                            eSets.getText(),
                            eReps.getText(),
                            eRest.getText(),
                            eLoad.getText()
                    );
                    user.myExercises.set(i, saveExercise);
                }

                // Change Activity
                Intent intent = new Intent(ShowExerciseActivity.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);
                intent.putExtra("SelectedWorkout", thisWorkout);

                startActivity(intent);
            }
        });


        // Delete Exercise Button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                user.myExercises.remove(i);

                Intent intent = new Intent(ShowExerciseActivity.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);
                intent.putExtra("SelectedWorkout", thisWorkout);

                startActivity(intent);
            }
        });

        // Back Button
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ShowExerciseActivity.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedPlan", thisPlan);
                intent.putExtra("SelectedWorkout", thisWorkout);

                startActivity(intent);
            }
        });

    }


}
