package com.e_swipe.e_swipe.server.Profil;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Anthonny on 27/05/2017.
 */

public class Swipe {

    public static final String BASE_URL =  "api.stardis.blue";

    public static void getSwipeable(String auth, double longitude, double latitude, int radius, Callback callback){

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("profils")
                .addQueryParameter("latitude",Double.toString(latitude))
                .addQueryParameter("longitude",Double.toString(longitude))
                .addQueryParameter("radius",Integer.toString(radius))
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("auth",auth)
                .url(httpUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void accept(String auth, String uuid, Callback callback){

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("users")
                .addPathSegment(uuid)
                .addPathSegment("accept")
                .build();

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(null, new byte[]{});

        Request request = new Request.Builder()
                .addHeader("auth",auth)
                .url(httpUrl)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void decline(String auth, String uuid, Callback callback){

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("users")
                .addPathSegment(uuid)
                .addPathSegment("decline")
                .build();

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(null, new byte[]{});

        Request request = new Request.Builder()
                .addHeader("auth",auth)
                .url(httpUrl)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
