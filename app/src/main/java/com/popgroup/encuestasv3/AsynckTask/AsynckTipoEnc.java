package com.popgroup.encuestasv3.AsynckTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.TipoEncuesta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jesus.hernandez on 08/12/16.
 * descarga los tipos de encuestas
 */

public class AsynckTipoEnc  extends AsyncTask<String,String,String> {

    private String TAG= getClass().getSimpleName();
    private String URL;
    Constantes constantes;
    private String success = null;
    private ArrayList<NameValuePair> data ;
    private String telefono;
    Context mContext;
    TipoEncuesta tipoEncuesta;
    private DBHelper mDBHelper;
    ArrayList<TipoEncuesta> arrayListTipoEnc;
    Dao dao;

    public AsynckTipoEnc(Context context, String telefono) {
        this.telefono = telefono;
        this.mContext = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        success = null;
        constantes = new Constantes();
        URL = constantes.getIPWBService();
        arrayListTipoEnc = new ArrayList<>();
        mDBHelper = OpenHelperManager.getHelper(mContext,DBHelper.class);
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("f","getTipoEnc"));
        data.add(new BasicNameValuePair("telefono",telefono));
        try{
            ServiceHandler jsonParser = new ServiceHandler();
            String jsonRes = jsonParser.makeServiceCall(URL, ServiceHandler.POST, data);
            JSONArray jsonArray = new JSONArray(jsonRes);

            for(int x = 0 ; x < jsonArray.length();x++ ){
                JSONObject jsonObject = new JSONObject();
                jsonObject = jsonArray.getJSONObject(x);
                try {
                tipoEncuesta = new TipoEncuesta();
                tipoEncuesta.setEncuesta(jsonObject.getString("encuesta"));
                tipoEncuesta.setIdArchivo(jsonObject.getString("idArchivo"));
                tipoEncuesta.setIdTienda(jsonObject.getString("idTienda"));
                tipoEncuesta.setIdEncuesta(jsonObject.getString("idEncuesta"));
                tipoEncuesta.setNumerTel(jsonObject.getString("numero_tel"));
                tipoEncuesta.setNombre(jsonObject.getString("NOMBRE"));
                tipoEncuesta.setIdProyecto(jsonObject.getString("ID_PROYECTO"));
                tipoEncuesta.setIdestablecimiento(jsonObject.getString("ESTABLECIMIENTO"));
                dao = getmDBHelper().getTipoEncDao();
                dao.create(tipoEncuesta);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            return success;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        // TODO agregamos el cliente a la base de dato

    }
    private DBHelper getmDBHelper(){
        if (mDBHelper == null){
            mDBHelper = OpenHelperManager.getHelper(mContext,DBHelper.class);
        }
        return mDBHelper;
    }


}