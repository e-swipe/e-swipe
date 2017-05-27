package com.e_swipe.e_swipe.server.Profil;

import android.graphics.Bitmap;
import android.util.Log;

import com.e_swipe.e_swipe.model.Image;
import com.e_swipe.e_swipe.model.Message;
import com.e_swipe.e_swipe.model.Profil;
import com.e_swipe.e_swipe.model.UserCreate;
import com.e_swipe.e_swipe.model.UserPatch;
import com.e_swipe.e_swipe.utils.PhotoUtils;
import com.e_swipe.e_swipe.utils.ResponseCode;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class ProfilServer {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final String BASE_URL =  "api.stardis.blue";

    public static void addProfil(UserCreate userCreate, String instanceId, Callback callback){
        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("users")
                .addQueryParameter("instance_id",instanceId)
                .build();

        Gson gson = new Gson();
        String json = gson.toJson(userCreate);

        Log.d("Register", json);

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body )
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

    }

    public static void getProfil(String auth, Callback callback) throws IOException {

        Profil profil = null;

        if(auth.equals("")) {

            OkHttpClient client = new OkHttpClient();

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host(BASE_URL)
                    .addPathSegment("v1")
                    .addPathSegment("me")
                    .build();

            Request request = new Request.Builder()
                    .addHeader("auth",auth)
                    .url(httpUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(callback);
        }
    }

    public static void getUserPhotos(String auth, Callback callback) throws IOException {

        if(auth.equals("")) {

            OkHttpClient client = new OkHttpClient();

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host(BASE_URL)
                    .addPathSegment("v1")
                    .addPathSegment("me")
                    .addPathSegment("photos")
                    .build();

            Request request = new Request.Builder()
                    .addHeader("auth",auth)
                    .url(httpUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(callback);
        }
    }
    //TODO
    public static void addPhotos(String auth, Bitmap bitmap, Callback callback) throws IOException {

        String myBase64Image = PhotoUtils.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);

        if(auth.equals("")) {

            OkHttpClient client = new OkHttpClient();

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host(BASE_URL)
                    .addPathSegment("v1")
                    .addPathSegment("me")
                    .addPathSegment("photos")
                    .build();

            RequestBody formBody = new FormBody.Builder()
                    .add("search",myBase64Image)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("auth",auth)
                    .url(httpUrl)
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(callback);

        }
    }

    public static void deletePhoto(String auth, String uuid, Callback callback) throws IOException {


        if(auth.equals("")) {

            OkHttpClient client = new OkHttpClient();

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host(BASE_URL)
                    .addPathSegment("v1")
                    .addPathSegment("me")
                    .addPathSegment("photos")
                    .addPathSegment(uuid)
                    .build();

            RequestBody formBody = new FormBody.Builder()
                    .build();

            Request request = new Request.Builder()
                    .addHeader("auth",auth)
                    .url(httpUrl)
                    .delete()
                    .build();

            Call call = client.newCall(request);
            call.enqueue(callback);

        }
    }

    public static void patch(String auth, UserPatch userPatch, Callback callback) throws IOException {
        if(auth.equals("")) {
            OkHttpClient client = new OkHttpClient();

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host(BASE_URL)
                    .addPathSegment("v1")
                    .addPathSegment("me")
                    .build();

            Gson gson = new Gson();
            String userPatchString = gson.toJson(userPatch);

            RequestBody requestBody = RequestBody.create(JSON,userPatchString);
            Request request = new Request.Builder()
                    .addHeader("auth",auth)
                    .patch(requestBody)
                    .url(httpUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(callback);
        }
    }

    public static void participateEvent(String auth, String uuid, Callback callback){
        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("events")
                .addPathSegment(uuid)
                .addPathSegment("participate")
                .build();

        Log.d("LoginServer",httpUrl.toString());

        RequestBody body = RequestBody.create(null, new byte[]{});

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void refuseEvent(String auth, String uuid, Callback callback){
        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("events")
                .addPathSegment(uuid)
                .addPathSegment("refuse")
                .build();

        Log.d("LoginServer",httpUrl.toString());

        RequestBody body = RequestBody.create(null, new byte[]{});

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void writeMessage(String auth, String uuid, Message message, Callback callback){

        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("chats")
                .addPathSegment(uuid)
                .build();

        Gson gson = new Gson();
        String messageJson = gson.toJson(message);

        RequestBody requestBody = RequestBody.create(JSON,messageJson);

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
