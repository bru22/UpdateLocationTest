package com.logdown.mycodetub.updatelocationtest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

import static android.Manifest.permission.*;

public class MainActivity extends Activity implements LocationListener {

    private static final String TAG = "UpdateLocationTest";
    private GmsLocationUtil mGmsLocationUtil;
    private TextView mTextView;
    private int count = 1;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mLocationPermitGranted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }else {

            mLocationPermitGranted = true;
        }

        mGmsLocationUtil = new GmsLocationUtil(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermitGranted = true;
//                    mGmsLocationUtil.connect();
//                    if (mGmsLocationUtil.isConnected() && mGmsLocationUtil.isRequestLocationUpdates()) {
//                        startUpdateLocation();
//                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    mLocationPermitGranted = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void initButtons() {
        Button startUpdateLocationBtn = (Button) findViewById(R.id.start_update_location_btn);
        startUpdateLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLocationPermitGranted) {
                    mGmsLocationUtil.connect();
                    if (mGmsLocationUtil.isConnected() && mGmsLocationUtil.isRequestLocationUpdates()) {
                        startUpdateLocation();
                    }
                }
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mLocationPermitGranted) {
            if (mGmsLocationUtil.isConnected() && mGmsLocationUtil.isRequestLocationUpdates()) {
                startUpdateLocation();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationPermitGranted) {
            stopUpdateLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocationPermitGranted) {
            mGmsLocationUtil.disConnect();
        }
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
