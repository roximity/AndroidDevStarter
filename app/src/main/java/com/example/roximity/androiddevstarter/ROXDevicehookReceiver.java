package com.example.roximity.androiddevstarter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by colerichards on 10/11/16.
 */

public class ROXDevicehookReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Forward this on via local broadcast
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
