package com.example.mygymplan;

public class Workout {

    public String wName;
    public int wImage;
    public String wDescription;
    public WorkoutType[] types;
    public Exercise[] wExercises;
    public Boolean active;

    public String getwName() {
        return wName;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Exercise[] getwExercises() {
        return wExercises;
    }

    public void setwExercises(Exercise[] wExercises) {
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

    public Workout(String wName, int wImage, String wDescription, WorkoutType[] types, Exercise[] wExercises, Boolean active) {
        this.wName = wName;
        this.wImage = wImage;
        this.wDescription = wDescription;
        this.types = types;
        this.wExercises = wExercises;
        this.active = active;
    }
}
