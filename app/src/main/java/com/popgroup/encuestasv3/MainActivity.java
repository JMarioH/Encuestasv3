package com.popgroup.encuestasv3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.CatMaster;
import com.popgroup.encuestasv3.Model.Cliente;
import com.popgroup.encuestasv3.Model.Preguntas;
import com.popgroup.encuestasv3.Model.Proyecto;
import com.popgroup.encuestasv3.Model.Respuestas;
import com.popgroup.encuestasv3.Model.TipoEncuesta;
import com.popgroup.encuestasv3.Model.User;
import com.popgroup.encuestasv3.Utility.Connectivity;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    Dao dao;
    private DBHelper mDBHelper;
    User user;
    Cliente cliente;
    Proyecto proyecto;
    TipoEncuesta tipoEncuesta;
    CatMaster catMaster;
    Preguntas preguntas;
    Respuestas respuestas;


    ArrayList<TipoEncuesta> arrayListTipoEnc;
    ArrayList<Cliente> arrayCliente;
    ArrayList<CatMaster> arrayCatMaster;
    ArrayList<Preguntas> arrayPreguntas;
    ArrayList<Respuestas> arrayRespuestas;

    @BindView(R.id.btnCambiarUser)
    Button btnCambiarUser;
    @BindView(R.id.btnInicio)
    Button btnInicio;
    @BindView(R.id.btnEncPnedientes)
    Button btnEncPendientes;
    @BindView(R.id.btnFotosPendientes)
    Button btnFotosPendientes;
    @BindView(R.id.btnSalir)
    Button btnSalir;
    @BindView(R.id.txtTelefono)
    TextView txtLog;
    @BindView(R.id.txtUsuario)
    TextView txtUser;

    String stringLog;
    String mUsuario;
    boolean connectionAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnCambiarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //revisamos si existe un telefono logeado
            try {
                dao = getmDBHelper().getUserDao();
                user = (User) dao.queryForId(1);
                dao.clearObjectCache();

            }catch (SQLException e){
                e.printStackTrace();
            }
            if(user!=null){
                Toast.makeText(MainActivity.this,"existe telefono " ,Toast.LENGTH_LONG).show();
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

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {
            // recuperando datos de la DB
            user = new User();
            cliente = new Cliente();
            proyecto = new Proyecto();
            tipoEncuesta = new TipoEncuesta();
            catMaster = new CatMaster();

            dao = getmDBHelper().getUserDao();
            user = (User) dao.queryForId(1);
            dao.clearObjectCache();

            dao = getmDBHelper().getClienteDao();
            cliente = (Cliente) dao.queryForId(1);
            dao.clearObjectCache();

            dao = getmDBHelper().getProyectoDao();
            proyecto = (Proyecto) dao.queryForId(1);
            dao.clearObjectCache();

            dao = getmDBHelper().getTipoEncDao();
            arrayListTipoEnc = (ArrayList<TipoEncuesta>) dao.queryBuilder().distinct().selectColumns("encuesta").query();
            Log.e(TAG, "arrayListTipo Encuesta : " + dao.queryForAll().size());
            dao.clearObjectCache();

            dao = getmDBHelper().getCatMasterDao();
            /*where().eq(CatMaster.IDTIENDA,"168045")*/
            arrayCatMaster = (ArrayList<CatMaster>) dao.queryBuilder().distinct().selectColumns("nombre").query();
            Log.e(TAG, "arrayListTipo catmaster : " + dao.queryForAll().size());
            dao.clearObjectCache();

            dao = getmDBHelper().getPregutasDao();
            /*where().eq(CatMaster.IDTIENDA,"168045")*/
            arrayPreguntas = (ArrayList<Preguntas>) dao.queryForAll();
            Log.e(TAG, "arrayPreguntas num : " + dao.queryForAll().size());
            dao.clearObjectCache();

            dao = getmDBHelper().getRespuestasDao();
            /*where().eq(CatMaster.IDTIENDA,"168045")*/
            arrayRespuestas = (ArrayList<Respuestas>) dao.queryForAll();
            Log.e(TAG, "arrayRespuestas num : " + dao.queryForAll().size());
            dao.clearObjectCache();



            if (user == null) {
                Log.e(TAG, "Ningún usuario con id = 1");
            } else {
                Log.e(TAG, "Recuperado usuario con id = 1: " + user.getNombre());
                mUsuario = user.getNombre().toString();
                txtUser.setText(mUsuario);
            }
            if (cliente == null) {
                Log.e(TAG, "Ningún usuario con id = 1");
            } else {
                Log.e(TAG, "Recuperado cliente con id = 1: " + cliente.getNombre());
                stringLog = "\n" + cliente.getNombre();
            }
            if (proyecto == null) {
                Log.e(TAG, "Ningún proyecto con id = 1");
            } else {
                Log.e(TAG, "Recuperado proyecto con id = 1: " + proyecto.getNombre());
                stringLog = "\n" + proyecto.getNombre();
            }

            if (arrayListTipoEnc.size() > 0) {
                for (TipoEncuesta item : arrayListTipoEnc) {
                    int id = item.getId();
                    String encuesta = item.getEncuesta();
                    String idArchivo = item.getIdArchivo();
                    String idTienda = item.getIdTienda();
                    String idEncuesta = item.getIdEncuesta();
                    String usuario = item.getNumerTel();
                    String nombre = item.getNombre();
                    String idProyecto = item.getIdProyecto();
                    String idEstablecimiento = item.getIdestablecimiento();
                  /*  Log.e(TAG, "id : " + id);
                    Log.e(TAG, "encuesta : " + encuesta);
                    Log.e(TAG, "idarchivo : " + idArchivo);
                    Log.e(TAG, "idTienda : " + idTienda);
                    Log.e(TAG, "idEncuesta : " + idEncuesta);
                    Log.e(TAG, "usuario : " + usuario);
                    Log.e(TAG, "Nombre : " + nombre);
                    Log.e(TAG, "idProyecto : " + idProyecto);
                    Log.e(TAG, "idEstablecimiento : " + idEstablecimiento);*/
                    stringLog = stringLog + "\ntipo encuesta :" + encuesta;
                }
            }

            if (arrayCatMaster.size() > 0) {
                for (CatMaster item : arrayCatMaster) {
                    int id = item.getId();
                    String idtienda = item.getIdTienda();
                    String nombre = item.getNombre();
                    String idArchivo = item.getIdArchivo();
                    String idEncuesta = item.getIdEncuesta();
                    Log.e(TAG, "Nombre : " + nombre);

                    stringLog = stringLog + "\n cat_master :" + nombre;
                }
            }

            if (arrayPreguntas.size() > 0) {
                for (Preguntas item : arrayPreguntas) {
                    int id = item.getId();
                    int idPregunta = item.getIdPregunta();
                    String pregunta = item.getPregunta();
                    int multiple = item.getMultiple();
                    int orden = item.getOrden();
                    int idencuesta = item.getIdEncuesta();
/*
                    Log.e(TAG, "id : " + id);
                    Log.e(TAG, "idPregunta : " + idPregunta);
                    Log.e(TAG, "pregunta : " + pregunta);
                    Log.e(TAG, "multiple : " + multiple);
                    Log.e(TAG, "orden : " + orden);
                    Log.e(TAG, "idEncuesta : " + idencuesta);*/
                }
            }

            if (arrayRespuestas.size() > 0) {
                for (Respuestas item : arrayRespuestas) {
                    int id = item.getId();
                    int idPregunta = item.getIdPregunta();
                    int idRespuesta = item.getIdRespuesta();
                    String respuesta = item.getRespuesta();
                    String sigPregunta = item.getSigPregunta();
                    String respLibre = item.getRespLibre();
                    int idencuesta = item.getIdEncuesta();

                 /*   Log.e(TAG, "id : " + id);
                    Log.e(TAG, "idPregunta : " + idPregunta);
                    Log.e(TAG, "idRespuesta : " + idRespuesta);
                    Log.e(TAG, "respuesta : " + respuesta);
                    Log.e(TAG, "sigPregunta : " + sigPregunta);
                    Log.e(TAG, "respLibre : " + respLibre);
                    Log.e(TAG, "idEncuesta : " + idencuesta);*/
                }
            }

          //  txtLog.setText("Data " + stringLog);



        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

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
            Toast.makeText(this,"sicronizar encuestas ",Toast.LENGTH_LONG).show();
        }else{
            showMessage();
        }

    }

    public void fotosPendientes(){
        Connectivity  connectivity = new Connectivity();
        connectionAvailable = connectivity.isConnected(this);
        if(connectionAvailable){
            Toast.makeText(this,"sicronizar fotos ",Toast.LENGTH_LONG).show();
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
                        dialog.dismiss();
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
        alertDialog.setMessage("Debe estar conectado a una red WI-FI para continuar");
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
}
