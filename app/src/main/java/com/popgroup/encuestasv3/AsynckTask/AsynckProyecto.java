package com.popgroup.encuestasv3.AsynckTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.Proyecto;
import com.popgroup.encuestasv3.Model.TipoEncuesta;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jesus.hernandez on 12/12/16.
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
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result");

            proyecto = new Proyecto();
            proyecto.setNombre(result.getString("proyecto"));
            proyecto.setIdproyecto(result.getString("idProyecto"));
            proyecto.setCliente(result.getString("cliente"));
            try {
                dao = getmDBHelper().getProyectoDao();
                dao.create(proyecto);
            }catch (SQLException e ){
                Log.i(TAG,"no se pudo guardar el proyecto en la base de datos",e);
                e.printStackTrace();
            }


        }catch (JSONException e ){
            Log.i(TAG,"no se pudo crear el objeto json",e);
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
