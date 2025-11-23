package com.example.mygymplan.Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Database.SavedExerciseDao;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Enums.ExperienceLevel;
import com.example.mygymplan.Enums.WorkoutType;
import com.example.mygymplan.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataInsert extends AppCompatActivity {

    public void PopulateDatabase(Context context) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                SavedExerciseDao savedExerciseDao = db.savedExerciseDao();
                ExerciseDao exerciseDao = db.exerciseDao();
                WorkoutDao workoutDao = db.workoutDao();
                PlanDao planDao = db.planDao();

                ImageConverter imageConverter = new ImageConverter();
                Bitmap defaultImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_fundoazul);


                // ------------------------------------------------
                // ---------- SAVED EXERCISES ---------------------
                // ------------------------------------------------

                // --- Chest --- \\
                savedExerciseDao.rawInsert("Supino Reto com Barra", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Supino Inclinado com Barra", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Supino Declinado com Barra", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Supino Reto com Halter", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Supino Inclinado com Halter", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Supino Declinado com Halter", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Flexão", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Flexão Diamante", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Flexão Inclinada", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Flexão Declinada", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Fly", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Crossover", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Crucifixo", "From Database", 4, 8, 60, 10, WorkoutType.Chest, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);

                // --- Leg --- \\
                savedExerciseDao.rawInsert("Leg Press 45°", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Leg Press 90°", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Agachamento", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Afundo", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Passada", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Búlgaro", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Elevação Pélvica", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Abdutora", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Adutora", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Mesa Flexora", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Cadeira Extensora", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Panturrilha", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Panturrilha na Maquina", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Panturrilha na Barra Fixa", "From Database", 4, 8, 60, 10, WorkoutType.Leg, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);

                // --- Back --- \\
                savedExerciseDao.rawInsert("Remada Curvada", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Remada Alta", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Remada Baixa", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Puxada", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Puxada Alta", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Puxada Baixa", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Puxada Unilateral", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Barra", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Cavalinho", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Lombar", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Trapézio", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Serrote", "From Database", 4, 8, 60, 10, WorkoutType.Back, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);

                // --- Biceps --- \\
                savedExerciseDao.rawInsert("Rosca Direta com Halter", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Rosca Direta com Barra", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Rosca Direta no Polia", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Rosca Alternada", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Rosca Martelo", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Rosca Spider com Halter", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Rosca Spider com Barra W", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Rosca Unilateral no Cabo", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Rosca Concentrada", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Scott com Barra W", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Scott na Maquina", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Scott Unilateral com Halter", "From Database", 4, 8, 60, 10, WorkoutType.Biceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);

                // --- Triceps --- \\
                savedExerciseDao.rawInsert("Tríceps Testa", "From Database", 4, 8, 60, 10, WorkoutType.Triceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Tríceps Corda", "From Database", 4, 8, 60, 10, WorkoutType.Triceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Tríceps com Triângulo", "From Database", 4, 8, 60, 10, WorkoutType.Triceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Tríceps com Barra na Polia", "From Database", 4, 8, 60, 10, WorkoutType.Triceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Francês", "From Database", 4, 8, 60, 10, WorkoutType.Triceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Coice", "From Database", 4, 8, 60, 10, WorkoutType.Triceps, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);

                // --- Shoulders --- \\
                savedExerciseDao.rawInsert("Elevação Lateral com Halter", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Elevação Lateral na Polia", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Elevação Lateral Inclinado no Banco", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Elevação Frontal com Halter", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Elevação Frontal com a Barra", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Elevação Frontal na Polia", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Fly Invertido", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Fly Invertido no Cross", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Fly Invertido no Cross Inclinado", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Fly Invertido no Cross Declinado", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Desenvolvimento", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);
                savedExerciseDao.rawInsert("Arnold", "From Database", 4, 8, 60, 10, WorkoutType.Shoulder, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);

                // --- Added Later --- \\
                savedExerciseDao.rawInsert("Rosca Invertida", "From Database", 4, 8, 60, 8, WorkoutType.Forearm, false, LocalDate.now().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), imageConverter.ConvertToString(defaultImage), null);


                // ---------------------------------------
                // ---------- PLANS  ---------------------
                // ---------------------------------------
                // --- 1 - BEGINNER PLAN --- \\
                planDao.rawInsert("Beginner Plan", "From Database", false, false, 1, ExperienceLevel.Beginner, "MyGymPlan", false, LocalDate.now().toString(), false);
                // - Workout 1 - \\
                workoutDao.rawInsert("Workout A", "From Database", WorkoutType.Chest, 1, "", null, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 1);
                // Exercises:

                // - Workout 2 - \\
                workoutDao.rawInsert("Workout B", "From Database", WorkoutType.Back, 2, "", null, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 1);
                // Exercises:

                // - Workout 3 - \\
                workoutDao.rawInsert("Workout C", "From Database", WorkoutType.Leg, 3, "", null, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 1);
                // Exercises:

                // --- 2 - INTERMEDIATE PLAN --- \\
                planDao.rawInsert("Intermediate Plan", "From Database", false, false, 1, ExperienceLevel.Intermediate, "MyGymPlan", false, LocalDate.now().toString(), false);
                // - Workout 4 - \\
                workoutDao.rawInsert("Chest Workout", "", WorkoutType.Chest, 1, "", null, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2);
                // Exercises:
                exerciseDao.rawInsert("Supino Reto com Barra", "From Database", 4, 10, 90, 15, WorkoutType.Chest, 1, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 4, 1, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Supino Inclinado com Halter", "From Database", 4, 10, 90, 12, WorkoutType.Chest, 2, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 4, 5, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Crossover", "From Database", 4, 8, 90, 6, WorkoutType.Chest, 3, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 4, 12, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Fly", "From Database", 4, 12, 60, 20, WorkoutType.Chest, 4, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 4, 11, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Crucifixo", "From Database", 4, 8, 60, 8, WorkoutType.Chest, 5, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 4, 13, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Triceps Testa", "From Database", 4, 8, 60, 12, WorkoutType.Triceps, 6, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 4, 52, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Triceps Corda", "From Database", 4, 8, 60, 10, WorkoutType.Triceps, 7, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 4, 53, imageConverter.ConvertToString(defaultImage), null);
                // - Workout 5 - \\
                workoutDao.rawInsert("Back Workout", "", WorkoutType.Back, 2, "", null, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2);
                // Exercises:
                exerciseDao.rawInsert("Remada Curvada", "From Database", 4, 10, 60, 10, WorkoutType.Back, 1, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 5, 28, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Puxada Alta", "From Database", 3, 10, 60, 20, WorkoutType.Back, 2, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 5, 32, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Puxada Baixa", "From Database", 3, 10, 60, 20, WorkoutType.Back, 3, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 5, 33, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Cavalinho", "From Database", 4, 8, 90, 20, WorkoutType.Back, 4, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 5, 36, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Serrote", "From Database", 4, 8, 45, 8, WorkoutType.Back, 5, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 5, 39, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Trapézio", "From Database", 3, 12, 60, 10, WorkoutType.Back, 6, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 5, 38, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Lombar", "From Database", 4, 8, 60, 0, WorkoutType.Back, 7, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 5, 37, imageConverter.ConvertToString(defaultImage), null);

                // - Workout 6 - \\
                workoutDao.rawInsert("Leg Workout", "", WorkoutType.Leg, 3, "", null, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2);
                // Exercises:
                exerciseDao.rawInsert("Agachamento", "From Database", 4, 8, 90, 20, WorkoutType.Leg, 1, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 6, 16, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Cadeira Extensora", "From Database", 3, 8, 60, 15, WorkoutType.Leg, 2, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 6, 24, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Mesa Flexora", "From Database", 3, 8, 60, 15, WorkoutType.Leg, 3, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 6, 23, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Afundo", "From Database", 3, 8, 60, 10, WorkoutType.Leg, 4, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 6, 17, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Abdutora", "From Database", 3, 8, 45, 20, WorkoutType.Leg, 5, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 6, 21, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Adutora", "From Database", 3, 8, 45, 20, WorkoutType.Leg, 6, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 6, 22, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Leg Press 45°", "From Database", 4, 8, 90, 80, WorkoutType.Leg, 7, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 6, 14, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Panturrilha", "From Database", 3, 15, 45, 20, WorkoutType.Leg, 8, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 6, 25, imageConverter.ConvertToString(defaultImage), null);
                // - Workout 7 - \\
                workoutDao.rawInsert("Arm Workout", "", WorkoutType.Arm, 4, "", null, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2);
                // Exercises:
                exerciseDao.rawInsert("Rosca Direta no Polia", "From Database", 3, 8, 60, 10, WorkoutType.Biceps, 1, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 7, 42, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Scott com Barra W", "From Database", 4, 8, 90, 16, WorkoutType.Biceps, 2, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 7, 49, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Rosca Martelo", "From Database", 4, 8, 60, 8, WorkoutType.Biceps, 3, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 7, 44, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Rosca Alternada", "From Database", 4, 8, 90, 8, WorkoutType.Biceps, 4, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 7, 43, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Elevação Lateral com Halter", "From Database", 4, 8, 45, 7, WorkoutType.Shoulder, 5, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 7, 58, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Desenvolvimento", "From Database", 4, 8, 45, 10, WorkoutType.Shoulder, 6, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 7, 62, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Fly Invertido", "From Database", 4, 10, 60, 20, WorkoutType.Shoulder, 7, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 2, 7, 64, imageConverter.ConvertToString(defaultImage), null);

                // --- 3 - ADVANCED PLAN --- \\
                planDao.rawInsert("Advanced Plan", "From Database", false, false, 1, ExperienceLevel.Advanced, "MyGymPlan", false, LocalDate.now().toString(), false);
                // - Workout 8 - \\
                workoutDao.rawInsert("Chest Workout", "", WorkoutType.Chest, 1, "", null, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 3);
                // Exercises:
                exerciseDao.rawInsert("Supino Reto com Barra", "From Database", 4, 10, 90, 15, WorkoutType.Chest, 1, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 3, 8, 1, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Supino Inclinado com Halter", "From Database", 4, 10, 90, 12, WorkoutType.Chest, 2, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 3, 8, 5, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Crossover", "From Database", 4, 8, 90, 6, WorkoutType.Chest, 3, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 3, 8, 11, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Fly", "From Database", 4, 12, 60, 20, WorkoutType.Chest, 4, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 3, 8, 10, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Crucifixo", "From Database", 4, 8, 60, 8, WorkoutType.Chest, 5, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 3, 8, 12, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Triceps Testa", "From Database", 4, 8, 60, 12, WorkoutType.Triceps, 6, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 3, 8, 52, imageConverter.ConvertToString(defaultImage), null);
                exerciseDao.rawInsert("Triceps Corda", "From Database", 4, 8, 60, 10, WorkoutType.Triceps, 7, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")), 3, 8, 53, imageConverter.ConvertToString(defaultImage), null);

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

                db.close();
            }
        }).start();
    }

}
