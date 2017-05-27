package com.e_swipe.e_swipe.model;

import java.util.List;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class UserPatch {

    String first_name;
    String last_name;
    String date_of_birth;
    String description;
    String gender;
    List<String> looking_for;
    int looking_for_age_min;
    int looking_for_age_max;
    boolean isVisible;

    public UserPatch(String first_name, String last_name, String date_of_birth, String description,
                     String gender, List<String> looking_for, int looking_for_age_min,
                     int looking_for_age_max, boolean isVisible) {

        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.description = description;
        this.gender = gender;
        this.looking_for = looking_for;
        this.looking_for_age_min = looking_for_age_min;
        this.looking_for_age_max = looking_for_age_max;
        this.isVisible = isVisible;
    }


    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getLooking_for() {
        return looking_for;
    }

    public int getLooking_for_age_min() {
        return looking_for_age_min;
    }

    public int getLooking_for_age_max() {
        return looking_for_age_max;
    }

    public boolean isVisible() {
        return isVisible;
    }
}
