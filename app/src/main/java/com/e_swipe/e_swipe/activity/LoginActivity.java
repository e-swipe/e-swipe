package com.e_swipe.e_swipe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.server.login.LoginServer;
import com.e_swipe.e_swipe.model.UserFacebook;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Activity related to every SignIn options (Email / FB)
 */
public class LoginActivity extends Activity {

    /**
     * Callback Manager that will handle result from facebook login
     */
    CallbackManager mCallbackManager;
    /*
     *Subviews
     */
    EditText editMail;
    EditText editPassword;
    Button signin;
    Button register;

    /**
     * Current context
     */
    Context context;

    /**
     * SharedPreferences
     */
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    /**
     * List of pictures url of urer
     */
    ArrayList<String> picturesUrl;
    JSONObject birthdayObject;
    int nbAlbums;
    int cpt;
    String auth;

    private static final String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        picturesUrl = new ArrayList<>();

        //Get context
        context = getApplicationContext();

        //Init sharedPreferences
        sharedPref = context.getSharedPreferences(
                getString(R.string.user_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //Initialise components
        editMail = (EditText) findViewById(R.id.mail);
        editPassword = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.signin);

        /*
         * Handle Facebook sign in
         */
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile", "user_birthday,user_photos");

        if(!sharedPref.getString("auth","").equals("")){
            //Get user infos from shared preferences
            Intent intent = new Intent(context,TabbedActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            //If user isn't connected init Facebook connexion and Email/Password Connexion
            //Init Register
            register = (Button) findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                    startActivity(intent);
                }
            });

            //Init Email/Password
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!editMail.getText().toString().equals("") &&
                            !editPassword.getText().toString().equals("")) {
                        try {
                            signinWithEmailAndPassword(editMail.getText().toString().trim(), editPassword.getText().toString().trim());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            //Init facebook signIn
            Log.d("Debug","user not set");
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                    // App code
                }
                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                }
                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                }
            });
        }
    }
    // TODO: 15/05/2017
    //Request Swagger for info and profil creation
    private void signinWithEmailAndPassword(String email, String password) throws JSONException {
        Log.d(TAG,"Email : " + email + " password : " + password);
        try {
            LoginServer.withMailAndPassword(email,password,FirebaseInstanceId.getInstance().getToken(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject mainObject = new JSONObject(response.body().string());
                        auth = mainObject.getString("auth");
                        editor.putString("auth",auth);
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(),TabbedActivity.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Authentification to firebase to get firebase user
     * @param token the facebook access token
     */
    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.d("TOKEN",token.getToken());
        requestFBUserInfos(token);

    }

    public void requestFBUserInfos(final AccessToken accessToken){

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            final String email = response.getJSONObject().getString("email");
                            final String gender = response.getJSONObject().getString("gender");
                            birthdayObject = object;
                            for(int i=0;i<object.getJSONObject("albums").getJSONArray("data").length();i++){
                                JSONObject album = object.getJSONObject("albums").getJSONArray("data").getJSONObject(i);
                                Log.d("Album",album.toString());
                                String albumId = album.getString("id");
                                String nameAlbum = album.getString("name");
                                //For each album request photos url
                                Bundle params = new Bundle();
                                params.putBoolean("redirect", false);
                                if(nameAlbum.equals("Profile Pictures")){
                                    new GraphRequest(
                                            AccessToken.getCurrentAccessToken(),
                                            "/" + albumId +"/picture",
                                            params,
                                            HttpMethod.GET,
                                            new GraphRequest.Callback() {
                                                public void onCompleted(GraphResponse response) {
                                                    JSONObject picture = response.getJSONObject();
                                                    try {
                                                        Log.d("DebugFacebook",picture.getJSONObject("data").toString());
                                                        picturesUrl.add(picture.getJSONObject("data").getString("url"));
                                                        Log.d("DebugFacebook",picture.getJSONObject("data").getString("url"));
                                                            //Facebook profile
                                                            final Profile profile = Profile.getCurrentProfile();
                                                            Log.d("Debug", String.valueOf(cpt));
                                                            try {
                                                                UserFacebook userFacebook = new UserFacebook(profile.getFirstName(),profile.getLastName(),birthdayObject.getString("birthday"),gender,email);
                                                                LoginServer.withFacebook(accessToken.getToken(), FirebaseInstanceId.getInstance().getToken(),
                                                                        profile.getId(), userFacebook, new Callback() {
                                                                            @Override
                                                                            public void onFailure(Call call, IOException e) {

                                                                            }
                                                                            @Override
                                                                            public void onResponse(Call call, Response response) throws IOException {
                                                                                JSONObject mainObject = null;
                                                                                Log.d("Response", String.valueOf(response.code()));
                                                                                try {
                                                                                    String body = response.body().string();
                                                                                    Log.d("Login",body);
                                                                                    mainObject = new JSONObject(body);
                                                                                    auth = mainObject.getString("auth");
                                                                                    switch (response.code()){
                                                                                        case 200:
                                                                                            //User Connected
                                                                                            editor.putString("auth",auth);
                                                                                            editor.commit();
                                                                                            if(auth != null){
                                                                                                //Go to next Activity
                                                                                                Intent intent = new Intent(context,TabbedActivity.class);
                                                                                                startActivity(intent);
                                                                                                finish();
                                                                                            }
                                                                                            break;
                                                                                        case 201:
                                                                                            //User Created
                                                                                            Bitmap bitmap = LoginServer.getFacebookProfilePicture(profile.getId());
                                                                                            editor.putString("auth",auth);
                                                                                            editor.commit();
                                                                                            if(auth != null){
                                                                                                //Go to next Activity
                                                                                                Intent intent = new Intent(context,TabbedActivity.class);
                                                                                                startActivity(intent);
                                                                                                finish();
                                                                                            }
                                                                                            break;
                                                                                        default:
                                                                                            //Default
                                                                                            break;
                                                                                    }
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    cpt++;
                                                }
                                            }
                                    ).executeAsync();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday,albums");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    /**
     * On Activity Start add the Authentification Listener
     */
    public void onStart() {
        super.onStart();
    }
    @Override
    /**
     * On Activity Stop remove the Authentification Listener
     */
    public void onStop() {
        super.onStop();
    }

    /*
     * onActivityResult that call the callbackManager with the result of the authentification
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
