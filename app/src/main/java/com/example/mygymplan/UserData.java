package com.example.mygymplan;

import java.util.ArrayList;
import java.util.Arrays;

public class UserData {

    public Plan[] myPlans;
    public Workout[] myWorkouts;
    public Exercise[] myExercises;

    ArrayList<Plan> myPlans2;
    
    public Plan findMyActualPlan() {

        Plan actualPlan = myPlans2.get(1);

        //myPlans2.stream().filter(element -> element.active == true).forEach(actualPlan);
       // Arrays.stream(myPlans).findAny().stream().anyMatch(Plan::getActive);

        
        return actualPlan;
    }

}