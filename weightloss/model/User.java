package com.app.weightloss.model;

import java.io.Serializable;

public class User implements Serializable {
    private int userId;
    private String username;
    private String email;
    private String password;
    private int age;
    private float weight;
    private float height;
    private String goals;
    private String dietaryRestrictions;

    // Newly added fields
    private String gender;
    private String allergies;
    private String chronic;

    public User() {}

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }

    public String getGoals() {
        return goals;
    }
    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }
    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAllergies() {
        return allergies;
    }
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getChronic() {
        return chronic;
    }
    public void setChronic(String chronic) {
        this.chronic = chronic;
    }
}
