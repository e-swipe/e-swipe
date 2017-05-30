package com.e_swipe.e_swipe.adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.model.EventCard;
import com.e_swipe.e_swipe.objects.Event;

import java.util.List;

/**
 * Adapter class linked to Event list View in the event fragment layout related to
 */
public class EventAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    /**
     * List of every event to display
     */
    List<EventCard> eventList;
    /**
     * Context related to the activity/Application
     */
    Context mContext;

    /**
     * Constructor
     * @param context Application Context
     * @param eventList List of events
     */
    public EventAdapter(Context context, List<EventCard> eventList) {
        this.eventList = eventList;
        this.mContext = context;
    }

    @Override
    /**
     * @return size of eventList
     */
    public int getCount() {
        return eventList.size();
    }

    @Override
    /**
     *  @return event at position
     */
    public EventCard getItem(int position) {
        return eventList.get(position);
    }

    @Override
    /**
     * @return itemId/Position
     */
    public long getItemId(int position) {
        return position;
    }

    @Override
    /**
     * @return the view inflated with subviews
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            //We must create a View:
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_event, parent, false);
            //Init subViews
            TextView eventName = (TextView) convertView.findViewById(R.id.event_name);
            eventName.setText(getItem(position).getName());
            TextView eventLocalisation = (TextView) convertView.findViewById(R.id.event_localisation);
            //TODO
            LocationManager locationManager = ((LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE));
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Current User Location
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location locationEvent = new Location("pointUser");
                locationEvent.setLatitude(getItem(position).getPosition().getLatitude());
                locationEvent.setLongitude(getItem(position).getPosition().getLongitude());

                float meters = location.distanceTo(locationEvent);
                int km = (int) Math.ceil(meters/1000);
                eventLocalisation.setText(km+"km");
            }

            ImageView imageView =  (ImageView) convertView.findViewById(R.id.event_image);
            //Ask Glide to load the image from url into imageView
            Glide.with(mContext).load(getItem(position).getPicture_url()).into(imageView);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
