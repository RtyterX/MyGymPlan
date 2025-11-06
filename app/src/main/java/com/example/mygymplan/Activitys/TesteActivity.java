package com.example.mygymplan.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Services.ImageConverter;
import com.example.mygymplan.Services.PlanService;
import com.example.mygymplan.Services.ShareService;
import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.Workout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class TesteActivity extends AppCompatActivity {

    Plan testPlan;
    Workout[] String;
    Workout thisWorkout;
    TextView textBox;

    String username;

    String email;

    String userImageString;
    Bitmap bitmap1;

    ImageView imageView;
    ActivityResultLauncher<Intent> resultLauncher;


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

        Button sharePlan = findViewById(R.id.ShareTest);
        Button convertPlan = findViewById(R.id.ConvertTest);
        textBox = findViewById(R.id.ConvertTextBox);
        imageView = findViewById(R.id.TesteImage2);


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");


        RegisterResult();

        // ------------------------------------------------------------------------------------
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

        convertPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvertImage();
            }
        });

        sharePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareImage2();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }


    public void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    public void RegisterResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Uri imageUri = result.getData().getData();
                imageView.setImageURI(imageUri);
            }
        });
    }

    private void GetActivePlan() {

        PlanService planService = new PlanService();
        planService.getActivePlan(getApplicationContext(), testPlan);

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


    public void ShareImage2() {

        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.logo_fundoazul);
        imageView.setImageBitmap(bitmap1);


        ImageConverter imageConverter = new ImageConverter();
        String userImageString = imageConverter.ConvertToString(bitmap1);
        // bitmap1 = imageView.getDrawingCache();

        //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        //byte[] bytesImageEncoded = byteArrayOutputStream.toByteArray();

        //String userImageString = android.util.Base64.encodeToString(bytesImageEncoded, android.util.Base64.DEFAULT);

        // String userImageString;
        //userImageString = Base64.getEncoder().encodeToString(bytesImageEncoded);

        textBox.setText(userImageString);

        // Check if its user First time opening App
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Insert in Shared Preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userImageString", userImageString);
        editor.apply();

        Toast.makeText(getApplicationContext(), userImageString,Toast.LENGTH_SHORT).show();




        // ShareService shareService = new ShareService();
        // shareService.CreatePlanFromString(getApplicationContext(), (String) shareText.getText());
    }

    public void SharePlan(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Context context = getApplicationContext();    // Or your activity context
                context.deleteDatabase("workouts");   // Just Change the Name

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ShareService shareService = new ShareService();
                        shareService.ConvertPlanToString(getApplicationContext(), testPlan);

                    }
                });
            }
        }).start();


    }


    public void ConvertImage() {

       String stringToBlob = textBox.getText().toString();
       byte[] bytesImageDecoded = Base64.decode(stringToBlob, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytesImageDecoded);
        Bitmap bitmap2 = BitmapFactory.decodeStream(byteArrayInputStream);
        imageView.setImageBitmap(bitmap2);
    }


}