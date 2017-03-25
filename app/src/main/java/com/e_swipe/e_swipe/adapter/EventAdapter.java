package com.e_swipe.e_swipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.objects.Event;

import java.util.List;

/**
 * Created by Anthonny on 25/03/2017.
 */
public class EventAdapter extends BaseAdapter implements View.OnClickListener {

    List<Event> eventList;
    Context mContext;

    public EventAdapter(Context context, List<Event> eventList){
        this.eventList = eventList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Event getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ){
            //We must create a View:
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_event, parent, false);
            //Init subViews
            TextView eventName = (TextView) convertView.findViewById(R.id.event_name);
            eventName.setText(getItem(position).getName());
            TextView eventLocalisation = (TextView) convertView.findViewById(R.id.event_localisation);
            eventLocalisation.setText(getItem(position).getLocation());
            ImageView imageView =  (ImageView) convertView.findViewById(R.id.event_image);
            Glide.with(mContext).load(getItem(position).getImageUrl()).into(imageView);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}
