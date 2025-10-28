package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Services.ExerciseService;
import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Services.TimerService;
import com.example.mygymplan.Services.WorkoutService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ExerciseActivity extends AppCompatActivity {

    UserData user;
    Plan thisPlan;
    Workout thisWorkout;                   // Know which workout to show when user goes back to workout activity
    Exercise thisExercise;                 // Used to save Exercise data
    String NewExerciseCompareString;       // Used to check if Exercise is new or not

    ExerciseService exerciseService = new ExerciseService();

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


    // --- TIMER TEST ---
    double time;
    boolean timerUp;


    @SuppressLint("SourceLockedOrientationActivity")
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
        Button deleteButton = findViewById(R.id.DeleteExerciseButton);
        Button timerButton = findViewById(R.id.TimerButton);
        Button backButton = findViewById(R.id.BackButton3);


        // --- Set UI Values ---
        NewExerciseCompareString = thisExercise.eName;                         // Just to check if it's a New Exercise or Not
        // If Exercise isn't New...
        if (!Objects.equals(NewExerciseCompareString, "1")) {
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
                // Apply Type in Exercise
                exerciseService.ApplyExerciseType(thisExercise, item);
            }
        });


        // ------------------------------------------------------
        // -------------------- Buttons -------------------------
        // ------------------------------------------------------
        /////////////////// IMAGE BUTTON TEST ///////////////////
        exerciseImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });


        // -------------------------------------------
        // ------------- Save Exercise ---------------
        // -------------------------------------------

        saveExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SaveExerciseValues();
                // ------------------
                ChangeActivity();
            }
        });


        // -------------------------------------------
        // ------------- Delete Button ---------------
        // -------------------------------------------

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeleteWarning();
            }
        });


        // -------------------------------------------
        // ------------- Timer Button ---------------
        // -------------------------------------------

        timerButton.setOnClickListener(new View.OnClickListener() {
            public  void onClick(View v) {
                OpenTimer();
            }
        });


        // -------------------------------------------
        // ------------- Back Button -----------------
        // -------------------------------------------

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // -----------------------------
                // If not new, Save Exercise
                // -----------------------------
                if (!Objects.equals(NewExerciseCompareString, "1")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SaveExerciseValues();
                        }
                    }).start();
                }

                // ------------------
                ChangeActivity();
            }
        });


        // -------------------------------------------
        // ----------- BackPress Button --------------
        // -------------------------------------------

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                if (!Objects.equals(NewExerciseCompareString, "1")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            SaveExerciseValues();

                        }
                    }).start();
                }

                // ------------------
                ChangeActivity();
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

    private void DeleteWarning() {
        // Inflate Activity with a new View
        View popupView = View.inflate(this, R.layout.popup_warning, null);

        // Popup View UI Content
        TextView popupWarning = popupView.findViewById(R.id.WarningMessage);
        Button confirmButton = popupView.findViewById(R.id.ConfirmWarningButton);
        Button closeButton = popupView.findViewById(R.id.CloseWarningButton);

        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // Set Text Warning
        popupWarning.setText("Voce confirma que vai deletar?");

        // Set Buttons
        confirmButton.setOnClickListener(v -> {
            exerciseService.deleteExercise(getApplicationContext(), thisExercise);
            ChangeActivity();
        });
        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }

    public void OpenTimer() {
    // Inflate Activity with a new View
    View popupView = View.inflate(this, R.layout.popup_timer, null);

    // Popup View UI Content
    TextView timerText = popupView.findViewById(R.id.TimerText);
    ImageView indicatorImage = popupView.findViewById(R.id.TimerImageIndicator);
    Button startButton = popupView.findViewById(R.id.StartTimerButton);
    Button closeButton = popupView.findViewById(R.id.CloseTimerButton);

    timerUp = false;
    TimerService timerService = new TimerService();
    timerText.setText(timerService.GetTimerTextInSeconds(Integer.parseInt(showRest.getText().toString())));


    // Initialize new Popup View
    PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
    // Set Shadow
    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    popupWindow.setElevation(10.0f);
    // Set Popup Location on Screen
    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    // Set Buttons
        startButton.setOnClickListener(v -> {

            timerUp = !timerUp;

            startButton.setText("Stop");

            // Set Double Variable Time
            time = Double.parseDouble(showRest.getText().toString());
            Timer myTimer = new Timer();

            if (timerUp) {
                // Timer Start
                myTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (time >= 0) {
                            // Perform background work
                            time--;
                            //timerService.StartTimer(time, timerText, getApplicationContext());
                        }
                        // Update UI on the main thread
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI elements here
                                if (time >= 0)
                                {
                                    timerText.setText(timerService.GetTimerTextInSeconds(time));
                                }
                                else {
                                    startButton.setText("Start");
                                    timerText.setText(timerService.GetTimerTextInSeconds(Integer.parseInt(showRest.getText().toString())));
                                    time = Double.parseDouble(showRest.getText().toString());
                                    myTimer.cancel();
                                }

                            }
                        });
                    }

                }, 0, 1000);

            }
            else {
                // Update UI on the main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Update UI elements here
                        time = Double.parseDouble(showRest.getText().toString());
                        startButton.setText("Start");
                        timerText.setText(timerService.GetTimerTextInSeconds(Integer.parseInt(showRest.getText().toString())));
                        myTimer.cancel();
                    }
                });

            }


            // Toast.makeText(getApplicationContext(), "Timer: " + timerService.GetTimerTextInSeconds(time), Toast.LENGTH_LONG).show();


    });

        closeButton.setOnClickListener(v -> {
        popupWindow.dismiss();
    });

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

        LocalDate date = LocalDate.now();

        // Get Exercise Values
        thisExercise.eName = showName.getText().toString();
        thisExercise.eDescription = showDescription.getText().toString();
        thisExercise.eSets = Integer.parseInt(showSets.getText().toString());
        thisExercise.eReps = Integer.parseInt(showReps.getText().toString());
        thisExercise.eRest = Integer.parseInt(showRest.getText().toString());
        thisExercise.eLoad = Integer.parseInt(showLoad.getText().toString());
        thisExercise.lastModified = date.format(DateTimeFormatter.ofPattern("dd/MM"));


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


        // ------------------------------------------------------------------------
        // Create if Exercise is New
        if (Objects.equals(NewExerciseCompareString, "1")) {
            // Save new Exercise in Workout and Store in Database
            exerciseService.createExercise(getApplicationContext(), thisExercise);

        }
        // Update if Exercise is already created
        else {
            // Update Exercise Values
            exerciseService.saveExercise(getApplicationContext(), thisExercise);
        }
        // Update Last Modified Date in Workout
        WorkoutService workoutService = new WorkoutService();
        workoutService.updateWorkout(getApplicationContext(), thisWorkout);
        // ------------------------------------------------------------------------
    }


    // ----------------------------------------------------------
    // ---------------- Change Activity Back  -------------------
    // ----------------------------------------------------------
    private void ChangeActivity() {

        Intent intent = new Intent(ExerciseActivity.this, WorkoutActivity.class);
        intent.putExtra("SelectedUser", user);
        intent.putExtra("SelectedPlan", thisPlan);
        intent.putExtra("SelectedWorkout", thisWorkout);

        startActivity(intent);
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

    //////////////////////// END ////////////////////////
}