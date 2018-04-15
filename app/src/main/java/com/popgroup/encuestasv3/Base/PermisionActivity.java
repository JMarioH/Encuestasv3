package com.popgroup.encuestasv3.Base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orion on 15/04/2018.
 */

public abstract class PermisionActivity extends BaseActivity {
    private static final int MULTIPLE_PERMISSIONS = 834;
    private static String TAG = PermisionActivity.class.getSimpleName ();
    private String[] permissions;

    public boolean checkPermissions () {
        int result;
        permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA
        };
        List<String> listPermissionsNeeded = new ArrayList<> ();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission (getApplicationContext (), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add (p);
            }
        }
        if (!listPermissionsNeeded.isEmpty ()) {
            ActivityCompat.requestPermissions (this, listPermissionsNeeded.toArray (new String[listPermissionsNeeded.size ()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d (TAG, "requestCode :" + requestCode);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d (TAG, "Permisos otorgados");
                    onSuccesPermissions (true);
                } else {
                    Log.d (TAG, "Permisos no otorgados");
                    onSuccesPermissions (false);
                }
                return;
            }
        }
    }

    protected abstract void onSuccesPermissions (boolean result);
}
