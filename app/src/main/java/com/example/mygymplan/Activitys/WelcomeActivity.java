package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.R;
import com.example.mygymplan.Services.SavedExerciseService;

public class WelcomeActivity extends AppCompatActivity {

    // User Info
    String username;
    String email;
    int bodyType;
    String language;

    // Change Pages
    int page = 0;
    Button next;
    Button back;
    Button skip;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Can't Rotate the Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // Components
        Button start = findViewById(R.id.LetsStartButton);
        Button skip = findViewById(R.id.SkipWelcomeButton);


        // Buttons
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this, WelcomePage2.class);
                startActivity(intent);
            }

        });


        //////////////////////// FOR TESTING //////////////////////////////////////

        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SkipPrefs();

                // Change Activity
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });


        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                page++;
                switchScreens();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                page--;
                switchScreens();

            }
        });
    }



    private void switchScreens() {
        switch (page) {
            case 0:
                next.setText("Start");
                back.setVisibility(View.GONE);
                skip.setVisibility(View.VISIBLE);
                break;
            case 1:

                back.setVisibility(View.VISIBLE);
                skip.setVisibility(View.GONE);
                break;
            case 2:


                break;
            case 3:


                break;
            case 4:
                SavePrefs();
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                break;

        }
    }


    private void SavePrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", username);
        editor.putString("email", email);
        editor.putInt("bodyType", bodyType);
        editor.putString("language", language);
        editor.putBoolean("poundsOverKg", false);
        editor.putBoolean("isPro", false);
        // ----------- FUTURE IDEAS ------------
        // Height
        // Weight

        editor.apply();
    }

    private void SkipPrefs() {

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", "No Name");
        editor.putString("email", "teste@gmail.com");
        editor.putInt("bodyType", 1);
        editor.putString("language", "English");
        editor.putBoolean("poundsOverKg", false);
        editor.putBoolean("isPro", false);
        // ----------- FUTURE IDEAS ------------
        // Height
        // Weight

        editor.apply();
    }

    //////////////////////// END ////////////////////////
}