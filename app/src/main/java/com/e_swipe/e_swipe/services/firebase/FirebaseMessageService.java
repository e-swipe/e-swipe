package com.e_swipe.e_swipe.services.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.activity.ChatActivity;
import com.e_swipe.e_swipe.model.Message;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Anthonny on 06/03/2017.
 */
public class FirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        sendNotification(remoteMessage);
        Log.d("Test","Test");
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     */
    private void sendNotification(RemoteMessage message) {
        if(message.getData().size() > 0) {
            Message messageJson = new Gson().fromJson(message.getData().get("message"), Message.class);
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("uuid", message.getData().get("uuid"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(body);
            mBuilder.setSmallIcon(R.mipmap.ic_eswipe_logo);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }
    }


    public static class ImageDownloader extends AsyncTask<Void,Void,Bitmap>{

        String imageUrl;

        public interface DownloadResponse{
            public void downloadEnded(Bitmap bitmap);
        }

        DownloadResponse downloadResponse;

        ImageDownloader(String imageUrl, DownloadResponse downloadResponse){
            this.imageUrl = imageUrl;
            this.downloadResponse = downloadResponse;
        }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                return bitmap;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            downloadResponse.downloadEnded(bitmap);
            super.onPostExecute(bitmap);
        }
    }

}