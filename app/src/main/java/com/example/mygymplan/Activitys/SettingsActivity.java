package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mygymplan.R;
import com.example.mygymplan.Services.ImageConverter;

public class SettingsActivity extends AppCompatActivity {

    // -------------------------------------------------------------

    // Entity's
    String username;
    String email;
    int bodyType;
    String userImageString;
    String language;

    // -------------------------------------------------------------

    // UI Elements
    TextView nameText;
    TextView emailText;
    Button bodyType1;
    Button bodyType2;
    ImageView userImage;

    // Language Selector
    AutoCompleteTextView autoComplete;
    ArrayAdapter<String> adapterItem;

    // -------------------------------------------------------------

    // Others
    boolean bodyType1Clicked;
    boolean bodyType2Clicked;
    SharedPreferences sharedPreferences;
    ImageConverter imageConverter = new ImageConverter();
    ActivityResultLauncher<Intent> resultLauncher;

    // ------------------------------------
    // ------------ Enum ------------------
    // ------------------------------------
    String[] languages = {
            "Inglês",
            "Português"
    };

    // -------------------------------------------------------------

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Can't Rotate the Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // Get UI Elements
        userImage = findViewById(R.id.SettingsPhoto);
        nameText = findViewById(R.id.SettingsNameText);
        emailText = findViewById(R.id.SettingsEmailText);
        bodyType1 = findViewById(R.id.SettingsBodyType1);
        bodyType2 = findViewById(R.id.SettingsBodyType2);
        autoComplete = findViewById(R.id.AutoCompleteLanguageList);
        Button backButton = findViewById(R.id.SettingsBackButton);
        Button editImage = findViewById(R.id.EditImageButton);
        ConstraintLayout editName = findViewById(R.id.EditNameConstraint);
        ConstraintLayout editEmail = findViewById(R.id.EditEmailConstraint);


        // Get Shared Preferences
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        getSharedPreferences();


        // Set UI Values
        nameText.setText(username);
        emailText.setText(email);
        // Set Image
        Bitmap bitmap = imageConverter.ConvertToBitmap(userImageString);
        userImage.setImageBitmap(bitmap);
        //Set Body Type Button Click
        if (bodyType == 1) {
            bodyType1.setText(username);   // Just For Test
        } else {
            bodyType2.setText(username);   // Just For Test
        }

        // Get URI from Pick Image
        RegisterResult();

        // ------------------------------------------------------
        // ------------------ Dropdown Menu ---------------------
        // ------------------------------------------------------
        autoComplete.setText(language);
        adapterItem = new ArrayAdapter<String>(this, R.layout.enum_list, languages);
        autoComplete.setAdapter(adapterItem);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                // Set Language on Shared Preferences
                setLanguage(item);
            }
        });


        // --- Buttons ---

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bodyType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodyType1Clicked = !bodyType1Clicked;
                bodyType2Clicked = false;
                if (bodyType1Clicked) {
                    setBodyType(1);
                } else {
                    setBodyType(0);
                }
                ;
            }
        });


        bodyType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodyType2Clicked = !bodyType2Clicked;
                bodyType1Clicked = false;
                if (bodyType2Clicked) {
                    setBodyType(2);
                } else {
                    setBodyType(0);
                }
            }
        });


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });


        // -------------------------------------------
        // ------------- Back Button -----------------
        // -------------------------------------------

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ------------------------------------
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    // --------------------------------------------------------------------------


    // ------------------------------------------------------
    // --------- Pick Image From Gallery (NaviBar) ----------
    // ------------------------------------------------------
    public void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    public void RegisterResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Uri imageUri = result.getData().getData();
                userImage.setImageURI(imageUri);

                // Convert Image to Bitmap
                Drawable drawable = userImage.getDrawable();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();

                // Convert to String
                userImageString = imageConverter.ConvertToString(bitmap);

                // Check if its user First time opening App
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();        // Insert in Shared Preferences
                editor.putString("userImageString", userImageString);
                editor.apply();

                // Re Create App
                recreate();
            }
        });
    }

    // ------------------------------
    // --------- Functions ----------
    // ------------------------------

    public void getSharedPreferences() {
        username = sharedPreferences.getString("username", "");
        email = sharedPreferences.getString("email", "");
        bodyType = sharedPreferences.getInt("bodyType", 0);
        language = sharedPreferences.getString("language", "");
        userImageString = sharedPreferences.getString("userImageString", "");
    }


    public void setName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Insert in Shared Preferences
        editor.putString("username", name);
        editor.apply();
    }


    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Insert in Shared Preferences
        editor.putString("email", email);
        editor.apply();
    }


    public void setLanguage(String language) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Insert in Shared Preferences
        editor.putString("language", language);
        editor.apply();
    }


    public void setBodyType(int type) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Insert in Shared Preferences
        if (type == 1) {
            editor.putInt("bodyType", 1);
            editor.apply();
        } else if (type == 2) {
            editor.putInt("bodyType", 2);
            editor.apply();
        } else {
            editor.putInt("bodyType", 0);
        }
        editor.apply();
    }

    //////////////////////////////////////////////
    public void KgToLb(View view) {
        // Change Kilograms to Lb
    }


/////////////// END //////////////////

}