package com.e_swipe.e_swipe.login;

import android.os.AsyncTask;

/**
 * Created by Anthonny on 23/05/2017.
 */
public class MailPasswordLoginTask extends AsyncTask<Void, Void , String> {

    String mail;
    String password;
    String firebaseId;

    public interface AsyncResponse {
        void processFinish(String token);
        void processError();
    }

    AsyncResponse asyncResponse = null;

    public MailPasswordLoginTask(String mail, String password, String firebaseId, AsyncResponse asyncResponse){
        this.mail = mail;
        this.password = password;
        this.firebaseId = firebaseId;
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected String doInBackground(Void... voids) {
        //Request server + response.getStatusLine().getStatusCode()
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
