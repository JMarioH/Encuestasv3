package com.popgroup.encuestasv3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.popgroup.encuestasv3.AsynckTask.AsyncUploadFotos;
import com.popgroup.encuestasv3.AsynckTask.AsynckEncPendientes;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.CatMaster;
import com.popgroup.encuestasv3.Model.Cliente;
import com.popgroup.encuestasv3.Model.Fotos;
import com.popgroup.encuestasv3.Model.Proyecto;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;
import com.popgroup.encuestasv3.Model.TipoEncuesta;
import com.popgroup.encuestasv3.Model.User;
import com.popgroup.encuestasv3.Utility.Connectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


public class MainActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    Dao dao;
    Bundle bundle;
    private DBHelper mDBHelper;
    User user;
    Cliente cliente;
    Proyecto proyecto;
    TipoEncuesta tipoEncuesta;
    CatMaster catMaster;
    RespuestasCuestionario respuestasCuestionario;
    ArrayList<Fotos> fotosPendientes;
    ArrayList<RespuestasCuestionario> encuestasPendientes;
    ArrayList<User> arrayUser;

    @BindView(R.id.btnCambiarUser)
    Button btnCambiarUser;
    @BindView(R.id.btnInicio)
    Button btnInicio;
    @BindView(R.id.btnEncPnedientes)
    Button btnEncPendientes;
    @BindView(R.id.btnFotosPendientes)
    Button btnFotosPendientes;
    @BindView(R.id.txtTelefono)
    TextView txtLog;
    @BindView(R.id.txtUsuario)
    TextView txtUser;
    String mUsuario;
    boolean connectionAvailable;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTitle = (TextView) toolbar.findViewById(R.id.txtTitle);
        txtTitle.setText("Encuestas");
        txtTitle.setTextSize(18);
        txtTitle.setTextColor(getBaseContext().getResources().getColor(R.color.colorTextPrimary));
        setSupportActionBar(toolbar);
        bundle = new Bundle();
        try {
            // recuperando datos de la DB
            user = new User();
            cliente = new Cliente();
            proyecto = new Proyecto();
            tipoEncuesta = new TipoEncuesta();
            catMaster = new CatMaster();
            respuestasCuestionario = new RespuestasCuestionario();

            dao = getmDBHelper().getUserDao();
            arrayUser = (ArrayList<User>) dao.queryForAll();
            for (User item : arrayUser){
                mUsuario  = item.getNombre();

            }
            if(arrayUser.size()>0) {
                txtUser.setText(mUsuario);
            }
            dao.clearObjectCache();

            dao = getmDBHelper().getRespuestasCuestioanrioDao();
            encuestasPendientes = (ArrayList<RespuestasCuestionario>) dao.queryBuilder().distinct().selectColumns("idEstablecimiento").where().eq("flag",true).query();

            dao.clearObjectCache();

            dao = getmDBHelper().getFotosDao();
            fotosPendientes = (ArrayList<Fotos>) dao.queryForAll();
            dao.clearObjectCache();

            if(encuestasPendientes.size() > 0 ){
                btnEncPendientes.setVisibility(View.VISIBLE);
                btnEncPendientes.setText("Encuestas Pendientes " + " ( " + encuestasPendientes.size() + " )" );
            }
            if(fotosPendientes.size()>0){
                btnFotosPendientes.setVisibility(View.VISIBLE);
                btnFotosPendientes.setText("Fotos Pendientes " + " ( " +fotosPendientes.size() +" )");
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        btnCambiarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //revisamos si existe un telefono logeado
                try {
                    dao = getmDBHelper().getUserDao();
                    arrayUser = (ArrayList<User>) dao.queryForAll();
                    for (User item : arrayUser){
                        mUsuario  = item.getNombre();

                    }
                    dao.clearObjectCache();

                }catch (SQLException e){
                    e.printStackTrace();
                }
                if(arrayUser.size()>0){
                    showAlert();
                }else{
                    Intent i = new Intent(MainActivity.this,Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
            }
        });
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarProceso();
            }
        });
        btnEncPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encuestasPendientes();
            }
        });
        btnFotosPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotosPendientes();
            }
        });
    }
    private DBHelper getmDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return mDBHelper;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    public void encuestasPendientes(){
        Connectivity  connectivity = new Connectivity();
        connectionAvailable = connectivity.isConnected(this);
        if(connectionAvailable){
            new AsynckEncPendientes(this,mUsuario,encuestasPendientes.size()).execute();
        }else{
            showMessage();
        }
    }
    public void fotosPendientes(){
        Connectivity  connectivity = new Connectivity();
        connectionAvailable = connectivity.isConnectedWifi(this);
        JSONArray jsonFotos;
        ArrayList<NameValuePair> datosPost = null;
        if(connectionAvailable){
            try {
                dao = getmDBHelper().getFotosDao();
                fotosPendientes =(ArrayList<Fotos>) dao.queryForAll();
                String nomArchivo;
                int j = 0;
                jsonFotos = new JSONArray();
                for (int x = 0; x < fotosPendientes.size(); x++) {
                    datosPost = new ArrayList<>();
                    JSONObject jsonFoto = new JSONObject();
                    try {
                        nomArchivo = fotosPendientes.get(x).getIdEncuesta() + "_" + fotosPendientes.get(x).getIdEstablecimiento() + "_" + fotosPendientes.get(x).getNombre() + "_" + x + ".jpg";
                        jsonFoto.put("idEstablecimiento", fotosPendientes.get(x).getIdEstablecimiento());
                        jsonFoto.put("idEncuesta", fotosPendientes.get(x).getIdEncuesta());
                        jsonFoto.put("nombreFoto", nomArchivo.toString());
                        jsonFoto.put("base64", fotosPendientes.get(x).getBase64());
                        jsonFotos.put(jsonFoto);
                        j++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                datosPost.add(new BasicNameValuePair("subeFotos", jsonFotos.toString()));
                new AsyncUploadFotos(this, datosPost).execute();
                if (j == fotosPendientes.size()) {

                    dao = getmDBHelper().getFotosDao();
                    DeleteBuilder<Fotos,Integer> deleteBuilder = dao.deleteBuilder();
                    deleteBuilder.delete();
                    dao.clearObjectCache();

                    btnFotosPendientes.setVisibility(View.GONE);
                }

                dao.clearObjectCache();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else{
            showMessage();
        }
    }
    public void showAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Mensaje");
        alertDialog.setMessage("Cambiar de usuario borrara los datos existentes");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try { // recuperamos los datos de la base de datos

                            dao = getmDBHelper().getUserDao();
                            DeleteBuilder<User,Integer> deleteBuilder = dao.deleteBuilder();
                            deleteBuilder.delete();
                            dao.clearObjectCache();
                            txtUser.setText("00000000");

                            dao = getmDBHelper().getUserDao();
                            dao.deleteBuilder().delete();
                            dao.clearObjectCache();

                            dao = getmDBHelper().getProyectoDao();
                            dao.deleteBuilder().delete();
                            dao.clearObjectCache();

                            dao = getmDBHelper().getClienteDao();
                            dao.deleteBuilder().delete();
                            dao.clearObjectCache();

                            dao = getmDBHelper().getTipoEncDao();
                            dao.deleteBuilder().delete();
                            dao.clearObjectCache();

                            dao = getmDBHelper().getCatMasterDao();
                            dao.deleteBuilder().delete();
                            dao.clearObjectCache();

                            dao = getmDBHelper().getPregutasDao();
                            dao.deleteBuilder().delete();
                            dao.clearObjectCache();

                            dao = getmDBHelper().getRespuestasDao();
                            dao.deleteBuilder().delete();
                            dao.clearObjectCache();

                            dao = getmDBHelper().getRespuestasCuestioanrioDao();
                            dao.deleteBuilder().delete();
                            dao.clearObjectCache();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        bundle.putString("bandera","1");
                        Intent intent = new Intent(getBaseContext(), Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancelar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public void showMessage(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Mensaje");
        alertDialog.setMessage("Debe estar conectado a una red Estable para continuar");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    public void iniciarProceso(){
        boolean existenDatos = true;
        if(existenDatos){
            Intent i = new Intent(MainActivity.this,Proyectos.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }else{
            showMessage();
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
