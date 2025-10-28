package com.example.mygymplan.Activitys;

import android.content.Intent;
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

    UserData user;
    String name;

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

                name = newUseName.getText().toString();

                user = new UserData();
                user.name = name;

                Intent intent = new Intent(WelcomePage2.this, WelcomePage3.class);
                intent.putExtra("SelectedUser", user);
                startActivity(intent);
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