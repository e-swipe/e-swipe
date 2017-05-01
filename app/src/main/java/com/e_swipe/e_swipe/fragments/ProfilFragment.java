package com.e_swipe.e_swipe.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.e_swipe.e_swipe.Profil;
import com.e_swipe.e_swipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    Switch switchHomme;
    Switch switchFemme;
    SeekBar seekbarDistance;
    RangeSeekBar <Integer> rangeSeekBar;
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
        /**
         * Init Shared Preferences
         */
        Context context = getActivity();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        /**
         * Init subviews
         */
        switchHomme = (Switch) v.findViewById(R.id.switch_homme);
        switchHomme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Save switch value in shared Preferences
                editor.putBoolean(getString(R.string.isManWanted), b);
                editor.apply();
                compoundButton.clearFocus();
            }
        });

        switchFemme = (Switch) v.findViewById(R.id.switch_femme);
        switchFemme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean(getString(R.string.isWomenWanted), b);
                editor.apply();
                compoundButton.clearFocus();
            }
        });

        seekbarDistance = (SeekBar) v.findViewById(R.id.seekBar_distance);
        seekbarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                editor.putInt(getString(R.string.distance), i);
                editor.apply();
                seekbarDistance.clearFocus();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rangeSeekBar = (RangeSeekBar) v.findViewById(R.id.rangeSeekBar_age);
        rangeSeekBar.setRangeValues(0,100);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                editor.putInt(getString(R.string.ageMin), minValue);
                editor.putInt(getString(R.string.ageMax),maxValue);
                editor.apply();
                rangeSeekBar.clearFocus();
                rangeSeekBar.clearAnimation();
            }
        });

        circleImageView = (de.hdodenhof.circleimageview.CircleImageView) v.findViewById(R.id.profile_image);
        nameAndImage = (TextView) v.findViewById(R.id.textView_user);
        int age = 0;
        SimpleDateFormat format = new SimpleDateFormat("MM/DD/YYYY");
        age = getAge(profil.getBirthday());
        nameAndImage.setText(profil.getName()+", "+ age);

        Bitmap bitmap = getFacebookProfilePicture(profil.getUserId());
        initSubviewsFromSharedPreferences();

        return v;
    }

    public void initSubviewsFromSharedPreferences(){
        final SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        switchHomme.setChecked(sharedPref.getBoolean(getString(R.string.isManWanted),false));
        switchFemme.setChecked(sharedPref.getBoolean(getString(R.string.isWomenWanted),false));
        seekbarDistance.setProgress(sharedPref.getInt(getString(R.string.distance),0));
        rangeSeekBar.setRangeValues(sharedPref.getInt(getString(R.string.ageMin),0), sharedPref.getInt(getString(R.string.ageMax),100));

        /*SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.string.);
        long highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);*/
    }

    public static int getAge(String date) {

        int age = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/DD/YYYY");
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
