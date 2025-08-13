package com.example.mygymplan;

import java.util.ArrayList;
import java.util.Arrays;

public class UserData {

    String name;
    public ArrayList<Plan> myPlans;
    public ArrayList<Workout> myWorkouts;
    public ArrayList<Exercise> myExercises;


    // ---- Constructor ----

    public UserData(String name, ArrayList<Plan> myPlans, ArrayList<Workout> myWorkouts, ArrayList<Exercise> myExercises) {
        this.name = name;
        this.myPlans = myPlans;
        this.myWorkouts = myWorkouts;
        this.myExercises = myExercises;
    }


    // ---- Getters and Setters ----

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Plan> getMyPlans() {
        return myPlans;
    }

    public void setMyPlans(ArrayList<Plan> myPlans) {
        this.myPlans = myPlans;
    }

    public ArrayList<Workout> getMyWorkouts() {
        return myWorkouts;
    }

    public void setMyWorkouts(ArrayList<Workout> myWorkouts) {
        this.myWorkouts = myWorkouts;
    }

    public ArrayList<Exercise> getMyExercises() {
        return myExercises;
    }

    public void setMyExercises(ArrayList<Exercise> myExercises) {
        this.myExercises = myExercises;
    }

}