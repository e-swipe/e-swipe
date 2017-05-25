package com.e_swipe.e_swipe.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.e_swipe.e_swipe.EditProfileActivity;
import com.e_swipe.e_swipe.LoginActivity;
import com.e_swipe.e_swipe.UserProfileActivity;
import com.e_swipe.e_swipe.objects.Profil;
import com.e_swipe.e_swipe.R;
import com.facebook.login.LoginManager;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Class related to the Profil fragment
 */
public class ProfilFragment extends Fragment {
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
    Button signOutButton;
    ImageButton editProfilButton;
    /**
     * Listener to event over the fragment
     */
    private OnFragmentInteractionListener mListener;
    private FragmentListenerCallback fragmentListenerCallback;
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
        editProfilButton = (ImageButton) v.findViewById(R.id.edit_profile_btn);
        editProfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),EditProfileActivity.class);
                startActivity(intent);
            }
        });
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

        //Init RangeSeekBar
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

        //Init CircleImageView
        circleImageView = (de.hdodenhof.circleimageview.CircleImageView) v.findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("profil",profil);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        nameAndImage = (TextView) v.findViewById(R.id.textView_user);
        int age = 0;
        age = getAge(profil.getBirthday());
        nameAndImage.setText(profil.getName()+", "+ age);

        Bitmap bitmap = getFacebookProfilePicture(profil.getUserId());
        initSubviewsFromSharedPreferences();

        signOutButton = (Button) v.findViewById(R.id.sign_out);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fragmentListenerCallback.askForFinish();
                editor.putString("auth","");
                LoginManager.getInstance().logOut();
                getActivity().finish();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                Log.d("Debug","finish");
            }
        });

        return v;
    }

    public void initSubviewsFromSharedPreferences(){
        final SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        switchHomme.setChecked(sharedPref.getBoolean(getString(R.string.isManWanted),false));
        switchFemme.setChecked(sharedPref.getBoolean(getString(R.string.isWomenWanted),false));
        seekbarDistance.setProgress(sharedPref.getInt(getString(R.string.distance),0));
        rangeSeekBar.setRangeValues(sharedPref.getInt(getString(R.string.ageMin),0), sharedPref.getInt(getString(R.string.ageMax),100));

    }

    public static int getAge(String date) {

        int age = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
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

    public interface FragmentListenerCallback {
        void askForFinish();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentListenerCallback = (FragmentListenerCallback) activity;
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
