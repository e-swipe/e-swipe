package com.e_swipe.e_swipe.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e_swipe.e_swipe.activity.EditProfileActivity;
import com.e_swipe.e_swipe.activity.LoginActivity;
import com.e_swipe.e_swipe.activity.TabbedActivity;
import com.e_swipe.e_swipe.activity.UserProfileActivity;
import com.e_swipe.e_swipe.model.Profil;
import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.model.UserPatch;
import com.e_swipe.e_swipe.server.Profil.ProfilServer;
import com.e_swipe.e_swipe.utils.DateUtils;
import com.e_swipe.e_swipe.utils.ResponseCode;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.e_swipe.e_swipe.server.Profil.ProfilServer.patch;


/**
 * Class related to the Profil fragment
 */
public class ProfilFragment extends Fragment{
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
    TextView distance;
    TextView age;
    /**
     * Anims
     */
    Animation animfadeOutImage;
    Animation animfadeInImage;
    Animation animfadeInText;

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
                getString(R.string.user_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        /**
         * Init subviews
         */
        animfadeOutImage = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        animfadeInImage = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_image);
        animfadeInText = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_text_view);

        age = (TextView) v.findViewById(R.id.textView_age);
        distance = (TextView) v.findViewById(R.id.test);
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
                List<String> lookingFor = new ArrayList<String>();
                if(switchFemme.isChecked()) lookingFor.add("female");
                if(switchHomme.isChecked()) lookingFor.add("male");
                try {
                    UserPatch userPatch = new UserPatch();
                    userPatch.setLooking_for_age_min(rangeSeekBar.getSelectedMinValue());
                    userPatch.setLooking_for_age_max(rangeSeekBar.getSelectedMaxValue());
                    userPatch.setLooking_for(lookingFor);
                    patch(sharedPref.getString("auth", ""), userPatch, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("PATCH", String.valueOf(response.code()));
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        switchFemme = (Switch) v.findViewById(R.id.switch_femme);
        switchFemme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                List<String> lookingFor = new ArrayList<String>();
                if(switchFemme.isChecked()) lookingFor.add("female");
                if(switchHomme.isChecked()) lookingFor.add("male");
                try {
                    UserPatch userPatch = new UserPatch();
                    userPatch.setLooking_for_age_min(rangeSeekBar.getSelectedMinValue());
                    userPatch.setLooking_for_age_max(rangeSeekBar.getSelectedMaxValue());
                    userPatch.setLooking_for(lookingFor);
                    patch(sharedPref.getString("auth", ""), userPatch, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("PATCH", String.valueOf(response.code()));
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        seekbarDistance = (SeekBar) v.findViewById(R.id.seekBar_distance);
        seekbarDistance.setProgress(0);
        seekbarDistance.setMax(200);
        Log.d("Distance", String.valueOf(sharedPref.getInt("distance",1)));
        seekbarDistance.setProgress(sharedPref.getInt("distance",10));
        distance.setText(String.valueOf(seekbarDistance.getProgress()+"km"));
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
                editor.commit();
                distance.setText(String.valueOf(seekBar.getProgress()+"km"));
            }
        });
        //Init RangeSeekBar
        rangeSeekBar = (RangeSeekBar) v.findViewById(R.id.rangeSeekBar_age);
        rangeSeekBar.setRangeValues(18,100);
        //age.setText(rangeSeekBar.getSelectedMinValue() + "-" + rangeSeekBar.getSelectedMaxValue());
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                List<String> lookingFor = new ArrayList<String>();
                if(switchFemme.isChecked()) lookingFor.add("female");
                if(switchHomme.isChecked()) lookingFor.add("male");
                try {
                    UserPatch userPatch = new UserPatch();
                    userPatch.setLooking_for_age_min(rangeSeekBar.getSelectedMinValue());
                    userPatch.setLooking_for_age_max(rangeSeekBar.getSelectedMaxValue());
                    patch(sharedPref.getString("auth", ""), userPatch, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("PATCH", String.valueOf(response.code()));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    age.setText(rangeSeekBar.getSelectedMinValue() + "-" + rangeSeekBar.getSelectedMaxValue());
                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                editor.commit();
                LoginManager.getInstance().logOut();
                getActivity().finish();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        //Get the new profil user
        try {
            Log.d("Authentification", sharedPref.getString("auth",""));
            getProfil(sharedPref.getString("auth",""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
    }

    public void getProfil(final String auth) throws IOException {
        Log.d("Profil","Profil");
        ProfilServer.getProfil(auth, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Profil", String.valueOf(response.code()));
                        String body = null;
                        try {
                            body = response.body().string();
                            Log.d("Profil",body);
                            final SharedPreferences sharedPref = mContext.getSharedPreferences(
                                    getString(R.string.user_file_key), Context.MODE_PRIVATE);
                            final SharedPreferences.Editor editor = sharedPref.edit();
                            if(response.code() == ResponseCode.REQUEST_UNAUTHAURIZED){
                                Log.d("REQUEST_UNAUTHAURIZED","REQUEST_UNAUTHAURIZED");
                                editor.putString("auth","");
                                editor.putString("uuid","");
                                editor.commit();
                                Intent intent = new Intent(mContext,LoginActivity.class);
                                startActivity(intent);
                            }else{
                                profil = new Gson().fromJson(body, Profil.class);
                                editor.putString("uuid",profil.getUuid());
                                editor.commit();
                                initSubViewsWithProfilAndPreferences(profil);

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void initSubViewsWithProfilAndPreferences(final Profil profil){
        nameAndImage.startAnimation(animfadeInText);
        nameAndImage.setText(profil.getFirst_name()+" , "+profil.getAge());
        circleImageView.startAnimation(animfadeOutImage);
        animfadeOutImage.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String imageUrl = "http://i.imgur.com/hES7D98.jpg";
                Glide.with(mContext).load(imageUrl).into(circleImageView);
                circleImageView.startAnimation(animfadeInImage);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if(profil.getLooking_for().contains("male")){
            switchHomme.setChecked(true);
        }
        else switchHomme.setChecked(false);
        if (profil.getLooking_for().contains("female")){
            switchFemme.setChecked(true);
        }
        else switchHomme.setChecked(false);

        /**
         * SharedPreferences
         */
        final SharedPreferences sharedPref = mContext.getSharedPreferences(
                getString(R.string.user_file_key), Context.MODE_PRIVATE);
        seekbarDistance.setProgress(sharedPref.getInt("distance",10));

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
