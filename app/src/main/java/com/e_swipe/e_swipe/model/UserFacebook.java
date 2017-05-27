package com.e_swipe.e_swipe.model;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class UserFacebook {

    private String first_name;
    private String last_name;
    private String date_of_birth;
    private String gender;
    private String email;

    public UserFacebook(String firstName, String lastName, String dateOfBirth, String gender, String email){
        this.first_name = firstName;
        this.last_name = lastName;
        this.date_of_birth = dateOfBirth;
        this.gender = gender;
        this.email = email;
    }
}
