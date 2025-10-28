package com.example.mygymplan.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.Workout;

public class TesteActivity extends AppCompatActivity {

    Workout thisWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teste);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button resetPreferences = findViewById(R.id.ResetSharedPreferences);
        Button backButton = findViewById(R.id.BackTestButton);
        SwitchCompat darkThemeSwitch = findViewById(R.id.DarkThemeSwitch);

        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");


        resetPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset Shared Preferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
            }
        });

        // Go Back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TesteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        darkThemeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }



    public void DeleteDataBase(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Context context = getApplicationContext();    // Or your activity context
                context.deleteDatabase("workouts");   // Just Change the Name

                //recreate();
            }
        }).start();
    }


}