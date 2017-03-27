package com.e_swipe.e_swipe.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.adapter.EventAdapter;
import com.e_swipe.e_swipe.objects.Event;
import com.e_swipe.e_swipe.objects.JsonLoader;

import java.util.List;

/**
 * Class that represent the EventFragment
 */
public class EventsFragment extends Fragment {

    /**
     * Listener over interaction that occurs over the fragment
     */
    private OnFragmentInteractionListener mListener;
    /**
     * Application Context
     */
    private static Context mContext;
    /**
     * List of events to display
     */
    private static List<Event> eventList;
    /**
     * Subviews
     */
    ListView listEvent;

    /**
     * Empty constructor
     */
    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * @param context Application context
     * @param events List of events to display
     * @return a new instance of the eventFragment
     */
    public static EventsFragment newInstance(Context context,List<Event> events) {
        EventsFragment fragment = new EventsFragment();
        mContext = context;
        eventList = events;
        return fragment;
    }

    @Override
    /**
     * onCreate Method
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
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        listEvent = (ListView) view.findViewById(R.id.event_list);
        listEvent.setAdapter(new EventAdapter(mContext,eventList));

        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
