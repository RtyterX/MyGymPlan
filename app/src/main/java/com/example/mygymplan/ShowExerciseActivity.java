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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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
        EditText showName = findViewById(R.id.ExerciseName);
        // ImageView showImage = findViewById(R.id.ExerciseImage);
       // EditText showDescription = findViewById(R.id.ExerciseDescription);
        //EditText showSets = findViewById(R.id.ExerciseSets);
        //EditText showReps = findViewById(R.id.ExerciseReps);
        //EditText showRest = findViewById(R.id.ExerciseRest);
        //EditText showLoad = findViewById(R.id.ExerciseLoad);
        Button saveExercise = findViewById(R.id.SaveExercise);
        Button deleteButton = findViewById(R.id.DeleteExerciseButton);    // Just for Test - Going to be in Recycle View
        Button backButton = findViewById(R.id.BackButton3);               // Just for Test - Going to be in Toolbar


        // Set UI Values
        showName.setText(thisExercise.eName);
        // eImage.setText(intent.getParcelableExtra(thisExercise.eName));
        // eDescription.setText(intent.getParcelableExtra(thisExercise.eDescription));
        //eSets.setText(intent.getExtras(thisExercise.eSets));
        //Reps.setText(intent.get(String.valueOf((int) thisExercise.eReps)));
        // eRest.setText(intent.getParcelableExtra(String.valueOf((int) thisExercise.eRest)));
        // eLoad.setText(intent.getParcelableExtra(String.valueOf((int) thisExercise.eLoad)));

        //NewExerciseCompareString = thisExercise.eName;




        // ----- Buttons -----

        // Save New Exercise
        saveExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Create if Exercise is New
                //if (Objects.equals(NewExerciseCompareString, "New Exercise")) {

                Exercise newExercise = new Exercise();

                newExercise.eName = showName.getText().toString();
                //newExercise.eDescription = showDescription.getText().toString();
                //newExercise.eSets = Integer.parseInt(String.valueOf(showSets));
                // newExercise.eReps = Integer.parseInt(String.valueOf(showReps));
                //newExercise.eRest = Integer.parseInt(String.valueOf(showRest));
                //newExercise.eLoad = Integer.parseInt(String.valueOf(showLoad));


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Save new Exercise
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Exercises").build();
                        ExerciseDao dao = db.exerciseDao();

                        dao.insertExercise(newExercise);

                    }

                }).start();


                // Update if Exercise is already created
                // else {

                // Exercise updateExercise = new Exercise();

                // updateExercise.eName = showName.getText().toString();
                // updateExercise.eDescription = showDescription.getText().toString();
                // updateExercise.eSets = Integer.parseInt(String.valueOf(showSets));
                // updateExercise.eReps = Integer.parseInt(String.valueOf(showReps));
                // updateExercise.eRest = Integer.parseInt(String.valueOf(showRest));
                // updateExercise.eLoad = Integer.parseInt(String.valueOf(showLoad));;

                // try {
                //  new Thread(new Runnable() {
                // @Override
                // public void run() {
                // Save new Exercise
                // AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Contatos").build();
                //  ExerciseDao dao = db.exerciseDao();

                //   dao.insertExercise(updateExercise);


                // Change Activity
                finish(); // Just for Test

                // Intent intent = new Intent(ShowExerciseActivity.this, ShowWorkoutActivity.class);
                // intent.putExtra("SelectedPlan", thisPlan);
                // intent.putExtra("SelectedWorkout", thisWorkout);

                //startActivity(intent);
            }
        });


    }


}
