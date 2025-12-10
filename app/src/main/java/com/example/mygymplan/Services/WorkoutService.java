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
import com.example.mygymplan.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
    // TEST
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
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                WorkoutDao dao = db.workoutDao();
                PlanDao planDao = db.planDao();

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
                workout.sequence = planWorkouts.size() + 1;

                // Set Workout to Editable
                workout.editable = true;

                // Set Duration Time
                ExerciseDao daoExercise = db.exerciseDao();
                List<Exercise> allExercises = daoExercise.listExercises();
                workout.duration = workoutDuration.CalculateDurationTime(workout, allExercises);

                // Insert in Database
                dao.insertWorkout(workout);

                Log.d("Teste Workout ID", "Workout Created - ID: " + workout.id);

                db.close();
            }
        }).start();
    }


    // ---------------------------------------------------------------------------------------------------
    public void updateWorkout(Context context, Workout workout) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                WorkoutDao workoutDao = db.workoutDao();

                // Set Duration Time
                ExerciseDao daoExercise = db.exerciseDao();
                List<Exercise> allExercises = daoExercise.listExercises();
                workout.duration = workoutDuration.CalculateDurationTime(workout, allExercises);

                // Then.. Update Workout
                workoutDao.updateWorkout(workout);

                db.close();
            }
        }).start();
    }


    // ---------------------------------------------------------------------------------------------------
    public void deleteWorkout(Context context, Workout workout) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                WorkoutDao dao = db.workoutDao();

                // Delete Workout
                dao.deleteWorkout(workout);

                // Update other Workouts order in Plan
                List<Workout> allWorkouts = dao.listWorkouts();
                for (Workout w : allWorkouts) {
                    if (w.plan_Id == workout.plan_Id) {
                        if (w.sequence >workout.sequence) {
                            w.sequence--;
                            dao.updateWorkout(w);
                        }
                    }
                }

                db.close();
            }
        }).start();
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


    // ---------------------------------------------------------------------------------------------------
    public void createVarient(Context context, Workout workout) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                WorkoutDao workoutDao = db.workoutDao();
                PlanDao planDao = db.planDao();

                Plan workoutPlan = new Plan();

                List<Plan> allPlans = planDao.listPlans();

                for (Plan item : allPlans) {
                    if (item.id == workout.plan_Id) {
                        workoutPlan = item;
                    }
                }
                // Set Duration Time
                ExerciseDao daoExercise = db.exerciseDao();
                List<Exercise> allExercises = daoExercise.listExercises();
                workout.duration = workoutDuration.CalculateDurationTime(workout, allExercises);

                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");

                // Get Plan ID
                int newPlanId = 0;

                // Get Higher id value
                for (int i = 0; i < allPlans.size(); i++) {
                    if (newPlanId < allPlans.get(i).id) {
                        newPlanId = allPlans.get(i).id;
                    }
                }

                newPlanId++;

                workoutPlan.id = newPlanId;
                workoutPlan.name = workoutPlan.name + " (Edited)";
                workoutPlan.author = username;
                workoutPlan.description = "Edited";
                workoutPlan.createdDate = LocalDate.now().toString();


                planDao.insertPlan(workoutPlan);

                // --------------------------------------------------------

                List<Workout> thisPlanWorkouts = new ArrayList<>();
                List<Workout> allWorkouts = workoutDao.listWorkouts();

                // Get next Workout ID
                newWorkoutId = 0;

                // Get Higher id value
                for (int i = 0; i < allWorkouts.size(); i++) {
                    if (newWorkoutId < allWorkouts.get(i).id) {
                        newWorkoutId = allWorkouts.get(i).id;
                    }
                }

                for (Workout item : allWorkouts) {
                    if (item.plan_Id == workout.plan_Id) {
                        thisPlanWorkouts.add(item);
                    }
                }

                allExercises = daoExercise.listExercises();
                List<Exercise> thisWorkoutExercises = new ArrayList<>();

                // Get Next Exercise ID
                int newExerciseId = 0;
                // Get Higher id value
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    newExerciseId = allExercises.getLast().id;
                }

                for (Workout thisPlanWorkout : thisPlanWorkouts) {

                    int oldId = thisPlanWorkout.id;

                    newWorkoutId = newWorkoutId + 1;
                    thisPlanWorkout.plan_Id = newPlanId;
                    thisPlanWorkout.id = newWorkoutId;
                    thisPlanWorkout.description = "";
                    workoutDao.insertWorkout(thisPlanWorkout);

                    if (!thisWorkoutExercises.isEmpty()) {
                        thisWorkoutExercises.clear();
                    }

                    for (Exercise e : allExercises) {
                        if (e.workout_Id == oldId) {
                            Log.d("Teste", "Try to Add ExerciseId = " + e.id);
                            thisWorkoutExercises.add(e);
                        }
                    }

                    for (Exercise newExercise : thisWorkoutExercises) {

                        newExerciseId++;

                        Log.d("Teste", "Edited Exercise plan_Id = " + newPlanId);
                        Log.d("Teste", "Edited Exercise workout_Id = " + newWorkoutId);
                        Log.d("Teste", "Edited Exercise Id = " + newExerciseId);

                        newExercise.id = newExerciseId;
                        newExercise.name = "Teste";
                        newExercise.description = "";
                        newExercise.plan_Id = newPlanId;
                        newExercise.workout_Id = newWorkoutId;
                        daoExercise.insertExercise(newExercise);

                    }

                }

                db.close();
            }
        }).start();
    }


}
