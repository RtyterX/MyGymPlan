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
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.UserData;
import com.example.mygymplan.Database.UserDataDao;

public class WelcomePage5 extends AppCompatActivity {

    UserData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_slide5);
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
        Button next = findViewById(R.id.StartPlanningButton);
        Button back = findViewById(R.id.backSlideButton);


        // Button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.isPro = false;

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // Save New User in Database
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                        UserDataDao dao = db.userDataDao();
                        dao.insertUser(user);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(WelcomePage5.this, MainActivity.class);
                                intent.putExtra("SelectedUser", user);
                                startActivity(intent);

                            }
                        });
                    }

                }).start();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomePage5.this, WelcomePage4.class);
                intent.putExtra("SelectedUser", user);
                startActivity(intent);
            }
        });

    }

}