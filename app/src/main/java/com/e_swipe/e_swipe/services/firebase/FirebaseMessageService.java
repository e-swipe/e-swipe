package com.e_swipe.e_swipe.services.firebase;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.e_swipe.e_swipe.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Anthonny on 06/03/2017.
 */
public class FirebaseMessageService extends FirebaseMessagingService {

    private static final String TAG_TOKEN = "TOKEN";
    private RemoteMessage lastMessageReceived;
    private Bitmap bitmap;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG_TOKEN, "From: " + remoteMessage.getFrom());
        lastMessageReceived = remoteMessage;
        // Check if message contains a data payload.

        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().get("Type") != null) {
                switch (remoteMessage.getData().get("Type")) {
                    case "Match":
                        break;
                    case "Message":
                        break;
                    case "Events":
                        break;
                }
            }
        }
        Log.d(TAG_TOKEN, "Message data payload: " + remoteMessage.getData());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

        }

        sendNotification(remoteMessage.getNotification().getBody());

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {

        final String personName = lastMessageReceived.getNotification().getTitle();
        final String bitmapURL = lastMessageReceived.getData().get("URL");
        final Context context = getApplicationContext();

        ImageDownloader imageDownloader = new ImageDownloader(bitmapURL, new ImageDownloader.DownloadResponse() {
            @Override
            public void downloadEnded(Bitmap bitmap) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setLargeIcon(bitmap)
                                .setContentTitle(personName)
                                .setContentText("You have Matched with " + personName);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(001, mBuilder.build());
            }
        });
        imageDownloader.execute();
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