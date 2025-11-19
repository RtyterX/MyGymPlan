package com.example.mygymplan.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Database.PlanDao;
import com.example.mygymplan.Database.WorkoutDao;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Enums.WorkoutType;
import android.content.Context;
import android.util.Log;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutService extends AppCompatActivity {

    int newWorkoutId = 0;

    WorkoutDuration workoutDuration = new WorkoutDuration();


    // ---------------------------------------------------------------------------------------------------
    public List<Workout> listAllWorkout(Context context) {

        List<Workout> allWorkouts;

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        allWorkouts = dao.listWorkouts();

        db.close();

        return allWorkouts;
    }

    // ---------------------------------------------------------------------------------------------------
    public void insertWorkout(Context context, Workout workout) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();
        PlanDao planDao = db.planDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Workout> allWorkouts = new ArrayList<>();
                allWorkouts = dao.listWorkouts();

                // Get Higher ID value
                for (Workout item : allWorkouts) {
                    if (newWorkoutId < item.id) {
                        newWorkoutId = item.id;
                    }
                }

                // Set Workout ID
                // (Workout with higher ID value + 1)
                workout.id = newWorkoutId + 1;

                // ----------------------------------------

                // Get Workout Plan
                Plan plan = new Plan();
                List<Plan> allPlans = planDao.listPlans();
                for (Plan item : allPlans) {
                    if (item.id == workout.plan_Id) {
                        plan = item;
                    }
                }

                // List All Workouts from this Plan
                List<Workout> planWorkouts = new ArrayList<>();
                for (Workout item : allWorkouts) {
                    if (item.plan_Id == plan.id) {
                        planWorkouts.add(item);
                    }
                }

                // Set New Workout Order based on list Size
                if (!planWorkouts.isEmpty()) {
                    workout.sequence = planWorkouts.size();
                }
                else {
                    workout.sequence = 0;
                }

                // Set Duration Time
                ExerciseDao daoExercise = db.exerciseDao();
                List<Exercise> allExercises = daoExercise.listExercise();
                workout.duration = workoutDuration.CalculateDurationTime(workout, allExercises);

                // Insert in Database
                dao.insertWorkout(workout);

                Log.d("Teste Workout ID", "Workout Created - ID: " + workout.id);
            }
        }).start();

        db.close();
    }


    // ---------------------------------------------------------------------------------------------------
    public void updateWorkout(Context context, Workout workout) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Set Duration Time
                ExerciseDao daoExercise = db.exerciseDao();
                List<Exercise> allExercises = daoExercise.listExercise();
                workout.duration = workoutDuration.CalculateDurationTime(workout, allExercises);

                dao.updateWorkout(workout);
            }
        }).start();

        db.close();
    }


    // ---------------------------------------------------------------------------------------------------
    public void deleteWorkout(Context context, Workout workout) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        WorkoutDao dao = db.workoutDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Delete Workout
                dao.deleteWorkout(workout);
            }
        }).start();

        db.close();
    }

    // ---------------------------------------------------------------------------------------------------
    public void changeWorkoutOrder(Context context, Workout workout1, Workout workout2) {

        int change = workout1.sequence;
        workout1.sequence = workout2.sequence;
        workout2.sequence = change;

        updateWorkout(context, workout1);
        updateWorkout(context, workout2);
    }


    // ---------------------------------------------------------------------------------------------------
    public Workout converterWorkout(String name, String description, WorkoutType type, DayOfWeek dayOfWeek, int planId) {
        // Create New Workout DataBase
        Workout newWorkout = new Workout();
        newWorkout.plan_Id = planId;
        newWorkout.name = name;
        newWorkout.dayOfWeek = dayOfWeek;
        newWorkout.description = description;
        newWorkout.type = Objects.requireNonNullElse(type, WorkoutType.NA);
        newWorkout.lastModified = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"));

        return newWorkout;
    }


    // ---------------------------------------------------------------------------------------------------
    public WorkoutType applyWorkoutType(Workout workout, String item){
        switch(item)  {
            case "Chest":
                workout.type = WorkoutType.Chest;
                break;
            case "Back":
                workout.type = WorkoutType.Back;
                break;
            case "Shoulder":
                workout.type = WorkoutType.Shoulder;
                break;
            case "Arms":
                workout.type = WorkoutType.Arm;
                break;
            case "Biceps":
                workout.type = WorkoutType.Biceps;
                break;
            case "Triceps":
                workout.type = WorkoutType.Triceps;
                break;
            case "Legs":
                workout.type = WorkoutType.Leg;
                break;
        }

        return workout.type;
    }

}
