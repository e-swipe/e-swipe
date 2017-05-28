package com.e_swipe.e_swipe.model;

import java.util.List;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class UserPatch {
    //TODO : Annotation
    String first_name;
    String last_name;
    String date_of_birth;
    String description;
    String gender;
    List<String> looking_for;
    int looking_for_age_min;
    int looking_for_age_max;
    boolean is_visible;

    public UserPatch(){
        this.first_name = null;
        this.last_name = null;
        this.date_of_birth = null;
        this.last_name = null;
        this.description = null;
        this.looking_for = null;
        this.looking_for_age_max = 0;
        this.looking_for_age_min = 0;
        this.is_visible = true;
    }

    public UserPatch(String first_name, String last_name, String date_of_birth, String description,
                     String gender, List<String> looking_for, int looking_for_age_min,
                     int looking_for_age_max, boolean is_visible) {

        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.description = description;
        this.gender = gender;
        this.looking_for = looking_for;
        this.looking_for_age_min = looking_for_age_min;
        this.looking_for_age_max = looking_for_age_max;

    }


    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLooking_for(List<String> looking_for) {
        this.looking_for = looking_for;
    }

    public void setLooking_for_age_min(int looking_for_age_min) {
        this.looking_for_age_min = looking_for_age_min;
    }

    public void setLooking_for_age_max(int looking_for_age_max) {
        this.looking_for_age_max = looking_for_age_max;
    }

    public void setIs_visible(boolean is_visible) {
        this.is_visible = is_visible;
    }

}
