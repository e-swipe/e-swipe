package com.e_swipe.e_swipe.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anthonny on 01/05/2017.
 */
public class Profil implements Parcelable {

    String userId;
    String name;
    String surname;
    String birthDay;
    ArrayList<String> profilesPicsUrls;

    public Profil(String userId, String name, String surname, String birthDay,ArrayList<String> profilesPicsUrls){
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.birthDay = birthDay;
        this.profilesPicsUrls = profilesPicsUrls;
    }

    public Profil(Parcel in){

        profilesPicsUrls = new ArrayList<>();
        String[] data = new String[4];
        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.userId = data[0];
        this.name = data[1];
        this.surname = data[2];
        this.birthDay = data[3];
        in.readStringList(profilesPicsUrls);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {userId,name,surname,birthDay});
        parcel.writeStringList(profilesPicsUrls);
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

    public ArrayList<String> getProfilesPicsUrls(){ return profilesPicsUrls;}


    public static final Creator<Profil> CREATOR = new Creator<Profil>() {
        @Override
        public Profil createFromParcel(Parcel in) {
            return new Profil(in);
        }

        @Override
        public Profil[] newArray(int size) {
            return new Profil[size];
        }
    };

}
