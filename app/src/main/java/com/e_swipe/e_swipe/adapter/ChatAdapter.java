package com.e_swipe.e_swipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.objects.Chat;

/**
 * Created by Anthonny on 20/03/2017.
 */
public class ChatAdapter extends BaseAdapter implements View.OnClickListener {

    Chat [] chats;
    Context context;
    int ressource;


    public ChatAdapter(Context context, int ressource, Chat[] chats){
        this.chats = chats;
        this.context = context;
        this.ressource = ressource;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getCount() {
        return chats.length;
    }

    @Override
    public Chat getItem(int position) {
        return chats[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ){
            //We must create a View:
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(ressource, parent, false);
            //Init subViews
            TextView chatName = (TextView) convertView.findViewById(R.id.chat_name);
            chatName.setText(getItem(position).getChatName());
            TextView lastMessage = (TextView) convertView.findViewById(R.id.last_message);
            lastMessage.setText(getItem(position).getLastMessage());

        }
        return convertView;
    }
}