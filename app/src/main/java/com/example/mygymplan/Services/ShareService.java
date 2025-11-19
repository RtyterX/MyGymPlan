package com.example.mygymplan.Services;

import android.content.Context;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShareService extends AppCompatActivity {

    String finalString;

    public void CreatePlanFromString(Context context, String longString) {

        // Split String in two parts ( Plan and Workouts)
        String[] splittedString = longString.split("->");

        // Needed Variables
        LocalDate date = LocalDate.now();
        int totalWorkouts;
        int totalPlans;

        // ---------------------------------------
        // Get Plans and Workout Ids ( Total of Both )

        // Our MyPrefs
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
                PlanDao planDao = db.planDao();
                WorkoutDao workoutDao = db.workoutDao();

                List<Plan> allPlans = planDao.listPlans();
                List<Workout> allWorkouts = workoutDao.listWorkouts();

                int totalPlans = allPlans.size();
                int totalWorkouts = allWorkouts.size();

                // ---------------------------------------
                // Create Plan
                String[] planParts = splittedString[0].split(",");

                Plan newPlan = new Plan();
                newPlan.id = totalPlans;
                newPlan.name = planParts[0];
                newPlan.description = planParts[1];
                newPlan.active = Boolean.valueOf(planParts[2]);
                newPlan.author = planParts[3];
                newPlan.createdDate = planParts[4];
                newPlan.pro = Boolean.valueOf(planParts[5]);

                PlanService planService = new PlanService();
                planService.insertPlan(context, newPlan);

                // ---------------------------------------
                // Create Workouts Individually
                String workoutsString;
                workoutsString = Arrays.toString(splittedString[1].split("->"));

                String[] workoutsList = workoutsString.split(";");

                // Split one by one
                for (int i = 0; i < workoutsList.length; i++) {

                    totalWorkouts++;
                    String[] parts = workoutsList[i].split(",");

                    Workout newWorkout = new Workout();
                    newWorkout.id = totalWorkouts;
                    newWorkout.name = parts[0];
                    newWorkout.description = parts[1];
                    // newWorkout.image = parts[1];
                    newWorkout.type = WorkoutType.valueOf(parts[2]);
                    newWorkout.sequence = Integer.parseInt(parts[3]);
                    newWorkout.lastModified = LocalDate.now().toString();
                    newWorkout.plan_Id = newPlan.id;

                    WorkoutService workoutService = new WorkoutService();
                    workoutService.insertWorkout(context, newWorkout);
                }

                // ---------------------------------------
                // Create Exercises Individually
                String exerciseString;
                exerciseString = Arrays.toString(splittedString[2].split("->"));

                String[] exercisesList = exerciseString.split(";");

                // Split one by one
                for (int i = 0; i < exercisesList.length; i++) {

                    String[] parts = exercisesList[i].split(",");

                    Exercise newExercise = new Exercise();
                    newExercise.name = parts[0];
                    newExercise.description = parts[1];
                    // newExercise.image = parts[2];
                    newExercise.sets = Integer.parseInt(parts[3]);
                    newExercise.reps = Integer.parseInt(parts[4]);
                    newExercise.rest = Integer.parseInt(parts[5]);
                    newExercise.load = Integer.parseInt(parts[6]);
                    newExercise.type = WorkoutType.valueOf(parts[7]);
                    newExercise.sequence = Integer.parseInt(parts[8]);
                    newExercise.lastModified = LocalDate.now().toString();
                    newExercise.plan_Id = newPlan.id;
                    newExercise.workout_Id = Integer.parseInt(parts[11]);
                    newExercise.savedExercise_Id = Integer.parseInt(parts[12]);

                    ExerciseService exerciseService = new ExerciseService();
                    exerciseService.insertExercise(context, newExercise);
                }
            }
        }).start();
    }


    public String ConvertPlanToString(Context context, Plan plan) {

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
        ExerciseDao exerciseDao = db.exerciseDao();
        WorkoutDao workoutDao = db.workoutDao();

        // Get all Workouts
        List<Workout> workoutList = new ArrayList<>();
        List<Workout> allWorkouts;
        WorkoutService workoutService = new WorkoutService();
        allWorkouts = workoutService.listAllWorkout(context);

                // Strings
                StringBuilder workoutString = new StringBuilder();
                String exerciseString = "";

                // --------------------------------------------------------
                // Workouts



                // Get all Workout with this plan ID
                for (Workout item : allWorkouts) {
                    if (item.plan_Id == plan.id) {
                        workoutList.add(item);
                    }
                }

                // String bloc for Workouts
                for (Workout w : workoutList) {
                    workoutString.insert(0, w.name + ","
                            + w.description + ","
                            // + w.wImage + ","
                            + w.type + ","
                            + w.sequence + ","
                            + LocalDate.now().toString() + ","
                            // Foreign Key doesn't Matter
                            + ";");

                    // --------------------------
                    // Format Exercise List
                    List<Exercise> exerciseList = new ArrayList<>();
                    List<Exercise> allExercises = new ArrayList<>();
                    allExercises = exerciseDao.listExercise();

                    for (Exercise item : allExercises) {
                        if (item.workout_Id == w.id) {
                            if (item.plan_Id == plan.id) {
                                exerciseList.add(item);
                            }
                        }
                    }

                    for (int i = 0; i < exerciseList.size(); i++) {

                        exerciseString = exerciseList.get(i).name + ","
                                + exerciseList.get(i).description + ","
                                // + exerciseList.get(i).image + ","
                                + exerciseList.get(i).sets + ","
                                + exerciseList.get(i).reps + ","
                                + exerciseList.get(i).rest + ","
                                + exerciseList.get(i).load + ","
                                + exerciseList.get(i).type + ","
                                + exerciseList.get(i).sequence + ","
                                + LocalDate.now().toString() + ","
                                // Foreign Key doesn't Matter
                                + ";" + exerciseString;
                    }
                }

                // --------------------------
                // Final String
                finalString = plan.name + ","
                        + plan.description + ","
                        //  + plan.image + ","
                        + plan.active + ","
                        + plan.author + ","
                        + plan.createdDate + ","
                        + plan.pro + ","
                        + "->"
                        + workoutString
                        + "->"
                        + exerciseString;


        return finalString;
    }
}
