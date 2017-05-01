package com.e_swipe.e_swipe.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.e_swipe.e_swipe.Profil;
import com.e_swipe.e_swipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URL;

//import com.facebook.AccessToken;
//import com.facebook.FacebookSdk;
//import com.facebook.login.LoginManager;

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
    TextView nameAndImage;
    de.hdodenhof.circleimageview.CircleImageView circleImageView;
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
     * The profil of the user
     */
    private static Profil profil;

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
    public static ProfilFragment newInstance(Context context, Profil profile) {
        ProfilFragment fragment = new ProfilFragment();
        mContext = context;
        profil = profile;
        return fragment;
    }

    @Override
    /**
     * onCreate Method that will listen to firebase user sign in and ask for updates on subviews
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profil, container, false);

        circleImageView = (de.hdodenhof.circleimageview.CircleImageView) v.findViewById(R.id.profile_image);
        nameAndImage = (TextView) v.findViewById(R.id.textView_user);
        nameAndImage.setText(profil.getName() + "," + profil.getBirthday());

        Bitmap bitmap = getFacebookProfilePicture(profil.getUserId());

        return v;
    }

    public Bitmap getFacebookProfilePicture(String userID){
        FacebookGraphConnector facebookGraphConnector = new FacebookGraphConnector();
        String url = "https://graph.facebook.com/" + userID + "/picture?type=large";
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            facebookGraphConnector.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class FacebookGraphConnector extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String[] strings) {
            URL imageURL = null;
            Bitmap bitmap = null;
            try {
                imageURL = new URL(strings[0]);
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            circleImageView.setImageBitmap(bitmap);
        }
    }
}
