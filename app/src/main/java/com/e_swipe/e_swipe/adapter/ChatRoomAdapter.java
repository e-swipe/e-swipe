package com.e_swipe.e_swipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.model.ChatCard;
import com.e_swipe.e_swipe.objects.ChatRoom;

import java.util.List;

/**
 * Adapter class linked to ChatRoom list View in the ChatRoom fragment layout related to
 */
public class ChatRoomAdapter extends BaseAdapter implements View.OnClickListener {
    /**
     * List of every chatRooms
     */
    List<ChatCard> chatRooms;
    /**
     * Context of the Activity/App
     */
    Context context;


    public ChatRoomAdapter(Context context, List<ChatCard> chatRooms){
        this.chatRooms = chatRooms;
        this.context = context;
    }

    @Override
    /**
     * @return size of chatList
     */
    public int getCount() {
        return chatRooms.size();
    }

    @Override
    /**
     * @return ChatRoom at position
     */
    public ChatCard getItem(int position) {
        return chatRooms.get(position);
    }

    @Override
    /**
     * @return id of item at position
     */
    public long getItemId(int position) {
        return position;
    }

    @Override
    /**
     * @return the view inflated with subviews
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ){
            //We must create a View:
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_chat, parent, false);
            //Init subViews
            TextView chatName = (TextView) convertView.findViewById(R.id.chat_name);
            chatName.setText(getItem(position).getUser().getFirst_name());
            TextView lastMessage = (TextView) convertView.findViewById(R.id.last_message);
            lastMessage.setHint(getItem(position).getLast_message_content());

        }
        return convertView;
    }

    @Override
    // TODO: 27/03/2017
    public void onClick(View v) {

    }
}