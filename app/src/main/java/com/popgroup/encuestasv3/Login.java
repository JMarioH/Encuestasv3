package com.popgroup.encuestasv3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.AsynckTask.AsynckCatMaster;
import com.popgroup.encuestasv3.AsynckTask.AsynckCliente;
import com.popgroup.encuestasv3.AsynckTask.AsynckPreguntas;
import com.popgroup.encuestasv3.AsynckTask.AsynckProyecto;
import com.popgroup.encuestasv3.AsynckTask.AsynckRespuestas;
import com.popgroup.encuestasv3.AsynckTask.AsynckTipoEnc;
import com.popgroup.encuestasv3.AsynckTask.Constantes;
import com.popgroup.encuestasv3.AsynckTask.ServiceHandler;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.User;
import com.popgroup.encuestasv3.Utility.Connectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jesus.hernandez on 07/12/16.
 */

public class Login extends AppCompatActivity{
    String TAG = getClass().getSimpleName();
    ProgressDialog pDialog;
    EditText editUser,editPassword;
    Button btnLogin;
    Bundle bundle;
    public String usuario ;
    public String password;
    public String URL;
    ArrayList<NameValuePair> data;
    Constantes constantes;
    DBHelper mDBHelper;
    Dao dao;
    Connectivity connectivity;
    Boolean conectionAvailable = false;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        constantes = new Constantes();
        bundle = new Bundle();
        connectivity = new Connectivity();
        user = new User();
        try {

            dao = getmDBHelper().getUserDao();
            user = (User) dao.queryForId(1);
            dao.clearObjectCache();

        }catch (SQLException e){
            e.printStackTrace();
        }
        if(user!=null){
            Intent i = new Intent(this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        URL = constantes.getIPWBService();
        editUser = (EditText) findViewById(R.id.editUser);
        editPassword = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        usuario = editUser.getText().toString();
        password = editPassword.getText().toString();



        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                connectivity = new Connectivity();
                conectionAvailable = connectivity.isConnected(getBaseContext());
                if(conectionAvailable) {
                    new LoginAsynck().execute();
                }else{
                    Toast.makeText(Login.this,"Debe conectarse a un red primero!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public class LoginAsynck extends AsyncTask<String,String,String> {
        String success= "0";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Descargando Datos. . .");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            pDialog.dismiss();
            pDialog.hide();
            if(res.equals("1")){
                try {
                    dao = getmDBHelper().getUserDao();
                    User user = new User();
                    user.setNombre(usuario);
                    dao.create(user);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Login Exitoso",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }else{
                Toast.makeText(Login.this,"Datos incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
        @SuppressWarnings("WrongThread")
        @Override
        protected String doInBackground(String... params) {

            data = new ArrayList<>();
            data.add(new BasicNameValuePair("f", "login"));
            data.add(new BasicNameValuePair("usuario", usuario));
            data.add(new BasicNameValuePair("password", password));
            try{
                ServiceHandler jsonParser = new ServiceHandler();
                String jsonRes = jsonParser.makeServiceCall(URL, ServiceHandler.POST, data);
                JSONObject jsonObject = new JSONObject(jsonRes);
                JSONObject result = jsonObject.getJSONObject("result");
                success = result.getString("logstatus").toString();
                //TODO todas las peticiones aqui

                Log.e(TAG,"logstatus"+success);
                if(success.equals("1")) {
                    new AsynckCliente(Login.this, usuario).execute();
                    new AsynckProyecto(Login.this, usuario).execute();
                    new AsynckTipoEnc(Login.this, usuario).execute();
                    new AsynckCatMaster(Login.this, usuario).execute();
                    new AsynckPreguntas(Login.this, usuario).execute();
                    new AsynckRespuestas(Login.this, usuario).execute();
                }else{
                    success ="0";
                }
                return success;

            }catch (JSONException e) {
                e.printStackTrace();
            }
            return success;
        }
    }
    private DBHelper getmDBHelper(){
        if (mDBHelper == null){
            mDBHelper = OpenHelperManager.getHelper(getBaseContext(),DBHelper.class);
        }
        return mDBHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDBHelper != null){
            OpenHelperManager.releaseHelper();
            mDBHelper= null;
        }
    }


}
