package com.popgroup.encuestasv3.Model;

import android.util.Log;

/**
 * Created by jesus.hernandez on 16/12/16.
 * modelo geos
 */

public class GeoEstatica {
    private static GeoEstatica instance;
    private double sLatitud ;
    private double sLongitud ;
    private boolean mEstatus ;

    public GeoEstatica() {
    }

    public GeoEstatica(double sLatitud, double sLongitud , boolean mEstatus) {
        this.sLatitud = sLatitud;
        this.sLongitud = sLongitud;
        this.mEstatus = mEstatus;

    }

    public static synchronized GeoEstatica getInstance () {
        if (instance == null) {
            instance = new GeoEstatica ();
        }
        return instance;
    }

    public double getsLatitud() {
        return sLatitud;
    }

    public void setsLatitud(double sLatitud) {
        this.sLatitud = sLatitud;
    }

    public double getsLongitud() {
        return sLongitud;
    }

    public void setsLongitud(double sLongitud) {
        this.sLongitud = sLongitud;
    }

    public boolean ismEstatus() {
        return mEstatus;
    }

    public void setmEstatus(boolean mEstatus) {
        this.mEstatus = mEstatus;
    }

    public void reset(){
        Log.e("GeoEstatica envio","reset");
        sLatitud = 0.0 ;
        sLongitud = 0.0 ;
        mEstatus = false;
        instance = null;
    }
}
