package com.example.mygymplan.Activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Database.SavedExerciseDao;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.Enums.ExperienceLevel;
import com.example.mygymplan.Enums.WorkoutType;
import com.example.mygymplan.R;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

public class WelcomePage5 extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
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


        // Components
        Button confirm = findViewById(R.id.StartPlanningButton);
        Button back = findViewById(R.id.backSlideButton);


        // Button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.createuserbg);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bytesImageEncoded = byteArrayOutputStream.toByteArray();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                String userImageString = Base64.encodeToString(bytesImageEncoded, Base64.DEFAULT);

                // Insert in Shared Preferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // Set Pro is False (buy later)
                editor.putBoolean("isPro", false);
                // All to Other Options
                editor.putString("language", "English");
                editor.putBoolean("poundsOverKg", false);
                editor.putString("userImageString", userImageString);
                editor.apply();

                // Change Activity
                Intent intent = new Intent(WelcomePage5.this, MainActivity.class);
                startActivity(intent);
                finish();

                StartDB();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomePage5.this, WelcomePage4.class);
                startActivity(intent);
            }
        });

    }

    private void StartDB() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "workouts").build();
        SavedExerciseDao savedExerciseDao =db.savedExerciseDao();
        ExerciseDao exerciseDao = db.exerciseDao();
        WorkoutDao workoutDao = db.workoutDao();
        PlanDao planDao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // ------------------------------------------------
                // ---------- SAVED EXERCISES ---------------------
                // ------------------------------------------------

                // --- Chest --- \\
                savedExerciseDao.rawInsert("Supino Reto com Barra","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Supino Inclinado com Barra","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Supino Declinado com Barra","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Supino Reto com Halter","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Supino Inclinado com Halter","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Supino Declinado com Halter","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Flexão","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Flexão Diamante","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Flexão Inclinada","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Flexão Declinada","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Fly","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Crossover","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Crucifixo","From Database",4,8,60,10, WorkoutType.Chest,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");

                // --- Leg --- \\
                savedExerciseDao.rawInsert("Leg Press 45°","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Leg Press 90°","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Agachamento","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Afundo","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Passada","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Búlgaro","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Elevação Pélvica","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Abdutora","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Adutora","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Mesa Flexora","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Cadeira Extensora","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Panturrilha","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Panturrilha na Maquina","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Panturrilha na Barra Fixa","From Database",4,8,60,10, WorkoutType.Leg,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");

                // --- Back --- \\
                savedExerciseDao.rawInsert("Remada Curvada","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Remada Alta","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Remada Baixa","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Puxada","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Puxada Alta","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Remada Curvada","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Puxada Baixa","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Barra","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Cavalinho","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Lombar","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Trapézio","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Serrote","From Database",4,8,60,10, WorkoutType.Back,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");

                // --- Biceps --- \\
                savedExerciseDao.rawInsert("Rosca Direta com Halter","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Rosca Direta com Barra","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Rosca Direta no Polia","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Rosca Alternada","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Rosca Martelo","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Rosca Spider com Halter","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Rosca Spider com Barra W","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Rosca Unilateral no Cabo","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Rosca Concentrada","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Scott com Barra W","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Scott na Maquina","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Scott Unilateral com Halter","From Database",4,8,60,10, WorkoutType.Biceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");

                // --- Triceps --- \\
                savedExerciseDao.rawInsert("Tríceps Testa","From Database",4,8,60,10, WorkoutType.Triceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Tríceps Corda","From Database",4,8,60,10, WorkoutType.Triceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Tríceps com Triângulo","From Database",4,8,60,10, WorkoutType.Triceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Tríceps com Barra na Polia","From Database",4,8,60,10, WorkoutType.Triceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Francês","From Database",4,8,60,10, WorkoutType.Triceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Coice","From Database",4,8,60,10, WorkoutType.Triceps,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");

                // --- Shoulders --- \\
                savedExerciseDao.rawInsert("Coice","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Elevação Lateral com Halter","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Elevação Lateral na Polia","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Elevação Lateral Inclinado no Banco","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Elevação Frontal com Halter","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Elevação Frontal com a Barra","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Elevação Frontal na Polia","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Fly Invertido","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Fly Invertido no Cross","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Fly Invertido no Cross Inclinado","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Fly Invertido no Cross Declinado","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Desenvolvimento","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");
                savedExerciseDao.rawInsert("Arnold","From Database",4,8,60,10, WorkoutType.Shoulder,false, LocalDate.now().toString(), LocalDate.now().toString(),"","");

                // ---------------------------------------
                // ---------- PLANS  ---------------------
                // ---------------------------------------
                // --- 1 - BEGINNER PLAN --- \\
                planDao.rawInsert("Beginner Plan","From Database",false,false, 1, ExperienceLevel.Beginner,"MyGymPlan",LocalDate.now().toString(),false);
                // - Workout 1 - \\
                workoutDao.rawInsert("Workout A","From Database",WorkoutType.Chest,0,"",null,LocalDate.now().toString(),1);
                // Exercises:

                // - Workout 2 - \\
                workoutDao.rawInsert("Workout B","From Database",WorkoutType.Back,1,"",null,LocalDate.now().toString(),1);
                // Exercises:

                // - Workout 3 - \\
                workoutDao.rawInsert("Workout C","From Database",WorkoutType.Leg,2,"",null,LocalDate.now().toString(),1);
                // Exercises:

                // --- 2 - INTERMEDIATE PLAN --- \\
                planDao.rawInsert("Intermediate Plan","From Database",false,false, 1, ExperienceLevel.Intermediate,"MyGymPlan",LocalDate.now().toString(),false);
                // - Workout 4 - \\
                workoutDao.rawInsert("Chest Workout","",WorkoutType.Chest,0,"",null,LocalDate.now().toString(),2);
                // Exercises:
                exerciseDao.rawInsert("Supino Reto com Barra","From Database",4,10,90,15,WorkoutType.Chest,0,LocalDate.now().toString(),2,4,0,"","");
                exerciseDao.rawInsert("Supino Inclinado com Halter","From Database", 4, 10, 90, 12, WorkoutType.Chest, 1,LocalDate.now().toString(), 2,4, 4,"","");
                exerciseDao.rawInsert("Crossover", "From Database",4,8,90,6,WorkoutType.Chest,2,LocalDate.now().toString(),1,4,11,"","");
                exerciseDao.rawInsert("Fly", "From Database",4,12,60,20,WorkoutType.Chest,3,LocalDate.now().toString(),1,4,10,"","");
                exerciseDao.rawInsert("Crucifixo", "From Database",4,8,60,8,WorkoutType.Chest,4,LocalDate.now().toString(),1,4,12,"","");
                exerciseDao.rawInsert("Triceps Testa", "From Database",4,8,60,12,WorkoutType.Triceps,5,LocalDate.now().toString(),2,4,52,"","");
                exerciseDao.rawInsert("Triceps Corda", "From Database",4,8,60,10,WorkoutType.Triceps,6,LocalDate.now().toString(),2,4,53,"","");
                // - Workout 5 - \\
                workoutDao.rawInsert("Chest Workout","",WorkoutType.Back,1,"",null,LocalDate.now().toString(),2);
                // Exercises:
                exerciseDao.rawInsert("Remada Curvada", "From Database",4,10,60,10,WorkoutType.Back,0,LocalDate.now().toString(),2,5,27,"","");
                exerciseDao.rawInsert("Puxada Alta", "From Database",3,10,60,20,WorkoutType.Back,1,LocalDate.now().toString(),2,5,31,"","");
                exerciseDao.rawInsert("Puxada Baixa", "From Database",3,10,60,20,WorkoutType.Back,2,LocalDate.now().toString(),2,5,32,"","");
                exerciseDao.rawInsert("Cavalinho", "From Database",4,8,90,20,WorkoutType.Back,3,LocalDate.now().toString(),2,5,34,"","");
                exerciseDao.rawInsert("Lombar", "From Database",4,8,60,0,WorkoutType.Back,4,LocalDate.now().toString(),2,5,35,"","");
                exerciseDao.rawInsert("Trapézio", "From Database",3,12,60,10,WorkoutType.Back,5,LocalDate.now().toString(),2,5,36,"","");
                exerciseDao.rawInsert("Serrote", "From Database",4,8,45,8,WorkoutType.Back,6,LocalDate.now().toString(),2,5,37,"","");
                // - Workout 6 - \\
                workoutDao.rawInsert("Chest Workout","",WorkoutType.Leg,2,"",null,LocalDate.now().toString(),2);
                // Exercises:
                exerciseDao.rawInsert("Agachamento", "From Database",4,8,90,20,WorkoutType.Leg,0,LocalDate.now().toString(),2,6,15,"","");
                exerciseDao.rawInsert("Cadeira Extensora", "From Database",3,8,60,15,WorkoutType.Leg,1,LocalDate.now().toString(),2,6,23,"","");
                exerciseDao.rawInsert("Mesa Flexora", "From Database",3,8,60,15,WorkoutType.Leg,2,LocalDate.now().toString(),2,6,22,"","");
                exerciseDao.rawInsert("Afundo", "From Database",3,8,60,10,WorkoutType.Leg,3,LocalDate.now().toString(),2,6,16,"","");
                exerciseDao.rawInsert("Abdutora", "From Database",3,8,45,20,WorkoutType.Leg,4,LocalDate.now().toString(),2,6,20,"","");
                exerciseDao.rawInsert("Adutora", "From Database",3,8,45,20,WorkoutType.Leg,5,LocalDate.now().toString(),2,6,21,"","");
                exerciseDao.rawInsert("Leg Press 45°", "From Database",4,8,90,80,WorkoutType.Leg,6,LocalDate.now().toString(),2,6,13,"","");
                exerciseDao.rawInsert("Panturilha", "From Database",3,15,45,20,WorkoutType.Leg,7,LocalDate.now().toString(),2,6,24,"","");
                // - Workout 7 - \\
                workoutDao.rawInsert("Chest Workout","",WorkoutType.Arm,3,"",null,LocalDate.now().toString(),2);
                // Exercises:
                exerciseDao.rawInsert("Rosca Direta no Polia", "From Database",3,8,60,10,WorkoutType.Biceps,0,LocalDate.now().toString(),2,7,42,"","");
                exerciseDao.rawInsert("Scott com Barra W", "From Database",4,8,90,16,WorkoutType.Biceps,1,LocalDate.now().toString(),2,7,49,"","");
                exerciseDao.rawInsert("Rosca Martelo", "From Database",4,8,60,8,WorkoutType.Biceps,2,LocalDate.now().toString(),2,7,44,"","");
                exerciseDao.rawInsert("Rosca Alternada", "From Database",4,8,90,8,WorkoutType.Biceps,3,LocalDate.now().toString(),2,7,43,"","");
                exerciseDao.rawInsert("Elevação Lateral com Halter", "From Database",4,8,45,7,WorkoutType.Shoulder,4,LocalDate.now().toString(),2,7,58,"","");
                exerciseDao.rawInsert("Desenvolvimento", "From Database",4,8,45,10,WorkoutType.Shoulder,5,LocalDate.now().toString(),2,7,62,"","");
                exerciseDao.rawInsert("Fly Invertido", "From Database",4,10,60,20,WorkoutType.Shoulder,6,LocalDate.now().toString(),2,7,64,"","");

                // --- 3 - ADVANCED PLAN --- \\
                planDao.rawInsert("Advanced Plan","From Database",false,false, 1, ExperienceLevel.Advanced,"MyGymPlan",LocalDate.now().toString(),false);
                // - Workout 8 - \\
                // Exercises:

                // - Workout 9 - \\
                // Exercises:

                // - Workout 10 - \\
                // Exercises:

                // - Workout 11 - \\
                // Exercises:

                // - Workout 12 - \\
                // Exercises:


                // ----------------------------------------------------------
                Log.d("Database", "Database Exercises Created");
            }
        }).start();

        db.close();
    }

}