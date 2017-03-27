package com.e_swipe.e_swipe;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    String mail;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialise components
        editMail = (EditText) findViewById(R.id.mail);
        editPassword = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.signin);
        mAuth = FirebaseAuth.getInstance();

        //If user is already connected, go to TabbedActivity else initialise Mail/FB authentification Listeners
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(),TabbedActivity.class);
            startActivity(intent);
        }
        else {
            //Initialise signIn with Email/Password
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mail = editMail.getText().toString();
                    password = editPassword.getText().toString();
                    //Initialise Mail/Password
                    handleEmailPassordConnexion();
                }
            });
            // Initialize Facebook Login button
            mCallbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("SUCCESS", "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d("CANCEL", "facebook:onCancel");
                    mAuth.signOut();
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d("ERROR", "facebook:onError", error);
                }
            });
        }
        //Initialize Firebase Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("AUTH", "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(LoginActivity.this,"Token " + user.getToken(false),Toast.LENGTH_LONG).show();
                } else {
                    // User is signed out
                    Log.d("AUTH", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    /**
     * On Activity Start add the Authentification Listener
     */
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
    /**
     * Method that handle the connexion with Email and Password
     */
    private void handleEmailPassordConnexion(){
        //Initialise Mail/Password
        mAuth.signInWithEmailAndPassword(mail, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("ON COMPLETE" , "signInWithEmail:onComplete:" + task.isSuccessful());
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w("!Successful", "signInWithEmail:failed", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentification  Failed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Authentification successful
                        Intent intent = new Intent(getApplicationContext(),TabbedActivity.class);
                        startActivity(intent);
                        }
                    }
                });
    }
    /**
     * Method that handle the connexion with Facebook Authentification
     */
    private void handleFacebookAccessToken(AccessToken token) {

        Log.d("HANDLE Facebook", "handleFacebookAccessToken:" + token);
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("ON COMPLETE", "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("ON COMPLETE", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(),TabbedActivity.class);
                            startActivity(intent);
                        }
                    }
                });
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
