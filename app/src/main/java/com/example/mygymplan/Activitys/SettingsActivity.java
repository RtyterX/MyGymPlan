package com.example.mygymplan.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mygymplan.R;
import com.example.mygymplan.Services.ImageConverter;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    // -------------------------------------------------------------

    // Entity's
    String username;
    String email;
    int bodyType;
    String imageString;
    String language;

    // -------------------------------------------------------------

    // UI Elements
    EditText nameText;
    EditText emailText;
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

    // ------------------------------------
    // ------------ Enum ------------------
    // ------------------------------------
    String[] languages = {
            "Inglês",
            "Português"
    };

    // -------------------------------------------------------------

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


        // Get Shared Preferences
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        getSharedPreferences();


        // Set UI Values
        nameText.setText(username);
        emailText.setText(email);
        // Set Image
        ImageConverter imageConverter = new ImageConverter();
        Bitmap bitmap = imageConverter.ConvertToBitmap(imageString);
        userImage.setImageBitmap(bitmap);
        //Set Body Type Button Click
        if (bodyType == 1) {
            bodyType1.setText(username);   // Just For Test
        } else {
            bodyType2.setText(username);   // Just For Test
        }


        // ------------------------------------------------------
        // ------------------ Dropdown Menu ---------------------
        // ------------------------------------------------------
        adapterItem = new ArrayAdapter<String>(this, R.layout.enum_list, languages);
        autoComplete.setAdapter(adapterItem);
        autoComplete.setText(language);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                // Set Language on Shared Preferences
                setLanguage(item);
            }
        });


        // --- Buttons ---

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


    // ------------------------------
    // --------- Functions ----------
    // ------------------------------

    public void getSharedPreferences() {
        username = sharedPreferences.getString("username", "");
        email = sharedPreferences.getString("email", "");
        bodyType = sharedPreferences.getInt("bodyType", 0);
        language = sharedPreferences.getString("language", "");
        imageString = sharedPreferences.getString("userImageString", "");
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