package com.e_swipe.e_swipe.server.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.e_swipe.e_swipe.model.UserFacebook;
import com.e_swipe.e_swipe.utils.PhotoUtils;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class LoginServer {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType MEDIA_TYPE_PNG
            = MediaType.parse("image/png");

    public static final String BASE_URL = "api.stardis.blue";

    public static void withFacebook(String facebookAuth, String instanceId, String userId,
                                      UserFacebook userFacebook, Callback callback) throws IOException, JSONException {
        //User : first_name, last_name, date_Of_birth, gender, email
        Log.d("LoginServer " , "withFacebook");
        Gson gson = new Gson();
        String userFacebookGson =  gson.toJson(userFacebook);

        Log.d("LoginServer",userFacebookGson);

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("login")
                .addPathSegment("facebook")
                .addQueryParameter("facebook_auth",facebookAuth)
                .addQueryParameter("instance_id",instanceId)
                .build();

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, userFacebookGson);

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }


    public static void withMailAndPassword(String email, String password, String instanceId, Callback callback) throws IOException, JSONException {

        Log.d("LoginServer",email);
        Log.d("LoginServer",password);
        Log.d("LoginServer",instanceId);
        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("login")
                .addQueryParameter("email",email)
                .addQueryParameter("password",password)
                .addQueryParameter("instance_id",instanceId)
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

    public static Bitmap getFacebookProfilePicture(String userID){
        Log.d("GET IMAGE","Get image");
        FacebookGraphConnector facebookGraphConnector = new FacebookGraphConnector(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("PHOTO SEND", String.valueOf(response.code()));
            }
        });
        String url = "https://graph.facebook.com/" + userID + "/picture?type=large";
        Bitmap bitmap = null;
        try {
            facebookGraphConnector.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    public static class FacebookGraphConnector extends AsyncTask<String,Void,Bitmap> {

        Callback callback;
        FacebookGraphConnector(Callback callback){
            this.callback = callback;
        }
        @Override
        protected Bitmap doInBackground(String[] strings) {
            URL imageURL = null;
            Bitmap bitmap = null;
            try {
                imageURL = new URL(strings[0]);
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //SendPhotos
            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host(BASE_URL)
                    .addPathSegment("v1")
                    .addPathSegment("me")
                    .addPathSegment("photos")
                    .build();

            OkHttpClient client = new OkHttpClient();

            String myBase64Image = PhotoUtils.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);

            RequestBody formBody = new FormBody.Builder()
                    .add("photo",myBase64Image)
                    .build();

            Request request = new Request.Builder()
                    .url(httpUrl)
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(callback);


        }
    }
}
