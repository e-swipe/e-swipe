package com.e_swipe.e_swipe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.objects.ChatMessage;

import java.util.ArrayList;

/**
 * Created by Anthonny on 16/05/2017.
 */
public class ChatAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<ChatMessage> chatMessages;
    Context context;

    public ChatAdapter(Context context, ArrayList<ChatMessage> chatMessages){
        this.context = context;
        this.chatMessages = chatMessages;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int i) {
        return chatMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessage chatMessage = (ChatMessage) getItem(i);
        View vi = view;
        if(view == null){
            vi = inflater.inflate(R.layout.chat_bubble, null);
        }
        TextView textMessage = (TextView) vi.findViewById(R.id.message_text);
        textMessage.setText(chatMessage.getMessage());

        LinearLayout layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout);
        LinearLayout parent_layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout_parent);

        // if message is mine then align to right
        if (chatMessage.isMine()) {
            layout.setBackgroundResource(R.drawable.bubble2);
            parent_layout.setGravity(Gravity.RIGHT);
        }
        // If not mine then align to left
        else {
            layout.setBackgroundResource(R.drawable.bubble1);
            parent_layout.setGravity(Gravity.LEFT);
        }
        textMessage.setTextColor(Color.BLACK);
        return vi;
    }

    public void addChatMessage(ChatMessage chatMessage){
        chatMessages.add(chatMessage);
    }
}
