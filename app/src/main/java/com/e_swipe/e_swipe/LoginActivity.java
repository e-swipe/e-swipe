package com.e_swipe.e_swipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.e_swipe.e_swipe.login.FacebookLoginTask;
import com.e_swipe.e_swipe.login.MailPasswordLoginTask;
import com.e_swipe.e_swipe.objects.Profil;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    private static final String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("TOKEN",FirebaseInstanceId.getInstance().getToken());

        picturesUrl = new ArrayList<String>();

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

        /**
         * Handle Facebook sign in
         */
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile", "user_birthday,user_photos");


        if(!sharedPref.getString("auth","").equals("")){
            //Get user infos from shared preferences
            Log.d("Debug","user set");
            Gson gson = new Gson();
            String json = sharedPref.getString("userProfil","");
            Profil profil = gson.fromJson(json, Profil.class);

            //Start intent with profile
            Bundle bundle = new Bundle();
            bundle.putParcelable("profil",profil);
            Intent intent = new Intent(context,TabbedActivity.class);
            intent.putExtras(bundle);
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
                        signinWithEmailAndPassword(editMail.getText().toString().trim(), editPassword.getText().toString().trim());
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
    private void signinWithEmailAndPassword(String email, String password) {
        Log.d(TAG,"Email : " + email + " password : " + password);
        MailPasswordLoginTask mailPasswordLoginTask = new MailPasswordLoginTask(email, password , FirebaseInstanceId.getInstance().getToken(), new MailPasswordLoginTask.AsyncResponse() {
            @Override
            public void processFinish(String token) {
                //Request for user infos
            }

            @Override
            public void processError() {

            }
        });
        mailPasswordLoginTask.execute();
    }

    /**
     * Authentification to firebase to get firebase user
     * @param token the facebook access token
     */
    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        FacebookLoginTask facebookLoginTask = new FacebookLoginTask(token.getToken(),FirebaseInstanceId.getInstance().getToken(), new FacebookLoginTask.AsyncResponse() {
            @Override
            public void processFinish(String authToken) {
                requestFBUserInfos(token);
            }

            @Override
            public void processError() {
                //Show Dialog to retry
            }
        });
        facebookLoginTask.execute();

    }

    public void requestFBUserInfos(AccessToken accessToken){

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            birthdayObject = object;
                            nbAlbums = object.getJSONObject("albums").getJSONArray("data").length()-1;
                            for(int i=0;i<object.getJSONObject("albums").getJSONArray("data").length();i++){
                                JSONObject album = object.getJSONObject("albums").getJSONArray("data").getJSONObject(i);
                                String albumId = album.getString("id");
                                //For each album request photos url
                                Bundle params = new Bundle();
                                params.putBoolean("redirect", false);
                                new GraphRequest(
                                        AccessToken.getCurrentAccessToken(),
                                        "/" + albumId +"/picture",
                                        params,
                                        HttpMethod.GET,
                                        new GraphRequest.Callback() {
                                            public void onCompleted(GraphResponse response) {
                                                JSONObject picture = response.getJSONObject();
                                                try {
                                                    picturesUrl.add(picture.getJSONObject("data").getString("url"));
                                                    Log.d("Debug",picture.getJSONObject("data").getString("url"));
                                                    if(cpt == nbAlbums){
                                                        //Facebook profile
                                                        Profile profile = Profile.getCurrentProfile();
                                                        //App profile
                                                        Profil profil = null;
                                                        try {
                                                            profil = new Profil(profile.getId(),profile.getFirstName(),profile.getLastName(), birthdayObject.getString("birthday"),picturesUrl);
                                                            //Save Profil to sharedPreferences
                                                            Gson gson = new Gson();
                                                            String json = gson.toJson(profil);
                                                            editor.putString("userProfil", json);
                                                            editor.commit();
                                                            //Create new Intent with new profil
                                                            Bundle bundle = new Bundle();
                                                            bundle.putParcelable("profil",profil);
                                                            Intent intent = new Intent(context,TabbedActivity.class);
                                                            intent.putExtras(bundle);
                                                            startActivity(intent);
                                                            finish();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                cpt++;
                                            }
                                        }
                                ).executeAsync();
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
