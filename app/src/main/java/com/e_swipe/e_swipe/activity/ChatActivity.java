package com.e_swipe.e_swipe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.adapter.ChatAdapter;
import com.e_swipe.e_swipe.model.Chat;
import com.e_swipe.e_swipe.model.Message;
import com.e_swipe.e_swipe.model.MessageCard;
import com.e_swipe.e_swipe.model.UserCard;
import com.e_swipe.e_swipe.objects.ChatMessage;
import com.e_swipe.e_swipe.server.Chat.ChatServer;
import com.e_swipe.e_swipe.server.Profil.ProfilServer;
import com.e_swipe.e_swipe.server.login.LoginServer;
import com.e_swipe.e_swipe.utils.ResponseCode;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//TODO Get infos from Swagger
public class ChatActivity extends AppCompatActivity {

    EditText editMessage;
    ListView listMessage;
    ImageButton sendButton;
    ChatAdapter chatAdapter;
    ArrayList<ChatMessage> chatMessage;
    TextView chatName;
    TextView lastMessage;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chatName = (TextView) toolbar.findViewById(R.id.chat_name);
        lastMessage = (TextView) toolbar.findViewById(R.id.toolbar_last_message);


        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();
        final String chatUuid =  intent.getStringExtra("uuid");

        Log.d("ChatActivity","Uuid " + chatUuid);
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

        sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.user_file_key), Context.MODE_PRIVATE);

        ChatServer.getChatInfo(chatUuid, sharedPref.getString("auth", ""), 0, 150, "0", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("ChatActivity","Get "+response.code());
                String body = response.body().string();
                Log.d("ChatActivity","body "+body);
                if(ResponseCode.checkResponseCode(response.code())){
                    final Chat chat = new Gson().fromJson(body,Chat.class);
                    final List<Message> messages = chat.getMessages();
                    Collections.reverse(messages);
                    for(Message message : messages){
                       addNewMessage(message);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lastMessage.setText(messages.get(messages.size()-1).getContent());
                            chatName.setText(chat.getUser().getFirst_name());
                        }
                    });
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final String message = editMessage.getText().toString();
                SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String myDate = format.format(new Date());
                Log.d("ChatActivity","Date : " + myDate);
                Log.d("ChatActivity",chatUuid);

                ProfilServer.writeMessage(sharedPref.getString("auth",""), chatUuid, new MessageCard(message, myDate), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("ChatActivity", String.valueOf(response.code()));
                        //Write to database
                        ChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatAdapter.addChatMessage(new ChatMessage(sharedPref.getString("uuid",""),message,true));
                                chatAdapter.notifyDataSetChanged();
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lastMessage.setText(message);
                            }
                        });
                    }
                });
                editMessage.getText().clear();
                editMessage.clearFocus();
            }
        });
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

    public void addNewMessage(Message message){
        Log.d("ChatActivity","UserId" + message.getUser_id());
        Log.d("ChatActivity","Me" + sharedPref.getString("uuid",""));
        if(message.getUser_id().equals(sharedPref.getString("uuid",""))){
            chatAdapter.addChatMessage(new ChatMessage(sharedPref.getString("uuid",""), message.getContent(),true));
        }else {
            chatAdapter.addChatMessage(new ChatMessage(sharedPref.getString("uuid",""), message.getContent(),false));
        }
    }
}
