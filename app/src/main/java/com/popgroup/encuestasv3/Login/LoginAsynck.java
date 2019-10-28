package com.popgroup.encuestasv3.Login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.AsynckTask.AsynckCatMaster;
import com.popgroup.encuestasv3.AsynckTask.AsynckCliente;
import com.popgroup.encuestasv3.AsynckTask.AsynckPreguntas;
import com.popgroup.encuestasv3.AsynckTask.AsynckProyecto;
import com.popgroup.encuestasv3.AsynckTask.AsynckRespuestas;
import com.popgroup.encuestasv3.AsynckTask.AsynckTipoEnc;
import com.popgroup.encuestasv3.AsynckTask.Constantes;
import com.popgroup.encuestasv3.AsynckTask.ICallbackAsyncktask;
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

    public static Boolean bandera = false;
    private String TAG = LoginAsynck.class.getSimpleName();
    private String usuario;
    private String password;
    private Context context;
    private ICallBack iCallBack;
    private ArrayList<NameValuePair> data;
    private Dao dao;
    private DBHelper dbHelper;

    public LoginAsynck(Context ctx, String user, String password, ICallBack mCallBack, Dao dao, DBHelper dbHelper) {
        this.context = ctx;
        this.usuario = user;
        this.password = password;
        this.iCallBack = mCallBack;
        this.dao = dao;
        this.dbHelper = dbHelper;
    }

    @SuppressWarnings("WrongThread")
    @Override
    protected String doInBackground(String... params) {
        String success = "0";
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("f", "login"));
        data.add(new BasicNameValuePair("usuario", usuario));
        data.add(new BasicNameValuePair("password", password));

        if (usuario.equals("00001")) {
            success = "2";
        } else {
            try {
                ServiceHandler jsonParser = new ServiceHandler();
                String jsonRes = jsonParser.makeServiceCall(Constantes.getIPWBService(), ServiceHandler.POST, data);
                if (jsonRes != null) {
                    JSONObject jsonObject = new JSONObject(jsonRes);
                    JSONObject result = jsonObject.getJSONObject("result");
                    success = result.getString("logstatus").toString();

                }else{
                    success = "-1";
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSONException" + e);
                success = "0";
            }
        }
        return success;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final String res) {
        super.onPostExecute(res);


        if (res.equals("1")) {

            try {
                dao = dbHelper.getUserDao();
                User user = new User();
                user.setNombre(usuario);
                dao.create(user);
            } catch (SQLException e) {
                Log.e(TAG, "SQLEXception" + e);
            }
            new AsynckCliente(new ICallbackAsyncktask() {
                @Override
                public void onFinish(String result) {
                    if ("1".equals(result)) {
                        new AsynckProyecto(new ICallbackAsyncktask() {
                            @Override
                            public void onFinish(String result) {
                                if ("1".equals(result)) {
                                    new AsynckTipoEnc(new ICallbackAsyncktask() {
                                        @Override
                                        public void onFinish(String result) {
                                            if ("1".equals(result)) {
                                                new AsynckCatMaster(new ICallbackAsyncktask() {
                                                    @Override
                                                    public void onFinish(String result) {
                                                        if ("1".equals(result)) {
                                                            new AsynckPreguntas(new ICallbackAsyncktask() {
                                                                @Override
                                                                public void onFinish(String result) {
                                                                    if ("1".equals(result)) {
                                                                        new AsynckRespuestas(new ICallbackAsyncktask() {
                                                                            @Override
                                                                            public void onFinish(String result) {
                                                                                if ("1".equals(result)) {

                                                                                    iCallBack.onSuccess(res);
                                                                                }
                                                                            }
                                                                        }).execute(usuario);

                                                                    }
                                                                }
                                                            }).execute(usuario);
                                                        }
                                                    }
                                                }).execute(usuario);

                                            }
                                        }
                                    }).execute(usuario);
                                }
                            }
                        }).execute(usuario);
                    }
                }
            }).execute(usuario);

        } else if(res.equals("-1")){

            iCallBack.onFailed(new Throwable("Servicio no disponible"));

        }else{
            iCallBack.onFailed(new Throwable("Usuario no existente"));

        }

    }
}
