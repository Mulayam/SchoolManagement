package com.demomvvm.school.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.demomvvm.school.util.Constans;
import com.demomvvm.school.util.Preferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

/**
 * Created by Kailash Patel
 */

public class CurrentLocation extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private LocationRequest mLocationRequest;
    private static GoogleApiClient mGoogleApiClient;
    private static android.location.Location mCurrentLocation;


    public void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkLocationPermission()) {
                createLocationRequest();
                mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                mGoogleApiClient.connect();
            }

        } else {
            /*createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();*/
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        checkPermission();
        Log.d("Start","startService");
        return START_STICKY;


    }


    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            //ActivityCompat.requestPermissions(, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (mGoogleApiClient == null) {
                        createLocationRequest();
                        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
                        mGoogleApiClient.connect();
                    }

                } else {
                    Toast.makeText(CurrentLocation.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        // mLocationRequest.setNumUpdates(stra2);
        // mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }


    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        if(mGoogleApiClient.isConnected()){
            @SuppressLint("MissingPermission") PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, CurrentLocation.this);

            @SuppressLint("MissingPermission") android.location.Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            if (mLastLocation != null) {
                Log.d("CurrentLatlong==", "Lat:" + mLastLocation.getLatitude() + " Lng:" + mLastLocation.getLongitude());

                setmCurrentLocation(mLastLocation);
            }
        }



    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

        mCurrentLocation = location;

        if (null != mCurrentLocation) {
            setmCurrentLocation(mCurrentLocation);
        }
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) CurrentLocation.this);
    }


    public static android.location.Location getmCurrentLocation() {
        return mCurrentLocation;
    }

    public void setmCurrentLocation(android.location.Location mCurrentLocation) {
        this.mCurrentLocation = mCurrentLocation;
        if (mCurrentLocation != null) {

            Log.d("CurrentLatlong==", "Lat:" + mCurrentLocation.getLatitude() + " Lng:" + mCurrentLocation.getLongitude());

            Constans.CURRENT_LATITUDE=mCurrentLocation.getLatitude();
            Constans.CURRENT_LONGITUDE=mCurrentLocation.getLongitude();


            Preferences.writeString(getApplicationContext(), Preferences.SELECTED_CITY_LATITUDE,mCurrentLocation.getLatitude()+"");
            Preferences.writeString(getApplicationContext(), Preferences.SELECTED_CITY_LONGITUDE,mCurrentLocation.getLongitude()+"");


            Preferences.writeString(getApplicationContext(), Preferences.LATITUDE,  mCurrentLocation.getLatitude() + "");
            Preferences.writeString(getApplicationContext(), Preferences.LONGITUDE,  mCurrentLocation.getLongitude() + "");

            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                if(addresses.get(0).getLocality() !=null) {

                    Preferences.writeString(this, Preferences.SELECTED_CURRENT_CITY, ""+addresses.get(0).getLocality());

                }else if(addresses.get(0).getSubAdminArea() !=null){
                    // Log.d("address_found","subadmin "+addresses.get(0).getSubAdminArea());
                }else if(addresses.get(0).getAdminArea() !=null){
                    //Log.d("address_found","admin "+addresses.get(0).getAdminArea());
                }else{
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}