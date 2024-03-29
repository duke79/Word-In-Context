package com.baliyaan.android.wordincontext;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Pulkit Singh on 1/21/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        /*Get updated InstanceID token.*/
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FirebaseInstanceService", "Refreshed token: " + refreshedToken);

         /*If you want to send messages to this application instance or
         manage this apps subscriptions on the server side, send the
         Instance ID token to your app server.*/
        //sendRegistrationToServer(refreshedToken);
    }
}
