package com.e_swipe.e_swipe.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import com.e_swipe.e_swipe.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText name;
    EditText birthday;
    EditText surname;
    Switch gender;
    Button register;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        sharedPreferences = getApplicationContext().getSharedPreferences(
                getString(R.string.user_file_key), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        email = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        birthday = (EditText) findViewById(R.id.birthday);
        surname = (EditText) findViewById(R.id.surname);
        gender = (Switch) findViewById(R.id.switchSex);

        Calendar myCalendar = Calendar.getInstance();

        birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                final Calendar mcurrentDate=Calendar.getInstance();
                int mYear=mcurrentDate.get(Calendar.YEAR);
                int mMonth=mcurrentDate.get(Calendar.MONTH);
                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        try {
                            String myFormat = "MM/dd/yyyy"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                            Date date = sdf.parse(selectedmonth+"/"+selectedday+"/"+selectedyear);
                            sdf.format(new Date(selectedmonth,selectedday,selectedyear));
                            birthday.setText(date.toString());

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();}
        });

        register = (Button) findViewById(R.id.accept_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerWithMailAndPassword(email.getText().toString(),password.getText().toString());
                //createFirebaseUser(email.getText().toString(),password.getText().toString());
            }
        });
    }
    public void registerWithMailAndPassword(String email, String password) {

    }
}
