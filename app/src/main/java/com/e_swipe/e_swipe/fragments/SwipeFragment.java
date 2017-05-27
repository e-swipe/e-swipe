package com.e_swipe.e_swipe.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.layout.TinderCard;
import com.e_swipe.e_swipe.objects.Event;
import com.e_swipe.e_swipe.objects.JsonLoader;
import com.e_swipe.e_swipe.objects.ProfilTinderCard;
import com.e_swipe.e_swipe.server.Profil.Swipe;
import com.e_swipe.e_swipe.utils.ResponseCode;
import com.google.gson.Gson;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Class related to the swipe fragment
 */
public class SwipeFragment extends Fragment {

    /**
     * Attribute related to listen interaction on fragment
     */
    private OnFragmentInteractionListener mListener;
    /**
     * Application Context
     */
    static Context mContext;
    /**
     * Holder of every tinderCards
     */
    SwipePlaceHolderView mSwipeView;

    onSwipeEventListener onSwipeEventListener;

    /**
     * Empty Constructor
     */
    public SwipeFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param context Application Context
     * @return a new instance of the SwipeFragment
     */
    public static SwipeFragment newInstance(Context context) {
        SwipeFragment fragment = new SwipeFragment();
        mContext = context;
        return fragment;
    }

    @Override
    /**
     * onCreateMethod
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    /**
     * @return view inflated with subviews
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_swipe, container, false);

        //Initialise the SwipePlaceHolderView
        mSwipeView = (SwipePlaceHolderView)v.findViewById(R.id.swipeView);

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {
            @Override
            public void onItemRemoved(int count) {
                //do something when the count changes to some specific value.
                //For Example: Call server to fetch more data when count is zero
                //Needs to fetch data a little bit before the end of count
                if(count == 2){
                    requestSwipeables();
                }
            }
        });

        //For each profiles in the file (assets/profiles.json) add a new tinderCard to the holder
        for(ProfilTinderCard profilTinderCard : JsonLoader.loadProfiles(mContext)){
            TinderCard tinderCard = new TinderCard(mContext, profilTinderCard, mSwipeView);
            tinderCard.setOnSwipeListener(new TinderCard.onSwipeListener() {
                @Override
                public void onCardChange(TinderCard tinderCard) {
                    onSwipeEventListener.onCardChange(tinderCard);
                }

                @Override
                public void onSwipeCancel() {
                    onSwipeEventListener.onSwipeCancel();
                }

                @Override
                public void onSwipeStarted() {
                    onSwipeEventListener.onSwipeStarted();
                }
            });
            mSwipeView.addView(tinderCard);
        }

        //Init buttons that will handle accept and reject of a tinderCard
        v.findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        v.findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        return v;
    }

    public void requestSwipeables(){

        SharedPreferences sharedPref = mContext.getSharedPreferences(
                getString(R.string.user_file_key), Context.MODE_PRIVATE);

        LocationManager locationManager = ((LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE));
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            try {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                int latitude = (int) location.getLatitude();
                int longitude = (int) location.getLatitude();

                Swipe.getSwipeable(sharedPref.getString("auth", ""), longitude, latitude, sharedPref.getInt("distance",1), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        if(ResponseCode.checkResponseCode(response.code())){

                            String swipeableString = response.body().string();
                            FileOutputStream fileOutputStream = mContext.openFileOutput("swipeables.json",Context.MODE_PRIVATE);
                            fileOutputStream.write(swipeableString.getBytes(Charset.forName("UTF-8")));
                        }
                    }
                });

            }
            catch (Exception e){

            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public void setOnSwipeEventListener(onSwipeEventListener onSwipeEventListener){
        this.onSwipeEventListener = onSwipeEventListener;
    }
    public interface onSwipeEventListener  {
        public void onFragmentCreated();
        public void onCardChange(TinderCard tinderCard);
        public void onSwipeCancel();
        public void onSwipeStarted();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
