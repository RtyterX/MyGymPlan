package com.example.mygymplan;

import java.io.Serializable;
import java.util.ArrayList;

public class Workout implements Serializable {

    public int id;
    public String wName;
    public int wImage;
    public String wDescription;
    public WorkoutType[] types;
    public ArrayList<Exercise> wExercises;
    public Boolean active;


    // ---- Constructor ----

    public Workout(int id, String name, ArrayList<Exercise> wExercises) {
        this.id = id;
        wName = name;
        this.wExercises = wExercises;
    }


    // ---- Getters and Setters ----


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ArrayList<Exercise> getwExercises() {
        return wExercises;
    }

    public void setwExercises(ArrayList<Exercise> wExercises) {
        this.wExercises = wExercises;
    }

    public WorkoutType[] getTypes() {
        return types;
    }

    public void setTypes(WorkoutType[] types) {
        this.types = types;
    }

    public String getwDescription() {
        return wDescription;
    }

    public void setwDescription(String wDescription) {
        this.wDescription = wDescription;
    }

    public int getwImage() {
        return wImage;
    }

    public void setwImage(int wImage) {
        this.wImage = wImage;
    }

    public String getwName() {
        return wName;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }
}
