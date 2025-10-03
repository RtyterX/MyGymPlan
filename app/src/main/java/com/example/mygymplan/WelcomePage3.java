package com.example.mygymplan;

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

public class WelcomePage3 extends AppCompatActivity {

    UserData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_slide3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        user = (UserData) intent.getSerializableExtra("SelectedUser");


        // Components
        EditText email = findViewById(R.id.NewUserEmail);
        Button next = findViewById(R.id.NextPageButton2);
        Button back = findViewById(R.id.backSlideButton);


        // Buttons
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.email = email.getText().toString();

                Intent intent = new Intent(WelcomePage3.this, WelcomePage4.class);
                intent.putExtra("SelectedUser", user);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomePage3.this, WelcomePage2.class);
                intent.putExtra("SelectedUser", user);
                startActivity(intent);
            }
        });
    }
}