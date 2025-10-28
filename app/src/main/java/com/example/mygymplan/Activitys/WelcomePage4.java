package com.example.mygymplan.Activitys;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mygymplan.R;

public class WelcomePage4 extends AppCompatActivity {

    UserData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_slide4);
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
        Button type1 = findViewById(R.id.NewUserBodyType1);
        Button type2 = findViewById(R.id.NewUserBodyType2);
        Button back = findViewById(R.id.backSlideButton);


        // Buttons
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.bodyType = 1;

                Intent intent = new Intent(WelcomePage4.this, WelcomePage5.class);
                intent.putExtra("SelectedUser", user);
                startActivity(intent);
            }
        });

        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.bodyType = 2;

                Intent intent = new Intent(WelcomePage4.this, WelcomePage5.class);
                intent.putExtra("SelectedUser", user);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomePage4.this, WelcomePage3.class);
                intent.putExtra("SelectedUser", user);
                startActivity(intent);
            }
        });

    }
}