package com.popgroup.encuestasv3.Cuestionario;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.ICallBack;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.GeoEstatica;
import com.popgroup.encuestasv3.Model.GeoLocalizacion;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;
import com.popgroup.encuestasv3.Utility.GPSTracker;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CuestionarioInteractor extends BaseInteractor implements ICuestionarioInteractor {

    private final String TAG = getClass ().getSimpleName ();
    private Context context;
    private Dao dao;

    public CuestionarioInteractor (Activity ctx) {
        this.context = ctx;
    }

    @Override
    public void SetGeo (String idEncuesta, String idEstablecimiento) {
        double longitud = 0.0, latitud = 0.0;
        GPSTracker gpsTracker = new GPSTracker (context);
        GeoEstatica geoEstatica = new GeoEstatica ().getInstance ();
        longitud = gpsTracker.getLongitude ();
        latitud = gpsTracker.getLatitude ();

        DateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        Date dt = new Date ();
        String fecha = dateFormat.format (dt);
        if (!geoEstatica.ismEstatus ()) {
            GeoLocalizacion geoLocalizacion;
            if (longitud != 0.0 && latitud != 0.0) {
                geoEstatica.setmEstatus (true);
                geoEstatica.setsLatitud (gpsTracker.getLatitude ());
                geoEstatica.setsLongitud (gpsTracker.getLongitude ());
                // guardamos la geolocalizacion en la base de datos
                try {
                    dao = getmDBHelper ().getGeosDao ();
                    geoLocalizacion = new GeoLocalizacion ();
                    geoLocalizacion.setFecha (fecha);
                    geoLocalizacion.setIdEncuesta (Integer.parseInt (idEncuesta));
                    geoLocalizacion.setIdTienda (idEstablecimiento);
                    geoLocalizacion.setLatitud (String.valueOf (latitud));
                    geoLocalizacion.setLongitud (String.valueOf (longitud));
                    dao.create (geoLocalizacion);
                    dao.clearObjectCache ();

                } catch (SQLException e) {
                    Log.e (TAG, "SQLException Geos " + e.getMessage ());
                }
            }
        }
    }

    @Override
    public void SaveRespuesta (RespuestasCuestionario respuestasCuestionario) {
        try {
            dao = getmDBHelper ().getRespuestasCuestioanrioDao ();
            dao.create (respuestasCuestionario);
            dao.clearObjectCache ();
        } catch (SQLException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper ();
            mDBHelper = null;
        }
    }

    @Override
    public void attachCallBack (ICallBack callBack) {

    }

    @Override
    protected DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper (context, DBHelper.class);
        }
        return mDBHelper;
    }
}
