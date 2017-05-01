package com.e_swipe.e_swipe;

/**
 * Created by Anthonny on 01/05/2017.
 */
public class Profil {

    String userId;
    String name;
    String surname;
    String birthDay;

    Profil(String userId, String name, String surname, String birthDay){
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.birthDay = birthDay;
    }

    public String getUserId(){
        return userId;
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }

    public String getBirthday(){
        return birthDay;
    }
}
