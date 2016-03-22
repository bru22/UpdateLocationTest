package com.logdown.mycodetub.updatelocationtest;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
public class GmsLocationUtilTest {

    private GmsLocationUtil gmsLocationUtil;
    private GoogleApiClient mMockGoogleApiClient;

    @Before
    public void setUp() throws Exception {
        Context mockAppContext = mock(Context.class);
        Context mockContext = mock(Context.class);
        when(mockContext.getApplicationContext()).thenReturn(mockAppContext);
        mMockGoogleApiClient = mock(GoogleApiClient.class);

        gmsLocationUtil = new GmsLocationUtil(mockContext, mMockGoogleApiClient);
    }

    private Location createMockLocation(double latitude, double longitude) {
        Location location = mock(Location.class);
        when(location.getLatitude()).thenReturn(latitude);
        when(location.getLongitude()).thenReturn(longitude);
        return location;
    }

    @Test
    public void testStartUpdateLocations() throws Exception {
        LocationListener mockListener = mock(LocationListener.class);

        Location mockLocation1 = createMockLocation(23.2493225, 125.9144712);
        Location mockLocation2 = createMockLocation(22.9486832, 123.294232);
        Location mockLocation3 = createMockLocation(22.9486832, 123.294232);

        // act
        gmsLocationUtil.startUpdateLocation(mockListener);
        // --> simulate callback from gms location service
        gmsLocationUtil.onLocationChanged(mockLocation1);
        gmsLocationUtil.onLocationChanged(mockLocation2);
        gmsLocationUtil.onLocationChanged(mockLocation3);

        // verify
        verify(mockListener).onLocationChanged(mockLocation1);
        verify(mockListener).onLocationChanged(mockLocation2);
        verify(mockListener).onLocationChanged(mockLocation3);
    }

    @Test
    public void testStopUpdateLocations() throws Exception {
        LocationListener mockListener = mock(LocationListener.class);

        Location mockLocation1 = createMockLocation(23.2493225, 125.9144712);
        Location mockLocation2 = createMockLocation(22.9486832, 123.294232);
        Location mockLocation3 = createMockLocation(22.9486832, 123.294232);

        // act
        gmsLocationUtil.startUpdateLocation(mockListener);
        gmsLocationUtil.stopUpdateLocation(mockListener);

        // --> simulate callback from gms location service
        gmsLocationUtil.onLocationChanged(mockLocation1);
        gmsLocationUtil.onLocationChanged(mockLocation2);
        gmsLocationUtil.onLocationChanged(mockLocation3);

        // verify
        verify(mockListener, times(0)).onLocationChanged(mockLocation1);
        verify(mockListener, times(0)).onLocationChanged(mockLocation2);
        verify(mockListener, times(0)).onLocationChanged(mockLocation3);
    }

    @Test
    public void testStopUpdateLocations_GoogleApiConnect_NeedToDisconnect() throws Exception {
        gmsLocationUtil.disConnect();
        verify(mMockGoogleApiClient).disconnect();
    }

    @Test
    public void testSTartUpdateLocations_ButNotConnectedYet_NeedToRestart() throws Exception {
        LocationListener mockListener = mock(LocationListener.class);

        gmsLocationUtil.startUpdateLocation(mockListener);
        gmsLocationUtil.onConnected(null);

        Location mockLocation = createMockLocation(24.9757391, 121.328004);
        gmsLocationUtil.onLocationChanged(mockLocation);

        verify(mockListener).onLocationChanged(mockLocation);
    }
}