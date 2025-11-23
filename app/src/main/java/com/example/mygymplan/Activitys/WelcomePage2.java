package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mygymplan.R;

public class WelcomePage2 extends AppCompatActivity {

    String username;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_slide2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // Components
        EditText newUseName = findViewById(R.id.NewUserName);
        Button next = findViewById(R.id.NextPageButton1);
        Button back = findViewById(R.id.backSlideButton);


        // Buttons
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get value from EditText
                username = newUseName.getText().toString();

                // Check Username before Apply
                if (username.length() >= 3 && username.length() < 15) {
                    if (!username.equals("MyGymPlan")) {
                        // Insert in Shared Preferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.apply();

                        // Change Activity
                        Intent intent = new Intent(WelcomePage2.this, WelcomePage3.class);
                        startActivity(intent);
                    }
                    else {
                        // Show Error
                    }
                }
                else {
                    // Show Error
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomePage2.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

    }
}