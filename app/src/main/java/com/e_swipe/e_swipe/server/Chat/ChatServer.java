package com.e_swipe.e_swipe.server.Chat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Anthonny on 27/05/2017.
 */

public class ChatServer {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final String BASE_URL =  "api.stardis.blue";

    public static void getAllChats(String auth, int offset, int limit, Callback callback){

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("me")
                .addPathSegment("chats")
                .addQueryParameter("offset",Integer.toString(offset))
                .addQueryParameter("limit",Integer.toString(limit))
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("auth",auth)
                .url(httpUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void getChatInfo(String uuid, String auth, int offset, int limit, String since, Callback callback){

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("chats")
                .addPathSegment(uuid)
                .addQueryParameter("offset",Integer.toString(offset))
                .addQueryParameter("limit",Integer.toString(limit))
                .addQueryParameter("since",since)
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("auth",auth)
                .url(httpUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

    }
}
