package com.popgroup.encuestasv3.FinEncuesta;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.popgroup.encuestasv3.AsynckTask.AsynckEncuestas;
import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.ICallBack;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.CatMaster;
import com.popgroup.encuestasv3.Model.FotoEncuesta;
import com.popgroup.encuestasv3.Model.Fotos;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by JMario. on 16/4/2018.
 */

public class FinEncuestaInteractor extends BaseInteractor implements IFinEncuestaInter {
    private final String TAG = getClass ().getSimpleName ();
    private Context ctx;
    private Dao dao;
    private ICallBack iCallBack;
    public FinEncuestaInteractor (Activity activity) {
        this.ctx = activity;

    }

    @Override
    public void enviarEnc (String idEncuesta, String idEstablecimiento, String idTienda, String usuario) {
        //TODO Revisar Este proceso a detalle
        new AsynckEncuestas (ctx, idEncuesta, idEstablecimiento, idTienda, usuario, iCallBack).execute ();
    }

    @Override
    public void saveEncLocal (String idEstablecimiento, String idEncuesta) {
        try {
            // cambiamos el status de la lista de encuestas
            dao = getmDBHelper ().getCatMasterDao ();
            UpdateBuilder<CatMaster, Integer> updateBuilder = dao.updateBuilder ();
            updateBuilder.updateColumnValue ("flag", false);
            updateBuilder.where ().eq ("idTienda", idEstablecimiento).and ().eq ("idEncuesta", idEncuesta);
            updateBuilder.update ();
            dao.clearObjectCache ();
            //cambiamos el estatus de las respuestas
            dao = getmDBHelper ().getRespuestasCuestioanrioDao ();
            UpdateBuilder<RespuestasCuestionario, Integer> updateRespuestas = dao.updateBuilder ();
            updateRespuestas.updateColumnValue ("flag", true);
            updateRespuestas.where ().eq ("idEstablecimiento", idEstablecimiento).and ().eq ("idEncuesta", idEncuesta);
            updateRespuestas.update ();
            dao.clearObjectCache ();


        } catch (SQLException e) {
            Log.e (TAG, "SQlException Respuestas Local" + e.getMessage ());
        }
    }

    @Override
    public void saveFotos (FotoEncuesta fotoEncuesta, String idEstablecimiento, String idEncuesta) {
        int x = 0;
        String base64 = "";
        ArrayList<String> arrayBase64 = new ArrayList<> ();
        if (fotoEncuesta.getArrayFotos () != null && fotoEncuesta.getArrayFotos ().size () > 0) {
            arrayBase64 = fotoEncuesta.getArrayFotos ();
            try {
                dao = getmDBHelper ().getFotosDao ();
                for (x = 0; x < arrayBase64.size (); x++) {
                    Fotos Objfotos = new Fotos ();
                    base64 = fotoEncuesta.getArrayFotos ().get (x);
                    Objfotos.setIdEstablecimiento (Integer.parseInt (idEstablecimiento));
                    Objfotos.setIdEncuesta (Integer.parseInt (idEncuesta));
                    Objfotos.setNombre (fotoEncuesta.getNombre ().get (x));
                    Objfotos.setBase64 (base64);
                    dao.create (Objfotos);
                }
                dao.clearObjectCache ();
            } catch (SQLException e) {
                Log.e (TAG, "SQLException FotoEncuesta " + e.getMessage ());
            }

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
        this.iCallBack = callBack;
    }

    @Override
    protected DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(ctx, DBHelper.class);
        }
        return mDBHelper;
    }
}
