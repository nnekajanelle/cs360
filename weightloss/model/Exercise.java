package com.app.weightloss.model;

public class Exercise {
    private int exerciseId;
    private String exerciseName;
    private String type;
    private String difficulty;
    private int duration;
    private int repetitions;
    private String description; // new
    private String imageUrl;
    private String dateAdded; // new

    public Exercise() {
    }

    public Exercise(String exerciseName, String type, String difficulty, int duration, int repetitions) {
        this.exerciseName = exerciseName;
        this.type = type;
        this.difficulty = difficulty;
        this.duration = duration;
        this.repetitions = repetitions;
    }

    // Getters and Setters
    public int getExerciseId() {
        return exerciseId;
    }
    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }
    public String getExerciseName() {
        return exerciseName;
    }
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getRepetitions() {
        return repetitions;
    }
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
