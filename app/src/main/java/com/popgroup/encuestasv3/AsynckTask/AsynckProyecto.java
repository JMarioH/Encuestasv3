package com.popgroup.encuestasv3.AsynckTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.Proyecto;
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
 * descar el proyecto
 */

public class AsynckProyecto extends AsyncTask<String,String,String> {

    private String TAG= getClass().getSimpleName();
    private String URL;
    Constantes constantes;
    private String success = null;
    private ArrayList<NameValuePair> data ;
    private String telefono;
    Context mContext;
    Proyecto proyecto;
    private DBHelper mDBHelper;
    ArrayList<TipoEncuesta> arrayListTipoEnc;
    Dao dao;

    public AsynckProyecto(Context context, String telefono) {
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
        mDBHelper = OpenHelperManager.getHelper(mContext,DBHelper.class);
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("f","getProyecto"));
        data.add(new BasicNameValuePair("telefono",telefono));

        try{
            ServiceHandler serviceHandler = new ServiceHandler();
            String response = serviceHandler.makeServiceCall(URL,ServiceHandler.POST,data);

            JSONArray jsonArray = new JSONArray(response);

            for(int x = 0 ; x < jsonArray.length();x++ ){
                JSONObject jsonObject = new JSONObject();
                jsonObject = jsonArray.getJSONObject(x);
                proyecto = new Proyecto();
                proyecto.setNombre(jsonObject.getString("proyecto"));
                proyecto.setIdproyecto(jsonObject.getString("idProyecto"));
                proyecto.setCliente(jsonObject.getString("cliente"));
                try {
                    dao = getmDBHelper().getProyectoDao();
                    dao.create(proyecto);
                }catch (SQLException e ){

                    e.printStackTrace();
                }
            }


        }catch (JSONException e ){

            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
    private DBHelper getmDBHelper(){
        if (mDBHelper == null){
            mDBHelper = OpenHelperManager.getHelper(mContext,DBHelper.class);
        }
        return mDBHelper;
    }

}
