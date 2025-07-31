package com.example.mygymplan;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShowExerciseActivity extends AppCompatActivity {

    private TextView showName;
    // private Image showImage
    private TextView showDescription;
    private TextView showSeries;
    private TextView showReps;
    private TextView showRest;   // Time
    private TextView showLoad;

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
    }


    // showName = findViewById(R.id.showName);
    // showImage = findViewById(R.id.showImage);
    // showDescription = findViewById(R.id.showDescription);
    // showSeries = findViewById(R.id.showSeries);
    // showReps = findViewById(R.id.showReps);
    // showRest = findViewById(R.id.showRest);
   //  showLoad = findViewById(R.id.showLoad);


    // Show one Exercise
    public void ShowExercise(View v) {
        // showName = Exercise.eName; // ???
    }
}