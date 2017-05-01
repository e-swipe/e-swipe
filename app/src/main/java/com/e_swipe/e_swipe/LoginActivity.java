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

import com.e_swipe.e_swipe.services.gps.LocalisationListener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

import org.json.JSONException;
import org.json.JSONObject;

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

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile", "user_birthday");

        if(AccessToken.getCurrentAccessToken() != null){
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
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                //handleFacebookAccessToken(loginResult.getAccessToken());
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                // Application code
                                try {
                                    Profile profile = Profile.getCurrentProfile();
                                    Intent intent = new Intent(context,TabbedActivity.class);
                                    intent.putExtra("id", profile.getId());
                                    intent.putExtra("name",profile.getFirstName());
                                    intent.putExtra("surname",profile.getLastName());
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                    intent.putExtra("birthday", birthday);
                                    Toast.makeText(context,"birthday : " + birthday, Toast.LENGTH_LONG);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
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
