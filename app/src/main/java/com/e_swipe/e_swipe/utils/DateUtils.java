package com.e_swipe.e_swipe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class DateUtils {

    public static int getAge(String date) {

        int age = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date date1 = format.parse(date);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            int month1 = now.get(Calendar.MONTH);
            int month2 = dob.get(Calendar.MONTH);
            if (month2 > month1) {
                age--;
            } else if (month1 == month2) {
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                int day2 = dob.get(Calendar.DAY_OF_MONTH);
                if (day2 > day1) {
                    age--;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age;
    }
}
