package com.e_swipe.e_swipe.utils;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class ResponseCode {

    public static final int REQUEST_OK = 200;
    public static final int REQUEST_CREATED = 201;
    public static final int REQUEST_SUCCESS_NO_CONTENT = 204;

    public static final int REQUEST_UNAUTHAURIZED = 401;
    public static final int REQUEST_INCORECT_PARAMETERS = 422;

    public static boolean checkResponseCode(int responseCode){
        switch (responseCode) {
            case REQUEST_OK:
                return true;
            case REQUEST_CREATED:
                return true;
            case REQUEST_SUCCESS_NO_CONTENT:
                return true;
            case REQUEST_UNAUTHAURIZED:
                return true;
            case REQUEST_INCORECT_PARAMETERS:
                return true;
        }
        return false;
    }
}
