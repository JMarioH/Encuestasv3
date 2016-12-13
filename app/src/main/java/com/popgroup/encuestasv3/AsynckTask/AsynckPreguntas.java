package com.popgroup.encuestasv3.AsynckTask;

import android.content.Context;
import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.Cliente;
import com.popgroup.encuestasv3.Model.Preguntas;
import com.popgroup.encuestasv3.Model.TipoEncuesta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jesus.hernandez on 12/12/16.
 */

public class AsynckPreguntas extends AsyncTask<String, String, String> {
    private String TAG = getClass().getSimpleName();
    private String URL;
    Constantes constantes;
    private String success = null;
    private ArrayList<NameValuePair> data;
    private String telefono;
    Context mContext;
    Preguntas preguntas;
    private DBHelper mDBHelper;
    ArrayList<Preguntas> arrayPreguntas;
    Dao dao;
    public AsynckPreguntas(Context context, String telefono) {
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
        arrayPreguntas = new ArrayList<>();
        mDBHelper = OpenHelperManager.getHelper(mContext,DBHelper.class);
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("f","getPreguntas"));
        data.add(new BasicNameValuePair("telefono",telefono));
        try{
            ServiceHandler jsonParser = new ServiceHandler();
            String jsonRes = jsonParser.makeServiceCall(URL, ServiceHandler.POST, data);
            JSONArray jsonArray = new JSONArray(jsonRes);

            for(int x = 0 ; x < jsonArray.length();x++ ){
                JSONObject jsonObject = new JSONObject();
                jsonObject = jsonArray.getJSONObject(x);
                try {
                    preguntas = new Preguntas();
                    preguntas.setIdPregunta(jsonObject.getInt("idPregunta"));
                    preguntas.setPregunta(jsonObject.getString("pregunta"));
                    preguntas.setMultiple(jsonObject.getInt("multiple"));
                    preguntas.setOrden(jsonObject.getInt("orden"));
                    preguntas.setIdEncuesta(jsonObject.getInt("idEncuesta"));

                    dao = getmDBHelper().getPregutasDao();
                    dao.create(preguntas);

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
    }
    private DBHelper getmDBHelper(){
        if (mDBHelper == null){
            mDBHelper = OpenHelperManager.getHelper(mContext,DBHelper.class);
        }
        return mDBHelper;
    }
}
