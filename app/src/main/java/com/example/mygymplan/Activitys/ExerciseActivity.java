package com.example.mygymplan.Activitys;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.UserData;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Enums.WorkoutType;

import java.util.Objects;

public class ExerciseActivity extends AppCompatActivity {

    // Data
    UserData user;
    Plan thisPlan;
    Workout thisWorkout;                   // Know which workout to show when user goes back to workout activity
    Exercise thisExercise;                 // Used to save Exercise data
    String NewExerciseCompareString;       // Used to check if Exercise is new or not

    // UI Variables
    EditText showName;
    EditText showDescription;
    EditText showSets;
    EditText showReps;
    EditText showRest;
    EditText showLoad;

    // Workout Type Selector
    AutoCompleteTextView autoComplete;
    ArrayAdapter<String> adapterItem;

    // For Image Selection
    ImageView exerciseImage;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    // ------------------------------------
    // ------------ Enum ------------------
    // ------------------------------------
    String[] types = {
            "Chest",
            "Back",
            "Shoulder",
            "Arms",
            "Legs",
            "Biceps",
            "Triceps"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_exercise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.testePopup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Can't Rotate the Screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        user = (UserData) intent.getSerializableExtra("SelectedUser");
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");
        thisExercise = (Exercise) intent.getSerializableExtra("SelectedExercise");


        // --- Components ---
        showName = findViewById(R.id.ExerciseName);
        showDescription = findViewById(R.id.ExerciseDescription);
        showSets = findViewById(R.id.ExerciseSets);
        showReps = findViewById(R.id.ExerciseReps);
        showRest = findViewById(R.id.ExerciseRest);
        showLoad = findViewById(R.id.ExerciseLoad);
        // Enum Dropdown Menu
        autoComplete = findViewById(R.id.AutoCompleteEnumList);
        // Image
        exerciseImage = findViewById(R.id.ExerciseImage);
        // Buttons
        Button saveExercise = findViewById(R.id.SaveExercise);
        Button deleteButton = findViewById(R.id.DeleteExerciseButton);    // Just for Test - Going to be in Recycle View
        Button backButton = findViewById(R.id.BackButton3);


        // --- Set UI Values ---
        NewExerciseCompareString = thisExercise.eName;                   // Just to check if it's a New Exercise or Not
        // If Exercise isn't New...
        if (!Objects.equals(NewExerciseCompareString, "New Exercise")) {
            // Show already storage Values
            showName.setText(thisExercise.eName);
            showDescription.setText(thisExercise.eDescription);
            showSets.setText(String.valueOf(thisExercise.eSets));
            showReps.setText(String.valueOf(thisExercise.eReps));
            showRest.setText(String.valueOf(thisExercise.eRest));
            showLoad.setText(String.valueOf(thisExercise.eLoad));
            autoComplete.setText(thisExercise.eType.toString());
            // showImage.setText(intent.getParcelableExtra(thisExercise.eName));
        }


        // ------------------------------------------------------
        // ------------------ Dropdown Menu ---------------------
        // ------------------------------------------------------

        adapterItem = new ArrayAdapter<String>(this, R.layout.enum_list, types);
        autoComplete.setAdapter(adapterItem);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(ExerciseActivity.this, "Item selected: " + item, Toast.LENGTH_SHORT).show();
                ApplyExerciseType(item);
            }
        });


        // ------------------------------------------------------
        // -------------------- Buttons -------------------------
        // ------------------------------------------------------

        exerciseImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });


        // Save New Exercise
        saveExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SaveExerciseValues();

                // Change Activity
                Intent intent = new Intent(ExerciseActivity.this, WorkoutActivity.class);
                intent.putExtra("SelectedWorkout", thisWorkout);
                intent.putExtra("SelectedUser", user);

                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveExerciseValues();

                // Change Activity
                Intent intent = new Intent(ExerciseActivity.this, WorkoutActivity.class);
                intent.putExtra("SelectedUser", user);
                intent.putExtra("SelectedPlan", thisPlan);
                intent.putExtra("SelectedWorkout", thisWorkout);

                startActivity(intent);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup();
            }
        });

        // -------------------------------------------
        // ----------- BackPress Button --------------
        // -------------------------------------------

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                SaveExerciseValues();

                // Change Activity
                Intent intent = new Intent(ExerciseActivity.this, WorkoutActivity.class);
                intent.putExtra("SelectedUser", user);
                intent.putExtra("SelectedPlan", thisPlan);
                intent.putExtra("SelectedWorkout", thisWorkout);

                startActivity(intent);
            }
        };


    }



    // ---------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    // ------------------------------------------ BUTTONS ------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------




    // ------------------------------------------------------
    // ---------- Delete Exercise Popup  --------------------
    // ------------------------------------------------------

    private void showPopup() {
        // Inflate Activity with a new View
        View popupView = View.inflate(this, R.layout.popup_warning, null);

        // Popup View UI Content
        TextView popupWarning = popupView.findViewById(R.id.WarningMessage);
        Button confirmButton = popupView.findViewById(R.id.ConfirmWarningButton);
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);

        // Set height and width as WRAP_CONTENT
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;

        // Create the New View
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // Set Text Warning
        popupWarning.setText("Voce confirma que vai deletar?");

        // Set Buttons
        confirmButton.setOnClickListener(v -> {
            DeleteExercise();
        });
        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }

    private void DeleteExercise() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();
                dao.deleteExercise(thisExercise);

                // Change Activity
                Intent intent = new Intent(ExerciseActivity.this, WorkoutActivity.class);
                intent.putExtra("SelectedWorkout", thisWorkout);
                intent.putExtra("SelectedUser", user);

                startActivity(intent);
            }

        }).start();

    }





    // ----------------------------------------
    // -------------- Plus/Minus --------------
    // ----------------------------------------
    //region Plus/Minus Functions
    public void SetsMinus(View view) {
        if (thisExercise.eSets > 1) {
            thisExercise.eSets -= 1;
            showSets.setText(String.valueOf(thisExercise.eSets));
        }
        else if (thisExercise.eSets == 0) {
            thisExercise.eSets = 1;
            showSets.setText(String.valueOf(thisExercise.eSets));
        }
    }

    public void SetsPlus(View view) {
        if (thisExercise.eSets < 10) {
            thisExercise.eSets += 1;
            showSets.setText(String.valueOf(thisExercise.eSets));
        }
    }

    public void RepsMinus(View view) {
        if (thisExercise.eReps > 1) {
            thisExercise.eReps -= 1;
            showReps.setText(String.valueOf(thisExercise.eReps));
        }
        else if (thisExercise.eReps == 0) {
            thisExercise.eReps = 1;
            showReps.setText(String.valueOf(thisExercise.eReps));
        }
    }

    public void RepsPlus(View view) {
        if (thisExercise.eReps < 100) {
            thisExercise.eReps += 1;
            showReps.setText(String.valueOf(thisExercise.eReps));
        }
    }

    public void RestMinus(View view) {
        if (thisExercise.eRest > 1) {
            thisExercise.eRest -= 1;
            showRest.setText(String.valueOf(thisExercise.eRest));
        }
        else if (thisExercise.eRest == 0) {
            thisExercise.eRest = 1;
            showRest.setText(String.valueOf(thisExercise.eRest));
        }
    }

    public void RestPlus(View view) {
        if (thisExercise.eRest < 100) {
            thisExercise.eRest += 1;
            showRest.setText(String.valueOf(thisExercise.eRest));
        }
    }

    public void LoadMinus(View view) {
        if (thisExercise.eLoad > 1) {
            thisExercise.eLoad -= 1;
            showLoad.setText(String.valueOf(thisExercise.eLoad));
        } else if (thisExercise.eLoad < 1) {
            thisExercise.eLoad = 0 ;
            showLoad.setText("-");
        }
    }

    public void LoadPlus(View view) {
        if (thisExercise.eLoad < 1000) {
            thisExercise.eLoad += 1;
            showLoad.setText(String.valueOf(thisExercise.eLoad));
        }
    }
    //endregion



    // -------------------------------------------------------------------
    // -------------------------------------------------------------------
    // ------------------------- FUNCTIONS -------------------------------
    // -------------------------------------------------------------------
    // -------------------------------------------------------------------

    // -------------------------------------------------------
    // --------------- Save Values in Database ---------------
    // -------------------------------------------------------
    public void SaveExerciseValues() {

        // Get Exercise Values
        thisExercise.eName = showName.getText().toString();
        thisExercise.eDescription = showDescription.getText().toString();
        thisExercise.eSets = Integer.parseInt(showSets.getText().toString());
        thisExercise.eReps = Integer.parseInt(showReps.getText().toString());
        thisExercise.eRest = Integer.parseInt(showRest.getText().toString());
        thisExercise.eLoad = Integer.parseInt(showLoad.getText().toString());

        //----------------------------------------------
        //-------------- NEED MORES TESTS --------------
        //----------------------------------------------
        // ------ If Variable has not values -----
        if (thisExercise.eName.isEmpty()) {
            thisExercise.eName = "New Exercise";
        }
        if (thisExercise.eDescription.isEmpty()) {
            thisExercise.eDescription = "";
        }
        //if (thisExercise.eSets == Integer.parseInt(null)) {
        // thisExercise.eSets = 1;
        // }

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Access Database
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();

                // ------------------------------------------------------------------------
                // Create if Exercise is New
                if (Objects.equals(NewExerciseCompareString, "New Exercise")) {
                    // Save new Exercise
                    dao.insertExercise(thisExercise);
                }
                // Update if Exercise is already created
                else {
                    // Save new Exercise
                    dao.updateExercise(thisExercise);
                }
                // ------------------------------------------------------------------------
            }

        }).start();
    }


    // ----------------------------------------------------------
    // ---------- Switch to Change Exercise type ----------------
    // ----------------------------------------------------------

    private void ApplyExerciseType(String item){
        switch(item)  {
            case "Chest":
                thisExercise.eType = WorkoutType.Chest;
                break;
            case "Back":
                thisExercise.eType = WorkoutType.Back;
                break;
            case "Shoulder":
                thisExercise.eType = WorkoutType.Shoulder;
                break;
            case "Arms":
                thisExercise.eType = WorkoutType.Arms;
                break;
            case "Biceps":
                thisExercise.eType = WorkoutType.Biceps;
                break;
            case "Triceps":
                thisExercise.eType = WorkoutType.Triceps;
                break;
            case "Legs":
                thisExercise.eType = WorkoutType.Legs;
                break;
        }
    }


    // ----------------------------------------------------------
    // ---------- Select Image From Gallery Test ----------------
    // ----------------------------------------------------------
    public void SelectImage(View view) {


    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the
                    // layout
                    exerciseImage.setImageURI(
                            selectedImageUri);
                }
            }
        }
  
    }

}
