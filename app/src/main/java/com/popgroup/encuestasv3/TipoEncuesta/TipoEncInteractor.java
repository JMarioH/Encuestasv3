package com.popgroup.encuestasv3.TipoEncuesta;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.ICallBack;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.TipoEncuesta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orion on 22/04/2018.
 * TipoEncuesta interactor
 */

class TipoEncInteractor extends BaseInteractor implements ITipoEncInteractor {
    private final static String TAG = TipoEncInteractor.class.getSimpleName ();
    private ICallBack mCallback;
    private Context context;
    private Dao dao;

    public TipoEncInteractor (Context ctx) {
        this.context = ctx;
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
        this.mCallback = callBack;
    }

    @Override
    protected DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper (context, DBHelper.class);
        }
        return mDBHelper;
    }

    @Override
    public List<TipoEncuesta> getListNomEnc (String usuario, String idProyecto) {
        ArrayList<TipoEncuesta> arrayTipoEnc = null;
        try {
            dao = getmDBHelper ().getTipoEncDao ();
            arrayTipoEnc = (ArrayList<TipoEncuesta>) dao.queryBuilder ().distinct ()
                    .selectColumns (TipoEncuesta.ENCUESTA)
                    .where ().eq (TipoEncuesta.NUMERO_TEL, usuario)
                    .and ().eq (TipoEncuesta.IDPROYECTO, idProyecto).query ();

            dao.clearObjectCache ();
        } catch (SQLException e) {
            Log.i (TAG, "error", e);
        }
        return arrayTipoEnc;
    }

    @Override
    public List<TipoEncuesta> getListTipoEnc (String usuario, String idProyecto, String encuesta) {
        ArrayList<TipoEncuesta> arrayTipoEnc = null;
        try {
            dao = getmDBHelper ().getTipoEncDao ();
            arrayTipoEnc = (ArrayList<TipoEncuesta>) dao.queryBuilder ().distinct ()
                    .selectColumns ("idArchivo", "idEncuesta", "idTienda")
                    .where ().eq ("numero_tel", usuario)
                    .and ().eq ("idProyecto", idProyecto)
                    .and ().eq ("encuesta", encuesta).query ();

            dao.clearObjectCache ();
        } catch (SQLException e) {
            Log.i (TAG, "error", e);
        }
        return arrayTipoEnc;
    }
}
