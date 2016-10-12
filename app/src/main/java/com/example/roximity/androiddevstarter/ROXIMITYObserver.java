package com.example.roximity.androiddevstarter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.roximity.sdk.ROXIMITYEngine;
import com.roximity.sdk.ROXIMITYEngineListener;
import com.roximity.sdk.external.ROXConsts;
import com.roximity.sdk.messages.ROXEventInfo;
import com.roximity.system.exceptions.GooglePlayServicesMissingException;
import com.roximity.system.exceptions.IncorrectContextException;
import com.roximity.system.exceptions.MissingApplicationIdException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by colerichards on 6/9/16.
 */
public class ROXIMITYObserver implements ROXIMITYEngineListener {


    public static final String EVENT_CACHE_KEY = "com.roximity.event-cache";
    private static final String TAG = "ROXObserver";

    public ArrayList<ROXEventInfo> eventHistory = new ArrayList<>();
    private ArrayList<ROXEventUpdateListener> eventListeners = new ArrayList<>();

    public ROXIMITYObserver(Context context) throws GooglePlayServicesMissingException, IncorrectContextException, MissingApplicationIdException {
        startROXIMITYEngine(context);
        dynamicallyRegisterDeviceHookEvent(context);
        loadCachedEvents(context);
    }

    public boolean addEventUpdateListener(ROXEventUpdateListener updateListener){
        if (this.eventListeners.contains(updateListener)){
            return false;
        }
        this.eventListeners.add(updateListener);
        return true;
    }

    public void backupEventHistory(Context context){
        if (this.eventHistory == null) return;

        try {
            InternalStorage.writeObject(context, ROXIMITYObserver.EVENT_CACHE_KEY, this.eventHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startROXIMITYEngine(Context context) throws GooglePlayServicesMissingException, IncorrectContextException, MissingApplicationIdException {
        HashMap<String,Object> roximityOptions = new HashMap<>();
        roximityOptions.put(ROXConsts.ENGINE_OPTIONS_MUTE_BLUETOOTH_OFF_ALERT, false);
        roximityOptions.put(ROXConsts.ENGINE_OPTIONS_START_LIMIT_AD_TARGETING, false);
        roximityOptions.put(ROXConsts.ENGINE_OPTIONS_START_LOCATION_DEACTIVATED, false);

        ROXIMITYEngine.startEngineWithOptions(context,"[YOUR-APP-ID]", roximityOptions,this);
    }

    @Override
    public void onROXIMITYEngineStarted() {
        Log.d(TAG, "ROXIMITYEngine has started");
    }

    @Override
    public void onROXIMITYEngineStopped() {
        Log.d(TAG, "ROXIMITYEngine was stopped");
    }

    private void dynamicallyRegisterDeviceHookEvent(Context context){
        IntentFilter intentFilter = new IntentFilter(ROXConsts.ROXIMITY_EVENT_ACTION);
        BroadcastReceiver deviceHookReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                receivedDeviceHook(intent, context);
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(deviceHookReceiver,intentFilter);
    }

    private void receivedDeviceHook(Intent intent, Context context){
        ROXEventInfo eventInfo = (ROXEventInfo) intent.getSerializableExtra(ROXConsts.EXTRA_EVENT_DATA);
        this.eventHistory.add(eventInfo);
        notifyListenersOfEventHisoryUpdate();
    }

    private boolean loadCachedEvents(Context context){
        try {
            // Retrieve the list from internal storage
            this.eventHistory = (ArrayList<ROXEventInfo>) InternalStorage.readObject(context, EVENT_CACHE_KEY);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void notifyListenersOfEventHisoryUpdate(){
        for (ROXEventUpdateListener updateListener : this.eventListeners){
            updateListener.eventsDidUpdate();
        }
    }
}
