package com.e_swipe.e_swipe.facebook;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphResponse;

import org.json.JSONObject;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class GraphRequest {
    //TODO
    public interface GraphListener {
        public void onGraphRequestEnded(JSONObject jsonObject);
    }

    static GraphListener graphListener = null;

    GraphRequest(GraphListener graphListener){
        graphListener = graphListener;
    }

    public static void request (AccessToken accessToken, Bundle bundle){
        com.facebook.GraphRequest request = com.facebook.GraphRequest.newMeRequest(
                accessToken,
                new com.facebook.GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        graphListener.onGraphRequestEnded(object);
                    }
                });
        request.setParameters(bundle);
        request.executeAsync();
    }

}
