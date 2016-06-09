package com.example.roximity.androiddevstarter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.roximity.sdk.ROXIMITYEngine;
import com.roximity.sdk.ROXIMITYEngineListener;
import com.roximity.sdk.external.ROXConsts;
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
}
