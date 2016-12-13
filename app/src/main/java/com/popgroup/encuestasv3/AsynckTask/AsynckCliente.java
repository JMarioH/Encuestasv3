package com.popgroup.encuestasv3.AsynckTask;

import android.content.Context;
import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.Cliente;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


/**
 * Created by jesus.hernandez on 08/12/16.
 */

public class AsynckCliente extends AsyncTask<String, String, String> {
    private String TAG = getClass().getSimpleName();
    private String URL;
    Constantes constantes;
    private String success = null;
    private ArrayList<NameValuePair> data;
    private String telefono;
    Context mContext;
    Cliente cliente;
    private DBHelper mDBHelper;
    Dao dao;

    public AsynckCliente() {
    }

    public AsynckCliente(Context context, String telefono) {
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
        mDBHelper = OpenHelperManager.getHelper(mContext, DBHelper.class);
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("f", "getCliente"));
        data.add(new BasicNameValuePair("telefono", telefono));

        try {
            ServiceHandler jsonParser = new ServiceHandler();
            String jsonRes = jsonParser.makeServiceCall(URL, ServiceHandler.POST, data);
            JSONObject jsonObject = new JSONObject(jsonRes);
            JSONObject result = jsonObject.getJSONObject("result");
          //  success = result.getString("cliente");
            try {
                dao = getmDBHelper().getClienteDao();
                cliente = new Cliente();
                cliente.setNombre(result.getString("cliente").toString());
                dao.create(cliente);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return success = "1" ;
        } catch (JSONException e) {
            e.printStackTrace();
            return success;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    private DBHelper getmDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(mContext, DBHelper.class);
        }
        return mDBHelper;
    }


}
