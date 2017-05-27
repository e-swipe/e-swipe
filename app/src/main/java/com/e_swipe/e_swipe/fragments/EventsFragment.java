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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.adapter.EventAdapter;
import com.e_swipe.e_swipe.model.EventCard;
import com.e_swipe.e_swipe.server.event.EventServer;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    private static List<EventCard> eventList;
    /**
     * Subviews
     */
    ListView listEvent;

    /**
     * EventAdapter
     */
    EventAdapter eventAdapter;

    /**
     * currentOffset
     */
    int offset;

    /**
     * isLoading ?
     */

    boolean loading;

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
    public static EventsFragment newInstance(Context context,List<EventCard> events) {
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
        eventAdapter = new EventAdapter(mContext,eventList);
        listEvent.setAdapter(eventAdapter);
        listEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO
                // Create Intent to new Activity
            }
        });

        listEvent.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount !=0){
                    if(!loading){
                        getEvents();
                        loading = true;
                    }
                }
            }
        });

        getEvents();

        return view;
    }


    public void getEvents() {
        //Get Events from Server
        LocationManager locationManager = ((LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE));
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            try {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                int latitude = (int) location.getLatitude();
                int longitude = (int) location.getLatitude();

                int radius;
                int limit = 10;

                //Get SharedPreferences for radius
                final SharedPreferences sharedPref = mContext.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                radius = sharedPref.getInt(getString(R.string.distance), 1);
                EventServer.getAllEvents(sharedPref.getString("auth", ""), latitude, longitude, radius, offset, limit, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new Gson();
                        EventCard[] eventCards = gson.fromJson(response.body().toString(), EventCard[].class);
                        eventList = new ArrayList<>(Arrays.asList(eventCards));
                        eventAdapter.notifyDataSetChanged();
                        loading = false;
                        offset++;
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
