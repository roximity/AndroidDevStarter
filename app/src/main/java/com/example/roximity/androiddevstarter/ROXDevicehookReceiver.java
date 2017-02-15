package com.example.roximity.androiddevstarter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.roximity.sdk.external.ROXConsts;
import com.roximity.sdk.messages.ROXEventInfo;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by colerichards on 10/11/16.
 */

public class ROXDevicehookReceiver extends BroadcastReceiver {

    public static final String TAG = "ROXDevicehookReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Forward this on via local broadcast
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        //Write the event to internal storage cache
        ROXEventInfo event = (ROXEventInfo) intent.getSerializableExtra(ROXConsts.EXTRA_EVENT_DATA);
        updateEventCache(event,context);
    }

    private void updateEventCache(ROXEventInfo event, Context context){
        try {
            // Retrieve the list from internal storage
            Object cachedObject = InternalStorage.readObject(context, ROXIMITYObserver.EVENT_CACHE_KEY);
            if (cachedObject == null) cachedObject = new ArrayList<ROXEventInfo>();
            try {
                ArrayList<ROXEventInfo> cache = (ArrayList<ROXEventInfo>) cachedObject;
                cache.add(event);
                InternalStorage.writeObject(context,ROXIMITYObserver.EVENT_CACHE_KEY,cache);
            }catch (ClassCastException e){
                Log.d(TAG, "Couldn't retrieve cached events due to class casting exception");
            }
        } catch (NullPointerException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
