package com.popgroup.encuestasv3.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Orion on 19/04/2018.
 */

public class NetWorkUtil {

    public static boolean checkConnection (Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo ();

        if (activeNetworkInfo != null) { // connected to the internet
            //  Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if (activeNetworkInfo.getType () == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType () == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }
}
