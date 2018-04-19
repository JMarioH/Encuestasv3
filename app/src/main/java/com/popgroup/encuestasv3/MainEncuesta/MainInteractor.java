package com.popgroup.encuestasv3.MainEncuesta;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.popgroup.encuestasv3.AsynckTask.AsyncUploadFotos;
import com.popgroup.encuestasv3.AsynckTask.AsynckEncPendientes;
import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.ICallBack;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.Fotos;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;
import com.popgroup.encuestasv3.Model.User;
import com.popgroup.encuestasv3.Utility.Connectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by JMario. on 18/4/2018.
 */

public class MainInteractor extends BaseInteractor implements IMainInteractor {
    private final String TAG = getClass ().getSimpleName ();

    private IMainCallback callBack;
    private Context context;
    private Dao dao;
    private boolean isConnected;


    public MainInteractor (Activity activity) {

        this.context = activity;
        isConnected = Connectivity.isConnected (context);
    }

    @Override
    public void getUsuario () {
        String mUsuario = "";
        List<User> arrayUser = new ArrayList<> ();
        try {
            dao = getmDBHelper ().getUserDao ();
            arrayUser = (ArrayList<User>) dao.queryForAll ();
            for (User item : arrayUser) {
                mUsuario = item.getNombre ();
            }
            if (mUsuario != null) {
                callBack.showUsuario (true, mUsuario);
            } else {
                callBack.showUsuario (true, "0000000000");
            }
        } catch (SQLException e) {
            Log.e (TAG, "SQlException" + e.getMessage ());
        }
    }

    @Override
    public void enviarEncPendientes (String mUsuario, ArrayList<RespuestasCuestionario> encuestasPendientes) {
        if (isConnected) {
            new AsynckEncPendientes (context, mUsuario, encuestasPendientes.size ()).execute ();
        } else {
            callBack.onFailed (new Throwable ("Debe Conectarse a una red para poder enviar la informacion."));
        }
    }

    @Override
    public void enviarFotosPendientes () {
        JSONArray jsonFotos;
        ArrayList<NameValuePair> datosPost = null;
        ArrayList<Fotos> fotosPendientes;
        if (isConnected) {
            try {
                dao = getmDBHelper ().getFotosDao ();
                fotosPendientes = (ArrayList<Fotos>) dao.queryForAll ();

                String nomArchivo;
                int j = 0;
                jsonFotos = new JSONArray ();
                try {
                    for (int x = 0; x < fotosPendientes.size (); x++) {
                        datosPost = new ArrayList<> ();
                        JSONObject jsonFoto = new JSONObject ();

                        nomArchivo = fotosPendientes.get (x).getIdEncuesta () + "_" + fotosPendientes.get (x).getIdEstablecimiento () + "_" + fotosPendientes.get (x).getNombre () + "_" + x + ".jpg";
                        jsonFoto.put ("idEstablecimiento", fotosPendientes.get (x).getIdEstablecimiento ());
                        jsonFoto.put ("idEncuesta", fotosPendientes.get (x).getIdEncuesta ());
                        jsonFoto.put ("nombreFoto", nomArchivo);
                        jsonFoto.put ("base64", fotosPendientes.get (x).getBase64 ());
                        jsonFotos.put (jsonFoto);
                        j++;
                        datosPost.add (new BasicNameValuePair ("subeFotos", jsonFotos.toString ()));
                        new AsyncUploadFotos (context, datosPost, String.valueOf (fotosPendientes.get (x).getIdEncuesta ()), String.valueOf (fotosPendientes.get (x).getIdEstablecimiento ())).execute ();
                    }
                } catch (JSONException e) {
                    Log.e (TAG, "jsonE -> " + e);
                }

                if (j == fotosPendientes.size ()) {

                    dao = getmDBHelper ().getFotosDao ();
                    DeleteBuilder<Fotos, Integer> deleteBuilder = dao.deleteBuilder ();
                    deleteBuilder.delete ();
                    dao.clearObjectCache ();

                    //btnFotosPendientes.setVisibility(View.GONE);
                }

                dao.clearObjectCache ();
            } catch (SQLException e) {
                Log.e (TAG, "sql->" + e);
            }

        } else {
            // showMessage();
        }
    }

    @Override
    public void validateEncPendientes () {
        List<RespuestasCuestionario> respuestas = new ArrayList<> ();
        try {
            dao = getmDBHelper ().getRespuestasCuestioanrioDao ();
            respuestas = (ArrayList<RespuestasCuestionario>) dao.queryBuilder ().distinct ()
                    .selectColumns ("idEstablecimiento").where ().eq ("flag", true).query ();
            if (respuestas != null && respuestas.size () > 0) {
                callBack.showBtnEncPendientes (true, respuestas.size ());
            }
            dao.clearObjectCache ();
        } catch (SQLException e) {
            Log.e (TAG, "SQLException " + e.getMessage ());
        }
    }

    @Override
    public void validateFotosPendientes () {
        List<Fotos> fotosPendientes = new ArrayList<> ();
        try {

            dao = getmDBHelper ().getFotosDao ();
            fotosPendientes = (ArrayList<Fotos>) dao.queryForAll ();
            if (fotosPendientes != null && fotosPendientes.size () > 0) {
                callBack.showBtnFotoPendientes (true, fotosPendientes.size ());
            }
            dao.clearObjectCache ();
        } catch (SQLException e) {
            Log.e (TAG, "SQLException " + e.getMessage ());
        }
    }

    @Override
    public void onDestroy () {
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper ();
            mDBHelper = null;
        }
    }

    @Override
    public void attachCallBack (ICallBack callBack) {
        this.callBack = (IMainCallback) callBack;
    }

    @Override
    protected DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper (context, DBHelper.class);
        }
        return mDBHelper;
    }
}
