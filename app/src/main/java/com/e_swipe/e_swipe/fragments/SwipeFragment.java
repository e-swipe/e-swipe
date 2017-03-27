package com.e_swipe.e_swipe.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.layout.TinderCard;
import com.e_swipe.e_swipe.objects.JsonLoader;
import com.e_swipe.e_swipe.objects.Profile;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.List;

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

        //For each profiles in the file (assets/profiles.json) add a new tinderCard to the holder
        for(Profile profile : JsonLoader.loadProfiles(mContext)){
            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
