package com.example.roximity.androiddevstarter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.roximity.sdk.ROXIMITYEngine;
import com.roximity.sdk.ROXIMITYEngineListener;
import com.roximity.sdk.external.ROXConsts;
import com.roximity.sdk.messages.ROXEventInfo;
import com.roximity.sdk.messages.ROXIMITYAction;
import com.roximity.sdk.messages.ROXIMITYSignal;
import com.roximity.system.exceptions.GooglePlayServicesMissingException;
import com.roximity.system.exceptions.IncorrectContextException;
import com.roximity.system.exceptions.MissingApplicationIdException;

import java.util.HashMap;

/**
 * Created by colerichards on 6/9/16.
 */
public class ROXIMITYObserver implements ROXIMITYEngineListener {


    private static final String TAG = "ROXObserver";

    public ROXIMITYObserver(Context context) throws GooglePlayServicesMissingException, IncorrectContextException, MissingApplicationIdException {

        startROXIMITYEngine(context);
        registerDeviceHookEvent(context);

    }


    private void startROXIMITYEngine(Context context) throws GooglePlayServicesMissingException, IncorrectContextException, MissingApplicationIdException {
        HashMap<String,Object> roximityOptions = new HashMap<>();
        roximityOptions.put(ROXConsts.ENGINE_OPTIONS_MUTE_BLUETOOTH_OFF_ALERT, false);
        roximityOptions.put(ROXConsts.ENGINE_OPTIONS_MUTE_REQUEST_ALERTS, false);
        roximityOptions.put(ROXConsts.ENGINE_OPTIONS_START_LOCATION_DEACTIVATED, false);

        ROXIMITYEngine.startEngineWithOptions(context, R.drawable.ic_launcher, roximityOptions, this, null);
    }

    @Override
    public void onROXIMITYEngineStarted() {
        Log.d(TAG, "ROXIMITYEngine has started");
    }

    @Override
    public void onROXIMITYEngineStopped() {
        Log.d(TAG, "ROXIMITYEngine was stopped");
    }

    private void registerDeviceHookEvent(Context context){
        IntentFilter intentFilter = new IntentFilter(ROXConsts.ROXIMITY_EVENT_ACTION);
        BroadcastReceiver deviceHookReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                receivedDeviceHook(intent);
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(deviceHookReceiver,intentFilter);

    }

    private void receivedDeviceHook(Intent intent){
        ROXEventInfo eventInfo = (ROXEventInfo) intent.getSerializableExtra(ROXConsts.EXTRA_EVENT_DATA);
        ROXIMITYAction action = eventInfo.getROXIMITYAction();
        ROXIMITYSignal signal = eventInfo.getROXIMITYSignal();
        Log.d("ROXSignal", signal.getId());
        Log.d("ROXSignal", signal.getName());
        Log.d("ROXSignal", String.valueOf(signal.getType()));
        Log.d("ROXSignal", String.valueOf(signal.getTags()));

        Log.d("ROXAction", action.getId());
        Log.d("ROXAction", action.getName());
        Log.d("ROXAction", action.getMessage());
        Log.d("ROXAction", String.valueOf(action.getPresentationType()));
        Log.d("ROXAction", String.valueOf(action.getTags()));
    }
}
