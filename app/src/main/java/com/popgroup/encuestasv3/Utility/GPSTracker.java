package com.popgroup.encuestasv3.Utility;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.popgroup.encuestasv3.R;

/**
 * Created by jesus.hernandez on 08/12/16.
 * clase para obtener la geolocalizacion
 */

public class GPSTracker extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 2; // 1 minute
    private final int REQUEST_PERMISSION_STATE=1;
    private final Context mContext;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    String TAG = getClass ().getName ();
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    double latitude; // latitude
    double longitude; // longitude
    private Location location; // location

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {

        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled) {
                //Log.e(TAG,"no hay provedor activo ");
                showGpsAlert();
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {

                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
               /* if (isNetworkEnabled) {
/*
                    if (ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                        }, REQUEST_PERMISSION_STATE);

                        showGpsAlert();


                    }else{*/


                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                       /* }*/
                }
                /*}*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * Function to show settings alert dialog
     */
    public void showGpsAlert () {

        final AlertDialog alertDialog = new AlertDialog.Builder (mContext).create ();
        alertDialog.setTitle ("GPS Configuracion");
        alertDialog.setMessage ("GPS no esta disponible. Debe activarlo!");
        alertDialog.setButton (AlertDialog.BUTTON_POSITIVE, "Ir a la Configuración", new DialogInterface.OnClickListener () {
            public void onClick (DialogInterface dialog, int which) {

                Intent intent = new Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity (intent);
            }
        });
        alertDialog.setOnShowListener (new DialogInterface.OnShowListener () {
            @Override
            public void onShow (DialogInterface arg0) {
                alertDialog.getButton (AlertDialog.BUTTON_POSITIVE).setTextColor (mContext.getResources ().getColor (R.color.colorPrimary));

            }
        });
        alertDialog.setCancelable (false);
        alertDialog.show ();
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

        this.location = location;
        //  Toast.makeText(mContext,"location Change"+location.getLatitude() + " - "+location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged (String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled (String provider) {

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
