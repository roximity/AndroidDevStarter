package com.example.roximity.androiddevstarter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
}
