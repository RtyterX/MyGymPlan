package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import kotlinx.coroutines.scheduling.WorkQueueKt;

public class ShowExerciseActivity extends AppCompatActivity {

    private TextView showExerciseName;
    // private Image showImage
    private TextView showDescription;
    private TextView showSets;
    private TextView showReps;
    private TextView showRest;   // Time
    private TextView showLoad;

    private UserData user;

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

        // ----- Receive Data From Another Activity Test ----
        // Intent intent = getIntent();

        // Workout myActualWorkout = intent.getParcelableExtra(myActualWorkout);


        // Examples:
        //String receivedString = intent.getStringExtra("key_string");
        //int receivedInt = intent.getIntExtra("key_int", 0); // 0 is a default value
        // MyCustomObject receivedObject = (MyCustomObject) intent.getSerializableExtra("key_object");
        // Or: MyCustomObject receivedObject = intent.getParcelableExtra("key_object");


// ----- Activity Assets -----

//showExerciseName = findViewById(R.id.ShowExerciseName);
// showImage = findViewById(R.id.ShowImage);
//showDescription = findViewById(R.id.ShowDescription);
//showSets = findViewById(R.id.ShowS);
//showReps = findViewById(R.id.ShowReps);
//showRest = findViewById(R.id.ShowRest);
//showLoad = findViewById(R.id.ShowLoad);

        Exercise thisExercise = new Exercise((Editable) showExerciseName.getText(),
                (Editable) showDescription.getText(),
                (Editable) showSets.getText(),
                (Editable) showReps.getText(),
                (Editable) showRest.getText(),
                (Editable) showLoad.getText()
        );


     // ---------- Buttons ----------

     // Save Exercise Edit
        //Button saveEditExercise = findViewById(R.id.SaveEditExercise);
        //saveEditExercise.setOnClickListener(new View.OnClickListener() {

        //public void SaveEditExercise(){

        // user.myWorkouts.getName().equals(thisWorkout).exercises.getName().equals(thisExerciseName)) = thisExercise;

        // Show one Exercise
        //public void ShowExercise(View v) {
        // showName = Exercise.eName; // ???
        //}
        // }
    }
}
