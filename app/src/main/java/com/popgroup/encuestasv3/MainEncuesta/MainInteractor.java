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
import com.popgroup.encuestasv3.Utility.NetWorkUtil;

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
 * maininteractor
 */

public class MainInteractor extends BaseInteractor implements IMainInteractor {
    private final String TAG = getClass ().getSimpleName ();

    private IMainCallback callBack;
    private Context context;
    private Dao dao;

    public MainInteractor (Activity activity) {
        this.context = activity;
    }

    @Override
    public void getUsuario () {
        String mUsuario = "";
        List<User> arrayUser;
        try {
            dao = getmDBHelper ().getUserDao ();
            arrayUser = (List<User>) dao.queryForAll ();
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
    public void enviarEncPendientes (String mUsuario, Integer encuestasPendientes) {
        if (NetWorkUtil.checkConnection (context)) {
            new AsynckEncPendientes (context, mUsuario, encuestasPendientes, callBack).execute ();
        } else {
            callBack.onFailed (new Throwable ("Debe Conectarse a una red para poder enviar la informacion."));
        }
    }

    @Override
    public void enviarFotosPendientes () {
        JSONArray jsonFotos;
        ArrayList<NameValuePair> datosPost;
        ArrayList<Fotos> fotosPendientes;
        if (NetWorkUtil.checkConnection (context)) {
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
                        new AsyncUploadFotos (context, datosPost,
                                String.valueOf (fotosPendientes.get (x).getIdEncuesta ()),
                                String.valueOf (fotosPendientes.get (x).getIdEstablecimiento ()),
                                callBack
                        ).execute ();

                    }
                } catch (JSONException e) {
                    Log.e (TAG, "jsonE -> " + e);
                }

                if (j == fotosPendientes.size ()) {

                    dao = getmDBHelper ().getFotosDao ();
                    DeleteBuilder<Fotos, Integer> deleteBuilder = dao.deleteBuilder ();
                    deleteBuilder.delete ();
                    dao.clearObjectCache ();

                }

                dao.clearObjectCache ();
            } catch (SQLException e) {
                Log.e (TAG, "sql->" + e);
            }

        } else {
            callBack.onFailed (new Throwable ("Debe Conectarse a una red para poder enviar la informacion."));
        }
    }

    @Override
    public void validateEncPendientes () {
        List<RespuestasCuestionario> respuestas;
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
        List<Fotos> fotosPendientes;
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
    public void checkUsuario () {
        String mUsuario = "";
        ArrayList<User> arrayUser = new ArrayList<> ();
        try {
            dao = getmDBHelper ().getUserDao ();
            arrayUser = (ArrayList<User>) dao.queryForAll ();
            for (User item : arrayUser) {
                mUsuario = item.getNombre ();
            }
            if (arrayUser != null && mUsuario != "") {
                callBack.showAlertDB ();
            } else {
                callBack.nextOperation ();
            }
            dao.clearObjectCache ();

        } catch (SQLException e) {
            Log.e (TAG, "sqlException " + e);
        }
    }

    @Override
    public void clearDataBase () {
        try { // recuperamos los datos de la base de datos

            dao = getmDBHelper ().getUserDao ();
            DeleteBuilder<User, Integer> deleteBuilder = dao.deleteBuilder ();
            deleteBuilder.delete ();
            dao.clearObjectCache ();

            dao = getmDBHelper ().getUserDao ();
            dao.deleteBuilder ().delete ();
            dao.clearObjectCache ();

            dao = getmDBHelper ().getProyectoDao ();
            dao.deleteBuilder ().delete ();
            dao.clearObjectCache ();

            dao = getmDBHelper ().getClienteDao ();
            dao.deleteBuilder ().delete ();
            dao.clearObjectCache ();

            dao = getmDBHelper ().getTipoEncDao ();
            dao.deleteBuilder ().delete ();
            dao.clearObjectCache ();

            dao = getmDBHelper ().getCatMasterDao ();
            dao.deleteBuilder ().delete ();
            dao.clearObjectCache ();

            dao = getmDBHelper ().getPregutasDao ();
            dao.deleteBuilder ().delete ();
            dao.clearObjectCache ();

            dao = getmDBHelper ().getRespuestasDao ();
            dao.deleteBuilder ().delete ();
            dao.clearObjectCache ();

            dao = getmDBHelper ().getRespuestasCuestioanrioDao ();
            dao.deleteBuilder ().delete ();
            dao.clearObjectCache ();

            dao = getmDBHelper ().getFotosDao ();
            dao.deleteBuilder ().delete ();
            dao.clearObjectCache ();

            dao = getmDBHelper ().getGeosDao ();
            dao.deleteBuilder ().delete ();
            dao.clearObjectCache ();

            callBack.showUsuario (true, "00000000");

        } catch (SQLException e) {
            Log.e (TAG, "sql->" + e);
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
