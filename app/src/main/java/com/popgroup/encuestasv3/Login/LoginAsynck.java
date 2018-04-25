package com.popgroup.encuestasv3.Login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.AppEnc;
import com.popgroup.encuestasv3.AsynckTask.AsynckCatMaster;
import com.popgroup.encuestasv3.AsynckTask.AsynckCliente;
import com.popgroup.encuestasv3.AsynckTask.AsynckPreguntas;
import com.popgroup.encuestasv3.AsynckTask.AsynckProyecto;
import com.popgroup.encuestasv3.AsynckTask.AsynckRespuestas;
import com.popgroup.encuestasv3.AsynckTask.AsynckTipoEnc;
import com.popgroup.encuestasv3.AsynckTask.Constantes;
import com.popgroup.encuestasv3.AsynckTask.ServiceHandler;
import com.popgroup.encuestasv3.Base.ICallBack;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Orion on 14/04/2018.
 */
public class LoginAsynck extends AsyncTask<String, String, String> {
    String success = "0";
    private String TAG = LoginAsynck.class.getSimpleName ();
    private String usuario;
    private String password;
    private Context context;
    private ICallBack iCallBack;
    private ArrayList<NameValuePair> data;
    private Dao dao;
    private DBHelper dbHelper;


    public LoginAsynck (Context ctx, String user, String password, ICallBack mCallBack, Dao dao, DBHelper dbHelper) {
        this.context = ctx;
        this.usuario = user;
        this.password = password;
        this.iCallBack = mCallBack;
        this.dao = dao;
        this.dbHelper = dbHelper;
    }

    @SuppressWarnings ("WrongThread")
    @Override
    protected String doInBackground (String... params) {

        data = new ArrayList<> ();
        data.add (new BasicNameValuePair ("f", "login"));
        data.add (new BasicNameValuePair ("usuario", usuario));
        data.add (new BasicNameValuePair ("password", password));

        if (usuario.equals ("00001")) {
            success = "2";
        } else {
            try {
                ServiceHandler jsonParser = new ServiceHandler ();
                String jsonRes = jsonParser.makeServiceCall (Constantes.getIPWBService (), ServiceHandler.POST, data);
                JSONObject jsonObject = new JSONObject (jsonRes);
                JSONObject result = jsonObject.getJSONObject ("result");
                success = result.getString ("logstatus").toString ();

            } catch (JSONException e) {
                Log.e (TAG, "JSONException" + e);
            }
        }
        return success;
    }

    @Override
    protected void onPreExecute () {
        super.onPreExecute ();
    }

    @Override
    protected void onPostExecute (String res) {
        super.onPostExecute (res);


        if (res.equals ("1")) {

            try {
                dao = dbHelper.getUserDao ();
                User user = new User ();
                user.setNombre (usuario);
                dao.create (user);
            } catch (SQLException e) {
                Log.e (TAG, "sql" + e);
            }

            new AsynckCliente ().executeOnExecutor (AppEnc.getInstance ().getExecutor (), usuario);
            new AsynckProyecto ().executeOnExecutor (AppEnc.getInstance ().getExecutor (), usuario);
            new AsynckTipoEnc ().executeOnExecutor (AppEnc.getInstance ().getExecutor (), usuario);
            new AsynckCatMaster ().executeOnExecutor (AppEnc.getInstance ().getExecutor (), usuario);
            new AsynckPreguntas ().executeOnExecutor (AppEnc.getInstance ().getExecutor (), usuario);
            new AsynckRespuestas ().executeOnExecutor (AppEnc.getInstance ().getExecutor (), usuario);
            iCallBack.onSuccess (res);
        } else {
            iCallBack.onFailed (new Throwable ("Usuario no existente"));
            ;
        }

    }
}
