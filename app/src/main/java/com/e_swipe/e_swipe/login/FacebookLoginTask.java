package com.e_swipe.e_swipe.login;

import android.os.AsyncTask;

/**
 * Created by Anthonny on 23/05/2017.
 */
public class FacebookLoginTask extends AsyncTask <Void, Void , String>{
    String facebookToken;
    String firebaseId;

    public interface AsyncResponse {
        void processFinish(String token);
        void processError();
    }

    AsyncResponse asyncResponse = null;

    public FacebookLoginTask(String facebookToken, String firebaseId, AsyncResponse asynResponse){
        this.facebookToken = facebookToken;
        this.firebaseId = firebaseId;
        this.asyncResponse = asynResponse;
    }
    @Override
    protected String doInBackground(Void... voids) {
        //Log with Server + response.getStatusLine().getStatusCode()
        return null;
    }

    @Override
    protected void onPostExecute(String auth) {
        super.onPostExecute(auth);
        if(auth == null) {
            asyncResponse.processError();
        }
        else {
            asyncResponse.processFinish(auth);
        }
    }
}
