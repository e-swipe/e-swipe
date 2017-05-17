package com.e_swipe.e_swipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.e_swipe.e_swipe.adapter.ChatAdapter;
import com.e_swipe.e_swipe.objects.ChatMessage;

import java.util.ArrayList;
//TODO Get infos from Swagger
public class ChatActivity extends AppCompatActivity {

    EditText editMessage;
    ListView listMessage;
    ImageButton sendButton;
    ChatAdapter chatAdapter;
    ArrayList<ChatMessage> chatMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //init chat
        chatMessage = new ArrayList<>();

        listMessage = (ListView) findViewById(R.id.msgListView);
        editMessage = (EditText) findViewById(R.id.messageEditText);
        sendButton = (ImageButton) findViewById(R.id.sendMessageButton);

        // ----Set autoscroll of listview when a new message arrives----//
        listMessage.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listMessage.setStackFromBottom(true);

        chatAdapter = new ChatAdapter(getApplicationContext(),chatMessage);
        listMessage.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            int i = 0;
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString();
                chatAdapter.addChatMessage(new ChatMessage("Test",message,true));
                chatAdapter.addChatMessage(new ChatMessage("Test",Integer.toString(i),false));
                i++;
                editMessage.getText().clear();
                editMessage.clearFocus();
                chatAdapter.notifyDataSetChanged();
            }
        });

        getChatFromSwagger();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getChatFromSwagger() {

    }
}
