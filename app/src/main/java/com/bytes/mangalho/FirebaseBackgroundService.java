package com.bytes.mangalho;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.legacy.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;

public class FirebaseBackgroundService extends WakefulBroadcastReceiver {

  private static final String TAG = "FirebaseService";
  SharedPrefrence prefrence;
  int i = 0;
  @Override
  public void onReceive(Context context, Intent intent) {
    prefrence = SharedPrefrence.getInstance(context);
    Log.d(TAG, "I'm in!!!");

    if (intent.getExtras() != null) {
      for (String key : intent.getExtras().keySet()) {
        Object value = intent.getExtras().get(key);
        Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
        if(value.toString().equalsIgnoreCase("New News Available") && value != null) {
          Intent broadcastIntent = new Intent();
          broadcastIntent.setAction(Consts.BROADCAST);
          LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
          i = prefrence.getIntValue("Value");
          i++;
          prefrence.setIntValue("Value", i);
          Bundle bundle = new Bundle();
          Intent backgroundIntent = new Intent(context, Dashboard.class);
          bundle.putString("push_message", value + "");
          backgroundIntent.putExtras(bundle);
          context.startService(backgroundIntent);
        }
      }
    }
  }
}