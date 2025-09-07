package com.example.mygymplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.Objects;

public class ShowExerciseActivity extends AppCompatActivity {

    // Data
    Workout thisWorkout;                // Know which workout to show when user goes back to workout activity
    Exercise thisExercise;              // Used to save Exercise data
    String NewExerciseCompareString;    // Used to check if Exercise is new or not


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


        // ----- Received Data From Another Activity -----
        Intent intent = getIntent();
        thisWorkout = (Workout) intent.getSerializableExtra("SelectedWorkout");
        thisExercise = (Exercise) intent.getSerializableExtra("SelectedExercise");


        // Components
        EditText showName = findViewById(R.id.ExerciseName);
        //ImageView showImage = findViewById(R.id.ExerciseImage);
        EditText showDescription = findViewById(R.id.ExerciseDescription);
        EditText showSets = findViewById(R.id.ExerciseSets);
        EditText showReps = findViewById(R.id.ExerciseReps);
        EditText showRest = findViewById(R.id.ExerciseRest);
        EditText showLoad = findViewById(R.id.ExerciseLoad);
        Button saveExercise = findViewById(R.id.SaveExercise);
        Button deleteButton = findViewById(R.id.DeleteExerciseButton);    // Just for Test - Going to be in Recycle View
        Button backButton = findViewById(R.id.BackButton3);               // Just for Test - Going to be in Toolbar


        // Set UI Values
        showName.setText(thisExercise.eName);
        // showImage.setText(intent.getParcelableExtra(thisExercise.eName));
        showDescription.setText(thisExercise.eDescription);
        showSets.setText(String.valueOf(thisExercise.eSets));
        showReps.setText(String.valueOf(thisExercise.eReps));
        showRest.setText(String.valueOf(thisExercise.eRest));
        showLoad.setText(String.valueOf(thisExercise.eLoad));


        // Just to Know if it's a New Exercise or Not
        NewExerciseCompareString = thisExercise.eName;


        // ----- Buttons -----

        // Save New Exercise
        saveExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // Get Exercise Value
                        thisExercise.eName = showName.getText().toString();
                        thisExercise.eDescription = showDescription.getText().toString();
                        thisExercise.eSets = Integer.parseInt(showSets.getText().toString());
                        thisExercise.eReps = Integer.parseInt(showReps.getText().toString());
                        thisExercise.eRest = Integer.parseInt(showRest.getText().toString());
                        thisExercise.eLoad = Integer.parseInt(showLoad.getText().toString());

                        // Database
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                        ExerciseDao dao = db.exerciseDao();

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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                // Change Activity
                                Intent intent = new Intent(ShowExerciseActivity.this, ShowWorkoutActivity.class);
                                intent.putExtra("SelectedWorkout", thisWorkout);

                                startActivity(intent);

                            }

                        });

                    }

                }).start();

            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup();
            }
        });

    }

    public void deleteButton(View view) {
        finish();
    }

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
            deleteExercise();
        });
        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }

    private void deleteExercise() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
                ExerciseDao dao = db.exerciseDao();
                dao.deleteExercise(thisExercise);

                // Change Activity
                Intent intent = new Intent(ShowExerciseActivity.this, ShowWorkoutActivity.class);
                intent.putExtra("SelectedWorkout", thisWorkout);

                startActivity(intent);
            }

        }).start();

    }

}
