/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bytes.mangalho;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "-->MyFirebaseMsgService";
    SharedPrefrence prefrence;
    int i = 0;
    String refreshedToken;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        prefrence = SharedPrefrence.getInstance(this);
        Log.e(TAG, "-->notification "+ remoteMessage.toString());
        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData() != null) {
                String types =remoteMessage.getData().get("type");
                Log.e("-->notification types",types);

                if (types.equals(Consts.CHAT_NOTIFICATION)) {
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Consts.BROADCAST);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                    i = prefrence.getIntValue("Value");
                    i++;
                    prefrence.setIntValue("Value", i);
                }else {
                    sendNotification(remoteMessage.getNotification().getBody());
                }

            }

        }

    }
    @Override
    public void onNewToken(String token) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Consts.FIREBASE_TOKEN, token);
        editor.commit();
        SharedPreferences userDetails = MyFirebaseMessagingService.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e(TAG, "-->my token: " + userDetails.getString(Consts.FIREBASE_TOKEN, ""));

    }

    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";


        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Mangal Ho")
                .setSound(defaultSoundUri)
                /* .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))*/
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);
        ;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

}