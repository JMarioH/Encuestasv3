package com.popgroup.encuestasv3.AsynckTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.FotoEncuesta;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by jesus.hernandez on 02/01/17.
 * sube las fotos
 */
public class AsyncUploadFotos extends AsyncTask<Void, Void, Boolean> {

    public ProgressDialog pDialog;
    private Context context;
    private ArrayList<NameValuePair> data;
    private String TAG = getClass ().getSimpleName ();
    private String Url = null;
    private Constantes constantes;
    private Dao dao;
    private DBHelper mDBHelper;

    private String mEncuesta;
    private String mEstablecimiento;

    public AsyncUploadFotos (Context context, ArrayList<NameValuePair> data, String encuesta, String establecimiento) {
        this.context = context;
        this.data = data;
        this.mEncuesta = encuesta;
        this.mEstablecimiento = establecimiento;
        mDBHelper = OpenHelperManager.getHelper (context, DBHelper.class);
    }

    @Override
    protected Boolean doInBackground (Void... params) {
        Boolean respuesta = false;
        constantes = new Constantes ();
        Url = constantes.getIPSetFoto ();
        ServiceHandler jsonParser = new ServiceHandler ();

        String jsonRes = jsonParser.makeServiceCall (Url, ServiceHandler.POST, data);
        try {
            JSONObject jsonObject = new JSONObject (jsonRes);
            JSONObject result = jsonObject.getJSONObject ("result");
            String success = result.getString ("success");
            if (success.equals ("1")) {
                respuesta = true;
            } else {
                respuesta = false;
            }


        } catch (JSONException e) {
            e.printStackTrace ();
        }
        return respuesta;
    }

    @Override
    protected void onPreExecute () {
        super.onPreExecute ();
        pDialog = new ProgressDialog (context);
        pDialog.setProgressStyle (ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage ("Enviando Fotos ");
        pDialog.setIndeterminate (false);
        pDialog.setCancelable (false);
        pDialog.show ();
    }

    @Override
    protected void onPostExecute (Boolean s) {
        super.onPostExecute (s);
        pDialog.hide ();
        pDialog.dismiss ();

        try {
            dao = getmDBHelper ().getFotosDao ();
            DeleteBuilder<FotoEncuesta, Integer> deleteBuilder = dao.deleteBuilder ();
            deleteBuilder.where ().eq ("idEncuesta", mEncuesta)
                    .and ().eq ("idEstablecimiento", mEstablecimiento);
            deleteBuilder.delete ();
            dao.clearObjectCache ();
        } catch (SQLException e) {
            Log.e (TAG, "Exception " + e.getMessage ());
        }
        if (s) {
            Toast.makeText (context, "Fotos enviadas correctamente", Toast.LENGTH_SHORT).show ();
        }

    }

    private DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper (context, DBHelper.class);
        }
        return mDBHelper;
    }

    private void borrarFotos () {

    }
}

