package com.e_swipe.e_swipe.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.e_swipe.e_swipe.activity.ChatActivity;
import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.activity.TabbedActivity;
import com.e_swipe.e_swipe.adapter.ChatRoomAdapter;
import com.e_swipe.e_swipe.model.ChatCard;
import com.e_swipe.e_swipe.objects.ChatRoom;
import com.e_swipe.e_swipe.server.Chat.ChatServer;
import com.e_swipe.e_swipe.utils.ResponseCode;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.e_swipe.e_swipe.fragments.SwipeFragment.sharedPref;

/**
 * Class that represents the fragment related to chatRooms rooms
 */
public class ChatFragment extends Fragment {

    /**
     * Listener on even that occurs over the fragment
     */
    private OnFragmentInteractionListener mListener;

    /**
     * List of every chatRooms
     */
    private static List<ChatCard> chatRooms;
    /**
     * the listView to inflate
     */
    ListView listView;

    /**
     * If the list view is loading new objects from server
     */
    boolean loading;

    /**
     * Current context
     */
    static Context mContext;

    /**
     * Current Offset
     */
    int offset;

    /**
     * Current Adapter
     */
    ChatRoomAdapter chatRoomAdapter;
    /**
     * Empty constructor
     */
    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * @return a new instance of the chatFragment
     */
    public static ChatFragment newInstance(Context context) {
        ChatFragment fragment = new ChatFragment();
        chatRooms = new ArrayList<>();
        mContext = context;
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
     * @return inflated views and subviews
     */
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        listView = (ListView) view.findViewById(R.id.chat_list);
        //Set Adapter
        chatRoomAdapter = new ChatRoomAdapter(getContext(),chatRooms);
        listView.setAdapter(chatRoomAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("uuid",chatRooms.get(i).getUuid());
                startActivity(intent);
            }
        });
        loading = true;
        offset = 0;

        /*listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount !=0){
                    if(!loading){
                        //Get SharedPreferences for radius
                        final SharedPreferences sharedPref = mContext.getSharedPreferences(
                                getString(R.string.user_file_key), Context.MODE_PRIVATE);
                        ChatServer.getAllChats(sharedPref.getString("auth", ""), offset, 10, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String body = response.body().string();
                                Log.d("Chat",body);
                                if(ResponseCode.checkResponseCode(response.code())){
                                    ChatCard[] chatCards = new Gson().fromJson(body, ChatCard[].class);
                                    chatRooms.addAll(Arrays.asList(chatCards));
                                    offset++;
                                }
                            }
                        });
                    }
                }
            }
        });*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Resume","resume");
        ChatServer.getAllChats(sharedPref.getString("auth", ""), offset, 150, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Chat", String.valueOf(response.code()));
                final String body = response.body().string();
                Log.d("Chat",body);
                if(ResponseCode.checkResponseCode(response.code())){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ChatCard[] chatCards = new Gson().fromJson(body, ChatCard[].class);
                            chatRooms.clear();
                            chatRoomAdapter.notifyDataSetChanged();
                            chatRooms.addAll(Arrays.asList(chatCards));
                            chatRoomAdapter.notifyDataSetInvalidated();
                            chatRoomAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
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
