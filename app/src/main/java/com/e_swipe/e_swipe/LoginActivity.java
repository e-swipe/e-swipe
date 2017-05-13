package com.e_swipe.e_swipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.e_swipe.e_swipe.objects.Profil;
import com.e_swipe.e_swipe.services.gps.LocalisationListener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

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
     * Firebase Authentifier and Listener
     */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /*
     *Subviews
     */
    EditText editMail;
    EditText editPassword;
    Button signin;

    int age;
    Context context;

    private static final String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        //Initialise components
        editMail = (EditText) findViewById(R.id.mail);
        editPassword = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.signin);
        mAuth = FirebaseAuth.getInstance();

        /**
         * Handle Facebook sign in
         */
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile", "user_birthday,user_photos");

        /*if(AccessToken.getCurrentAccessToken() != null){
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            Profile profile = Profile.getCurrentProfile();
                            Intent intent = new Intent(context,TabbedActivity.class);
                            intent.putExtra("id", profile.getId());
                            intent.putExtra("name",profile.getFirstName());
                            intent.putExtra("surname",profile.getLastName());
                            intent.putExtra("birthday", object.optString("birthday"));
                            startActivity(intent);
                            finish();
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }*/

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

        final Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Authentification to firebase to get firebase user
     * @param token the facebook access token
     */
    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            requestFBUserInfos(token);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void requestFBUserInfos(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            final JSONObject birthdayObject = object;
                            final ArrayList<String> picturesUrl = new ArrayList<>();
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

                                                    //Facebook profile
                                                    Profile profile = Profile.getCurrentProfile();
                                                    //App profile
                                                    Profil profil = new Profil(profile.getId(),profile.getFirstName(),profile.getLastName(),birthdayObject.getString("birthday"),picturesUrl);
                                                    //Create new Intent
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

    private HashMap<String,String> getUsersInfoFromSwagger() {
        return null;
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
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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
