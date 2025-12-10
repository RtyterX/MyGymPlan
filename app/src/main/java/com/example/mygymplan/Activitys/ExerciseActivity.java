package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Enums.WorkoutType;
import com.example.mygymplan.Services.ExerciseService;
import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Services.ImageConverter;
import com.example.mygymplan.Services.PopupService;
import com.example.mygymplan.Services.TimerService;
import com.example.mygymplan.Services.WorkoutService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ExerciseActivity extends AppCompatActivity {

    // Entity's
    Plan thisPlan;
    Workout thisWorkout;                   // Know which workout to show when user goes back to workout activity
    Exercise thisExercise;                 // Used to save Exercise data
    int compareInt;                         // Used to check if Exercise is new or not

    String typeString;                     // Workout Type show on Dropdown Menu

    ExerciseService exerciseService = new ExerciseService();

    // -------------------------------------------------------------

    // UI Variables
    EditText showName;
    EditText showDescription;
    EditText showSets;
    EditText showReps;
    EditText showRest;
    EditText showLoad;

    Button saveExercise;
    Button deleteButton;
    ImageView timerButton;
    Button createVarient;


    // Workout Type Selector
    AutoCompleteTextView autoComplete;
    ArrayAdapter<String> adapterItem;

    // -------------------------------------------------------------

    // For Image Selection
    ImageView exerciseImage;
    VideoView exerciseVideo;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    // ------------------------------------
    // ------------ Enum ------------------
    // ------------------------------------
    String[] types = {
            "Chest",
            "Back",
            "Leg",
            "Shoulder",
            "Biceps",
            "Triceps",
            "Forearm",
            "Abs",
            "Quadriceps",
            "Hamstrings",
            "Adductors",
            "Glutes",
            "Cardio",
            "Stretching",
            "Strength",
            "Calisthenics"
    };


    // --- TIMER TEST ---
    double time;
    boolean timerUp;

    // Image Test
    Bitmap bitmap1;
    Uri selectedImageUri;
    ActivityResultLauncher<Intent> resultLauncher;
    ImageConverter imageConverter = new ImageConverter();


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
        thisPlan = (Plan) intent.getSerializableExtra("SelectedPlan");
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");
        thisExercise = (Exercise) intent.getSerializableExtra("SelectedExercise");
        compareInt = intent.getIntExtra("CompareInt",1);

        // Debug
        Log.d("Exercise Activity", "Selected Plan ID is: " + thisPlan.id);
        Log.d("Exercise Activity", "Selected Workout ID is: " + thisWorkout.id);
        Log.d("Exercise Activity", "Selected Exercise ID is: " + thisExercise.id);
        Log.d("Exercise Activity", "Selected Saved Exercise ID is: " + thisExercise.savedExercise_Id);
        Log.d("Exercise Activity", "Compare Int is: " + compareInt);


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
        exerciseVideo = findViewById(R.id.videoView);
        TextView noVideoImage = findViewById(R.id.NoVideoText);
        // Buttons
        saveExercise = findViewById(R.id.SaveExercise);
        deleteButton = findViewById(R.id.DeleteExerciseButton);
        timerButton = findViewById(R.id.TimerButton);
        createVarient = findViewById(R.id.CreateVariantButton);
        Button backButton = findViewById(R.id.BackButton3);

        // ------------------------------------------------------
        // ------------------ Dropdown Menu ---------------------
        // ------------------------------------------------------
        adapterItem = new ArrayAdapter<String>(this, R.layout.enum_list, types);
        if (thisExercise.type != null) {
            typeString = thisExercise.type.toString();
            autoComplete.setText(typeString); }
        // Only Attach Adapter if Exercise is User Created,check Compare Int...


        // --- Set UI Values ---
        SetCompereInt(compareInt);
        showDescription.setText(thisExercise.description);
        showSets.setText(String.valueOf(thisExercise.sets));
        showReps.setText(String.valueOf(thisExercise.reps));
        showRest.setText(String.valueOf(thisExercise.rest));
        showLoad.setText(String.valueOf(thisExercise.load));
        // Set Image
        ImageConverter imageConverter = new ImageConverter();
        exerciseImage.setImageBitmap(imageConverter.ConvertToBitmap(thisExercise.image));

        // Set Video
        if (thisExercise.video != null) {
            noVideoImage.setVisibility(View.GONE);
            exerciseVideo.setVideoPath("Teste");
        }
        else {
            exerciseVideo.setVisibility(View.GONE);
            noVideoImage.setVisibility(View.VISIBLE);
        }


        RegisterResult();


        // ------------------------------------------------------
        // -------------------- Buttons -------------------------
        // ------------------------------------------------------

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
                if (!Objects.equals(compareInt, 1)) {
                    SaveExerciseValues();
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
                // -----------------------------
                // If not new, Save Exercise
                // -----------------------------
                if (!Objects.equals(compareInt, 1)) {
                    SaveExerciseValues();
                }
                // ------------------
                ChangeActivity();
            }
        };


    }

    private void SetCompereInt(int compareInt) {
        switch (compareInt) {
            case 0:
                // If Exercise is New...
                // --------------------------------------------------------------------
                timerButton.setVisibility(View.GONE);           // Not necessary when creating new
                deleteButton.setVisibility(View.GONE);          // Not necessary when creating new
                createVarient.setVisibility(View.GONE);         // Not necessary when creating new
                // Alter Save Button Size
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,    // Set width to match_parent
                        ViewGroup.LayoutParams.WRAP_CONTENT     // Keep height as wrap_content or set to MATCH_PARENT
                );
                saveExercise.setLayoutParams(layoutParams);
                autoComplete.setAdapter(adapterItem);
                autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();
                        // Apply Type in Exercise
                        exerciseService.applyExerciseType(thisExercise, item);
                    }
                });
                exerciseImage.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        pickImage();
                    }
                });
                break;
            case 1:
                // If Exercise is created by User...
                // --------------------------------------------------------------------
                // Show already storage Values
                showName.setText(thisExercise.name);
                autoComplete.setAdapter(adapterItem);
                autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();
                        // Apply Type in Exercise
                        exerciseService.applyExerciseType(thisExercise, item);
                    }
                });
                exerciseImage.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        pickImage();
                    }
                });
                createVarient.setVisibility(View.GONE);
                break;
            case 2:
                // If Exercise is created by Database...
                // --------------------------------------------------------------------
                showName.setText(thisExercise.name);
                deleteButton.setVisibility(View.GONE);          // Cant Delete Exercise from Database
                saveExercise.setVisibility(View.GONE);
                createVarient.setVisibility(View.GONE);
                break;
            case 3:
                // For edit a already Saved Exercise
                // --------------------------------------------------------------------
                showName.setText(thisExercise.name);
                if (!thisExercise.userCreated) {
                    deleteButton.setVisibility(View.GONE);          // Cant Delete Exercise from Database
                }
                timerButton.setVisibility(View.GONE);
                createVarient.setVisibility(View.VISIBLE);
                break;

        }
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
                exerciseImage.setImageURI(imageUri);

                // Convert Image to Bitmap
                Drawable drawable = exerciseImage.getDrawable();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();

                // Convert to String
                thisExercise.image = imageConverter.ConvertToString(bitmap);

            }
        });
    }


    public void pickVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        resultLauncher.launch(intent);
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
        PopupService popupService = new PopupService();
        popupService.DeleteWarning(this, this, thisExercise);
    }

    public void OpenTimerOld() {
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
                                if (time >= 0) {
                                    timerText.setText(timerService.GetTimerTextInSeconds(time));
                                } else {
                                    startButton.setText("Start");
                                    timerText.setText(timerService.GetTimerTextInSeconds(Integer.parseInt(showRest.getText().toString())));
                                    time = Double.parseDouble(showRest.getText().toString());
                                    myTimer.cancel();
                                }

                            }
                        });
                    }

                }, 0, 1000);
            } else {
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

                    // Toast.makeText(getApplicationContext(), "Timer: " + timerService.GetTimerTextInSeconds(time), Toast.LENGTH_LONG).show();
                });
            }
        });

        closeButton.setOnClickListener(v2 -> {
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

        // Variables
        TimerService timerService = new TimerService();
        timerUp = false;
        String startTime = timerService.GetTimerTextInSeconds(Integer.parseInt(showRest.getText().toString()));
        timerText.setText(startTime);

        // Initialize new Popup View
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // Set Shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10.0f);
        // Set Popup Location on Screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // Set Buttons
        startButton.setOnClickListener(v -> {

            Timer myTimer = new Timer();

            if (!timerUp) {
                timerUp = true;
                startButton.setText("Stop");

                // Start Timer
                myTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (time >= 1) {
                            // Perform background work
                            time--;

                                    // Perform background work
                                    timerText.setText(timerService.GetTimerTextInSeconds(time));
                                } else {
                            myTimer.cancel();
                            time = Double.parseDouble(startTime);
                                    startButton.setText("Start");
                                    timerUp = false;
                                    myTimer.cancel();
                                    timerText.setText(startTime);
                            time = Double.parseDouble(showRest.getText().toString());
                                }

                    }
                }, 0, 1000);
            } else {
                timerUp = false;
                startButton.setText("Start");
                myTimer.cancel();
                timerText.setText(startTime);
                time = Double.parseDouble(startTime);
            }
        });

        closeButton.setOnClickListener(v2 -> {
            popupWindow.dismiss();
        });
    }


    // ----------------------------------------
    // -------------- Plus/Minus --------------
    // ----------------------------------------
    //region Plus/Minus Functions
    public void SetsMinus(View view) {
        if (thisExercise.sets > 1) {
            thisExercise.sets -= 1;
            showSets.setText(String.valueOf(thisExercise.sets));
        }
        else if (thisExercise.sets == 0) {
            thisExercise.sets = 1;
            showSets.setText(String.valueOf(thisExercise.sets));
        }
    }

    public void SetsPlus(View view) {
        if (thisExercise.sets < 20) {
            thisExercise.sets += 1;
            showSets.setText(String.valueOf(thisExercise.sets));
        }
    }

    public void RepsMinus(View view) {
        if (thisExercise.reps > 1) {
            thisExercise.reps -= 1;
            showReps.setText(String.valueOf(thisExercise.reps));
        }
        else if (thisExercise.reps == 0) {
            thisExercise.reps = 1;
            showReps.setText(String.valueOf(thisExercise.reps));
        }
    }

    public void RepsPlus(View view) {
        if (thisExercise.reps < 1000) {
            thisExercise.reps += 1;
            showReps.setText(String.valueOf(thisExercise.reps));
        }
    }

    public void RestMinus(View view) {
        if (thisExercise.rest > 0) {
            thisExercise.rest -= 1;
            showRest.setText(String.valueOf(thisExercise.rest));
        }
        if (thisExercise.rest <= 0) {
            thisExercise.rest = 0;
            showRest.setText("-");
        }
    }

    public void RestPlus(View view) {
        if (thisExercise.rest < 100) {
            thisExercise.rest += 1;
            showRest.setText(String.valueOf(thisExercise.rest));
        }
    }

    public void LoadMinus(View view) {
        if (thisExercise.load > 0) {
            thisExercise.load -= 1;
            showLoad.setText(String.valueOf(thisExercise.load));
        }
        if (thisExercise.load <= 0) {
            thisExercise.load = 0 ;
            showLoad.setText("-");
        }
    }

    public void LoadPlus(View view) {
        if (!Objects.equals(compareInt, "1")) {
            thisExercise.load = Integer.parseInt(showLoad.getText().toString());
        }
        if (thisExercise.load < 1000) {
            thisExercise.load += 1;
            showLoad.setText(String.valueOf(thisExercise.load));
        }
    }
    //endregion


    // --------------------------------------------------------------------------------
    // TIMER PICKER TESTS
    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


            }
        };
    }

    public class CustomTimePickerDialog extends TimePickerDialog {
        public CustomTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
            super(context, listener, hourOfDay, minute, is24HourView);
        }

        public CustomTimePickerDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
            super(context, themeResId, listener, hourOfDay, minute, is24HourView);
        }
    }




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
        thisExercise.name = showName.getText().toString();
        thisExercise.description = showDescription.getText().toString();
        thisExercise.sets = Integer.parseInt(showSets.getText().toString());
        thisExercise.reps = Integer.parseInt(showReps.getText().toString());
        thisExercise.rest = Integer.parseInt(showRest.getText().toString());
        thisExercise.load = Integer.parseInt(showLoad.getText().toString());
        thisExercise.lastModified = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"));
        // Type get from dropdown menu


        //----------------------------------------------
        //-------------- NEED MORES TESTS --------------
        //----------------------------------------------
        // ------ If Variable has not values -----
        if (thisExercise.name.isEmpty()) {
            thisExercise.name = "New Exercise";
        }
        if (thisExercise.description.isEmpty()) {
            thisExercise.description = "";
        }
        //if (thisExercise.eSets == Integer.parseInt(null)) {
        // thisExercise.eSets = 1;
        // }

        if (thisExercise.type == null) {
            thisExercise.type = WorkoutType.NA;
        }

        // ------------------------------------------------------------------------
        // Create if Exercise is New
        if (Objects.equals(compareInt, 0)) {
            // Save new Exercise in Workout and Store in Database
            exerciseService.createExercise(getApplicationContext(), thisExercise);
            // Show Text on Screen
            Toast.makeText(getApplicationContext(), "Exercise Created",Toast.LENGTH_SHORT).show();

        }
        // Update if Exercise is already created
        else {
            // Update Exercise Values
            exerciseService.saveExercise(getApplicationContext(), thisExercise);
            // Show Text on Screen
            Toast.makeText(getApplicationContext(), "Exercise Saved",Toast.LENGTH_SHORT).show();
        }
        // Update Duration Time in Workout
        WorkoutService workoutService = new WorkoutService();
        workoutService.updateWorkout(getApplicationContext(), thisWorkout);

        // ------------------------------------------------------------------------

    }


    // ----------------------------------------------------------
    // ---------------- Change Activity Back  -------------------
    // ----------------------------------------------------------
    public void ChangeActivity() {
        // --------------------------------------
        Intent intent = new Intent(ExerciseActivity.this, WorkoutActivity.class);
        intent.putExtra("SelectedPlan", thisPlan);
        intent.putExtra("SelectedWorkout", thisWorkout);
        startActivity(intent);
    }


    // ----------------------------------------------------------
    // ---------- Select Image From Gallery Test ----------------
    // ----------------------------------------------------------
    public void SelectImage(View view) {


    }

    protected void onActivityResultOld(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
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



    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "Image Selected", Toast.LENGTH_LONG).show();


        // -------------------------------
        // Check Permission
        // Activity.Compat.requestPermissions((Activity.this, new String[]{READ_MEDIA_IMAGES}, PackageManager.PERMISSION_GRANTED);

        // -------------------------------
        // Open Gallery
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                assert data != null;
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                     // update the preview image in the layout
                    exerciseImage.setImageURI(selectedImageUri);
                    // or
                    // exerciseImage.setImageBitmap(bitmap2);
                }
            }
        }

        // ----------------------------------------
        // Set Image in Bitmap to use when saving
        bitmap1 = BitmapFactory.decodeFile(String.valueOf(selectedImageUri));
         // Need to be Image Path in ( )
    }



    //////////////////////// END ////////////////////////
}