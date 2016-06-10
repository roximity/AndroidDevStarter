package com.example.roximity.androiddevstarter;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.roximity.sdk.ROXIMITYEngine;
import com.roximity.sdk.messages.ROXEventInfo;
import com.roximity.system.exceptions.GooglePlayServicesMissingException;
import com.roximity.system.exceptions.IncorrectContextException;
import com.roximity.system.exceptions.MissingApplicationIdException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements ROXEventUpdateListener {

    public static final String TAG = "MAIN_ACTIVITY";
    private static final int PERMISSION_REQUEST_LOCATION = 55;

    private ROXIMITYObserver roximityObserver;
    private ListView eventHistoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup the ROXIMITYEvent listener and ensure we have propper location permissions
        setupROXObserver();
        checkLocationPermission();

        //Setup the ListView for viewing ROXIMITYEvents
        setupEventHistoryListView();
    }

    @Override
    public void eventsDidUpdate() {
        populateEventHistoryList(this.roximityObserver.eventHistory);
    }

    private void setupROXObserver() {
        try {
            this.roximityObserver = new ROXIMITYObserver(this.getApplicationContext());
            this.roximityObserver.addEventUpdateListener(this);
        } catch (GooglePlayServicesMissingException e) {
            Log.d(TAG, "Couldn't start ROXIMITY due to GooglePlayServices Error");
            e.printStackTrace();
        } catch (IncorrectContextException e) {
            Log.d(TAG, "Couldn't start ROXIMITY due to incorrect context Error");
            e.printStackTrace();
        } catch (MissingApplicationIdException e) {
            Log.d(TAG, "Couldn't start ROXIMITY due to missing application id Error");
            e.printStackTrace();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ///////////////////////////////////////////////////////////////
                    //BE SURE TO ACTIVATE LOCATION IN THE ROXIMITY SDK ////////////
                    //ONCE THIS PERMISSION IS ACQUIRED                 ////////////
                    //                                                 ////////////
                    ROXIMITYEngine.activateLocation();                 ////////////
                    ///////////////////////////////////////////////////////////////
                }
            }
        }
    }

    private void setupEventHistoryListView(){
        setContentView(R.layout.activity_main);
        wireUpListViewWithResource();
        populateEventHistoryList(this.roximityObserver.eventHistory);
    }

    private void wireUpListViewWithResource() {
        this.eventHistoryView = (ListView) findViewById(R.id.roximityEventList);
    }

    private void populateEventHistoryList(ArrayList<ROXEventInfo> eventHistory) {
        //ensure the eventHistory items are chronologically sorted
        Collections.sort(eventHistory, CHRONOLOGIC_EVENT);
        ROXIMITYEventAdapter adapter = new ROXIMITYEventAdapter(this, eventHistory);
        this.eventHistoryView.setAdapter(adapter);
    }

    private static final Comparator<ROXEventInfo> CHRONOLOGIC_EVENT = new Comparator<ROXEventInfo>() {
        public int compare(ROXEventInfo sr, ROXEventInfo sr1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return Long.compare(sr1.getTimestamp(), sr.getTimestamp());
            }
            return 0;
        }
    };

}
