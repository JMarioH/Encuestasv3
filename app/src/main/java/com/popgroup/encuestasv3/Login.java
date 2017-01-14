package com.popgroup.encuestasv3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jesus.hernandez on 07/12/16.
 */

public class Login extends AppCompatActivity{
    String TAG = getClass().getSimpleName();

    ProgressDialog pDialog;
    Toolbar toolbar;

    TextView txtTitle ;
    @BindView(R.id.editUser)
    EditText editUser;

    @BindView(R.id.editPassword)
    EditText editPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    Bundle bundle;
    public String usuario ;
    public String password;
    public String URL;
    public String bandera;

    ArrayList<NameValuePair> data;
    Constantes constantes;
    DBHelper mDBHelper;
    Dao dao;
    Connectivity connectivity;
    Boolean conectionAvailable = false;

    User user;
    ArrayList<User> arrayUser;
    String usuarioAct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTitle = (TextView) toolbar.findViewById(R.id.txtTitle);
        txtTitle.setText("Encuestas");
        txtTitle.setTextSize(18);
        txtTitle.setTextColor(getBaseContext().getResources().getColor(R.color.colorTextPrimary));
        setSupportActionBar(toolbar);
        constantes = new Constantes();
        bundle = new Bundle();
        connectivity = new Connectivity();
        user = new User();
        try {
            dao = getmDBHelper().getUserDao();
            arrayUser = (ArrayList<User>) dao.queryForAll();
            dao.queryForAll();
            for(User item :arrayUser){
                usuarioAct = item.getNombre();
            }
            if(usuarioAct!= null){
                Intent intent = new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            URL = constantes.getIPWBService();

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    connectivity = new Connectivity();
                    conectionAvailable = connectivity.isConnected(getBaseContext());
                    usuario = editUser.getText().toString();
                    password = editPassword.getText().toString();
                    Log.e(TAG, "user  : " + usuario);
                    if (conectionAvailable) {
                        new LoginAsynck().execute();
                    } else {
                        Toast.makeText(Login.this, "Debe conectarse a un red primero!", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuInicio) {
            //Display Toast
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }else if(id== R.id.menuSalir){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

}
