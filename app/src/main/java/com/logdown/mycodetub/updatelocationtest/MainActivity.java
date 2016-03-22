package com.logdown.mycodetub.updatelocationtest;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

public class MainActivity extends Activity implements LocationListener {

    private static final String TAG = "UpdateLocationTest";
    private GmsLocationUtil mGmsLocationUtil;
    private TextView mTextView;
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();

        mGmsLocationUtil = new GmsLocationUtil(this);
    }

    private void initButtons() {
        Button startUpdateLocationBtn = (Button) findViewById(R.id.start_update_location_btn);
        startUpdateLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdateLocation();
            }
        });

        final Button stopUpdateLocationBtn = (Button) findViewById(R.id.stop_update_location_btn);
        stopUpdateLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopUpdateLocation();
            }
        });
    }

    private void stopUpdateLocation() {
        mGmsLocationUtil.stopUpdateLocation(this);
    }

    private void startUpdateLocation() {
        mGmsLocationUtil.startUpdateLocation(this);
        updateUI("Starting...");

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGmsLocationUtil.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGmsLocationUtil.isConnected() && mGmsLocationUtil.isRequestLocationUpdates()) {
            startUpdateLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopUpdateLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGmsLocationUtil.disConnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        updateUI(location);
    }

    private void updateUI(Location location) {
        Log.d(TAG, "Update location - " + location.getLatitude() + ", " + location.getLongitude() + "]");
        if (mTextView == null) {
            mTextView = (TextView) findViewById(R.id.location_message_text_view);
        }
        String message = "CurrentLocation: [" + location.getLatitude() + ", " + location.getLongitude() + "], updates: " + count++;
        mTextView.setText(message);
    }

    private void updateUI(String s) {
        if (mTextView == null) {
            mTextView = (TextView) findViewById(R.id.location_message_text_view);
        }
        mTextView.setText(s);
    }
}
