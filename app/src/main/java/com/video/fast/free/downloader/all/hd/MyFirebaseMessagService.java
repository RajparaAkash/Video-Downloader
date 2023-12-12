package com.video.fast.free.downloader.all.hd;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle the received FCM message here
        // You can access the message data and perform appropriate actions
        remoteMessage.getData().size();// Handle data payload of the FCM message

        remoteMessage.getNotification();// Handle notification payload of the FCM message
    }

    @Override
    public void onNewToken(String token) {
        // Handle the generation or refreshing of the FCM token
        // This method will be invoked whenever a new token is generated or an existing token is refreshed
        // You can send the token to your app server for further processing or store it locally for later use
    }

}
