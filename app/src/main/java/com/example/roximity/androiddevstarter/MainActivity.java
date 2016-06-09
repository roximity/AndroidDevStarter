package com.example.roximity.androiddevstarter;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.roximity.sdk.ROXIMITYEngine;
import com.roximity.system.exceptions.GooglePlayServicesMissingException;
import com.roximity.system.exceptions.IncorrectContextException;
import com.roximity.system.exceptions.MissingApplicationIdException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MAIN_ACTIVITY";
    private ROXIMITYObserver roximityObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermission();
        setupROXObserver();

    }

    private void setupROXObserver(){
        try {
            this.roximityObserver = new ROXIMITYObserver(this.getApplicationContext());
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

    private  static final int PERMISSION_REQUEST_LOCATION = 55;
    private void checkLocationPermission(){


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
                    //BE SURE TO ACTIVATE LOCAITON IN THE ROXIMITY SDK ////////////
                    //ONCE THIS PERMISSION IS AQUIRED                  ////////////
                    //                                                 ////////////
                    ROXIMITYEngine.activateLocation();                 ////////////
                    ///////////////////////////////////////////////////////////////
                }
            }
        }
    }
}
