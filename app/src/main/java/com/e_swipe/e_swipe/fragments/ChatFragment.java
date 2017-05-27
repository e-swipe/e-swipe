package com.e_swipe.e_swipe.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.e_swipe.e_swipe.activity.ChatActivity;
import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.adapter.ChatRoomAdapter;
import com.e_swipe.e_swipe.objects.ChatRoom;

import java.util.List;

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
    private static List<ChatRoom> chatRooms;
    /**
     * the listView to inflate
     */
    ListView listView;

    /**
     * Empty constructor
     */
    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param chatsRooms List of every chatRooms
     * @return a new instance of the chatFragment
     */
    public static ChatFragment newInstance(List<ChatRoom> chatsRooms) {
        ChatFragment fragment = new ChatFragment();
        chatRooms = chatsRooms;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        listView = (ListView) view.findViewById(R.id.chat_list);
        //Set Adapter
        listView.setAdapter(new ChatRoomAdapter(getContext(), chatRooms));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        return view;
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
