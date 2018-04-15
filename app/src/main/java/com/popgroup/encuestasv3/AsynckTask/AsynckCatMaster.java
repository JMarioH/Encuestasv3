package com.popgroup.encuestasv3.AsynckTask;

import android.content.Context;
import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.CatMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jesus.hernandez on 12/12/16.
 * descarga los datos del catmaster
 */

public class AsynckCatMaster extends AsyncTask<String,String,String>{

    Constantes constantes;
    Context mContext;
    CatMaster catMaster;
    ArrayList<CatMaster> arrayCatMaster;
    Dao dao;
    private String TAG= getClass().getSimpleName();
    private String URL;
    private String success = null;
    private ArrayList<NameValuePair> data ;
    private String telefono;
    private DBHelper mDBHelper;

    @Override
    protected String doInBackground(String... strings) {
        success = null;
        constantes = new Constantes();
        URL = constantes.getIPWBService();
        arrayCatMaster = new ArrayList<>();
        mDBHelper = OpenHelperManager.getHelper(mContext,DBHelper.class);
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("f","getCatMaster"));
        data.add (new BasicNameValuePair ("telefono", strings[0]));
        try{
            ServiceHandler jsonParser = new ServiceHandler();
            String jsonRes = jsonParser.makeServiceCall(URL, ServiceHandler.POST, data);

            JSONArray jsonArray = new JSONArray(jsonRes);

            for(int x = 0 ; x < jsonArray.length();x++ ){
                JSONObject jsonObject = new JSONObject();
                jsonObject = jsonArray.getJSONObject(x);
                try {
                    catMaster = new CatMaster();
                    catMaster.setIdTienda(jsonObject.getString("idTienda"));
                    catMaster.setNombre(jsonObject.getString("nombre"));
                    catMaster.setIdArchivo(jsonObject.getString("idArchivo"));
                    catMaster.setIdEncuesta(jsonObject.getString("idEncuesta"));
                    catMaster.setFlag(true);
                    dao = getmDBHelper().getCatMasterDao();
                    dao.create(catMaster);

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
    protected void onPreExecute () {
        super.onPreExecute ();
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
