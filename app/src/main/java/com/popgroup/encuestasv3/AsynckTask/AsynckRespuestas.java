package com.popgroup.encuestasv3.AsynckTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.Preguntas;
import com.popgroup.encuestasv3.Model.Respuestas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jesus.hernandez on 12/12/16.
 * descarga el catalogo de respuestas
 */
public class AsynckRespuestas extends AsyncTask<String,String,String>{
    Constantes constantes;
    Context mContext;
    Respuestas respuestas;
    ArrayList<Preguntas> arrayPreguntas;
    Dao dao;
    private String TAG = getClass().getSimpleName();
    private String URL;
    private String success = null;
    private ArrayList<NameValuePair> data;
    private String telefono;
    private DBHelper mDBHelper;
    private ICallbackAsyncktask iCallbackAsyncktask;

    public AsynckRespuestas (ICallbackAsyncktask iCallbackAsyncktask) {
        this.iCallbackAsyncktask = iCallbackAsyncktask;
    }

    @Override
    protected String doInBackground(String... strings) {
        success = null;
        constantes = new Constantes();
        URL = constantes.getIPWBService();
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("f","getRespuestas"));
        data.add (new BasicNameValuePair ("telefono", strings[0]));
        try{
            ServiceHandler jsonParser = new ServiceHandler();
            String jsonRes = jsonParser.makeServiceCall(URL, ServiceHandler.POST, data);
            JSONArray jsonArray = new JSONArray(jsonRes);

            for(int x = 0 ; x < jsonArray.length();x++ ){
                JSONObject jsonObject = new JSONObject();
                jsonObject = jsonArray.getJSONObject(x);
                try {
                    respuestas = new Respuestas();
                    respuestas.setIdPregunta(jsonObject.getInt("idPregunta"));
                    respuestas.setIdRespuesta(jsonObject.getInt("idRespuesta"));
                    respuestas.setRespuesta(jsonObject.getString("respuesta"));
                    respuestas.setSigPregunta(jsonObject.getString("sig_pregunta"));
                    respuestas.setRespLibre(jsonObject.getString("respuesta_libre"));
                    respuestas.setIdEncuesta(jsonObject.getInt("idEncuesta"));
                    dao = getmDBHelper().getRespuestasDao();
                    dao.create(respuestas);
                    success = "1";
                } catch (SQLException e) {
                    success = "0";
                    e.printStackTrace();
                    Log.d (TAG, "SqlException " + e.getMessage ());
                }

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    protected void onPreExecute () {
        super.onPreExecute ();
    }

    @Override
    protected void onPostExecute (String s) {
        super.onPostExecute (s);
        iCallbackAsyncktask.onFinish(s);
    }

    private DBHelper getmDBHelper(){
        if (mDBHelper == null){
            mDBHelper = OpenHelperManager.getHelper(mContext,DBHelper.class);
        }
        return mDBHelper;
    }
}
