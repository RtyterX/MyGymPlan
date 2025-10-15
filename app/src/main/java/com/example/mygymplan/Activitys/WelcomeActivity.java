package com.example.mygymplan.Activitys;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class WelcomeActivity extends AppCompatActivity {


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
        TextView text = findViewById(R.id.WecomeText);
        Button start = findViewById(R.id.LetsStartButton);
        Button skip = findViewById(R.id.SkipWelcomeButton);


        // Buttons
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this, WelcomePage2.class);
                startActivity(intent);
            }

        });

        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                UserData user = new UserData();
                user.name = "No Name";

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

                                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                intent.putExtra("SelectedUser", user);
                                startActivity(intent);

                            }
                        });
                    }

                }).start();
            }

        });

    }

}