package com.e_swipe.e_swipe.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.e_swipe.e_swipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Class related to the Profil fragment
 */
public class ProfilFragment extends Fragment {
    // TODO: 27/03/2017
    /**
     * Application Context
     */
    private static Context mContext;

    /**
     * Subviews
     */
    Button signOut;
    TextView tx_email;
    /**
     * Firebase listener and authentifier
     */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /**
     * Listener to event over the fragment
     */
    private OnFragmentInteractionListener mListener;
    /**
     * String related to the mail adress of the user
     */
    String email;

    /**
     * Empty constructor
     */
    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param context Application context
     * @return a new instance of the ProfilFragment
     */
    public static ProfilFragment newInstance(Context context) {
        ProfilFragment fragment = new ProfilFragment();
        mContext = context;
        return fragment;
    }

    @Override
    /**
     * onCreate Method that will listen to firebase user sign in and ask for updates on subviews
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get user Info from firebase
        //Asynchrone and called after onCreateView so need to update userInfo when we listener is fired
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d("onCreate", "onAuthStateChange");
                if (user != null) {
                    // User is signed in
                    updateUserInfo(user);
                } else {
                    // User is signed out
                    Log.d("AUTH", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    /**
     * Update the subviews relative to user infos
     * @param user Firebase user currently connected
     */
    private void updateUserInfo(FirebaseUser user) {
        tx_email.setText(user.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profil, container, false);
        signOut = (Button) v.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                if (mAuthListener != null) {
                    mAuth.removeAuthStateListener(mAuthListener);
                }
            }
        });
        //Initialise subviews
        tx_email = (TextView) v.findViewById(R.id.email);

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
