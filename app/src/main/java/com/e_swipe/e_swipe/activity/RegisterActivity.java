package com.e_swipe.e_swipe.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.model.UserCreate;
import com.e_swipe.e_swipe.server.Profil.ProfilServer;
import com.e_swipe.e_swipe.utils.ResponseCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText name;
    EditText birthday;
    EditText surname;

    RadioButton radioButtonMale;
    RadioButton radioButtonFemale;
    Button register;


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
        radioButtonMale = (RadioButton) findViewById(R.id.radioMale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioFemale);

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
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                        birthday.setText(selectedmonth+"/"+selectedday+"/"+selectedyear);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();}
        });

        register = (Button) findViewById(R.id.accept_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender;
                if(radioButtonMale.isChecked()) gender = "male";
                else gender = "female";
                registerWithMailAndPassword(email.getText().toString(),password.getText().toString(),
                        name.getText().toString(),surname.getText().toString(),birthday.getText().toString(),gender);
            }
        });
    }
    public void registerWithMailAndPassword(String email, String password, String firstName, String lastName,String birthday, String gender) {
        UserCreate userCreate = new UserCreate(firstName,lastName,birthday,gender,email,password);
        Log.d("Register","register");
        ProfilServer.addProfil(userCreate, FirebaseInstanceId.getInstance().getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Register", String.valueOf(response.code()));
                if(ResponseCode.checkResponseCode(response.code())){
                    final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                            getString(R.string.user_file_key), Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedPref.edit();

                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String auth = jsonResponse.getString("auth");
                        editor.putString("auth",auth);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(),TabbedActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
