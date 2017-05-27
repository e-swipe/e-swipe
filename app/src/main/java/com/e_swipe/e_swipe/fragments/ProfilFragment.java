package com.e_swipe.e_swipe.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e_swipe.e_swipe.activity.EditProfileActivity;
import com.e_swipe.e_swipe.activity.LoginActivity;
import com.e_swipe.e_swipe.activity.UserProfileActivity;
import com.e_swipe.e_swipe.model.Profil;
import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.model.UserPatch;
import com.e_swipe.e_swipe.server.Profil.ProfilServer;
import com.e_swipe.e_swipe.utils.DateUtils;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.e_swipe.e_swipe.server.Profil.ProfilServer.patch;


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
     * UserProfil
     */
    Profil profil;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profil, container, false);
        /**
         * SharedPreferences
         */
        final SharedPreferences sharedPref = mContext.getSharedPreferences(
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

            }
        });

        switchFemme = (Switch) v.findViewById(R.id.switch_femme);
        switchFemme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        seekbarDistance = (SeekBar) v.findViewById(R.id.seekBar_distance);
        seekbarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("distance",seekBar.getProgress());
                if(profil != null) {
                    List<String> lookingFor = new ArrayList<String>();
                    if(switchFemme.isChecked()) lookingFor.add("female");
                    if(switchHomme.isChecked()) lookingFor.add("male");
                    try {
                        patch(sharedPref.getString("auth", ""), new UserPatch(profil.getFirst_name(), profil.getLast_name(),
                                profil.getDate_of_birth(), profil.getDescription(), profil.getGender(), lookingFor,
                                rangeSeekBar.getSelectedMinValue(), rangeSeekBar.getSelectedMaxValue(), true), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        seekbarDistance.setMax(1);
        seekbarDistance.setMax(100);

        //Init RangeSeekBar
        rangeSeekBar = (RangeSeekBar) v.findViewById(R.id.rangeSeekBar_age);
        rangeSeekBar.setRangeValues(18,100);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {

            }
        });

        //Init CircleImageView
        circleImageView = (de.hdodenhof.circleimageview.CircleImageView) v.findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });
        nameAndImage = (TextView) v.findViewById(R.id.textView_user);

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
            }
        });

        //Get the new profil user
        try {
            getProfil(sharedPref.getString("auth",""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
    }

    public void getProfil(String auth) throws IOException {
        ProfilServer.getProfil(auth, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                profil = new Gson().fromJson(response.body().string(), Profil.class);
                initSubViewsWithProfilAndPreferences(profil);
            }
        });
    }

    public void initSubViewsWithProfilAndPreferences(Profil profil){

        nameAndImage.setText(profil.getFirst_name()+","+DateUtils.getAge(profil.getDate_of_birth()));
        Glide.with(mContext).load(profil.getPicture_url()).into(circleImageView);
        if(profil.getLooking_for().contains("male")){
            switchHomme.setChecked(true);
        }
        else switchHomme.setChecked(false);
        if (profil.getLooking_for().contains("female")){
            switchFemme.setChecked(true);
        }
        else switchFemme.setChecked(false);

        /**
         * SharedPreferences
         */
        final SharedPreferences sharedPref = mContext.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        seekbarDistance.setProgress(sharedPref.getInt("distance",1));

        rangeSeekBar.setSelectedMinValue(profil.getLooking_for_age_min());
        rangeSeekBar.setSelectedMaxValue(profil.getLooking_for_age_max());
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
}
