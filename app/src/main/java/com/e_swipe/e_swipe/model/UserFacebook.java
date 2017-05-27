package com.e_swipe.e_swipe.model;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class UserFacebook {

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String email;

    public UserFacebook(String firstName, String lastName, String dateOfBirth, String gender, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
    }
}
