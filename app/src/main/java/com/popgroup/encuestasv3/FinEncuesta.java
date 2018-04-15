package com.popgroup.encuestasv3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.popgroup.encuestasv3.AsynckTask.AsynckEncuestas;
import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Utility.Connectivity;

import butterknife.BindView;
/**
 * Created by jesus.hernandez on 26/12/16.
 * envia los datos de la encuesta al servidor
 */
public class FinEncuesta extends BaseActivity {

    public String idEncuesta, encuesta, idTienda, idEstablecimiento, idArchivo, usuario;
    String TAG = getClass().getSimpleName();
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.btnTerminar)
    Button btnTerminar;
    Bundle bundle;
    Connectivity connectivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle= new Bundle();
        Bundle extras = getIntent().getExtras();
        connectivity = new Connectivity ();
        //recivimos las variables
        idEncuesta = extras.getString("idEncuesta").toString();
        encuesta = extras.getString("encuesta").toString();
        idTienda = extras.getString("idTienda").toString();
        idArchivo = extras.getString("idArchivo").toString();
        idEstablecimiento = extras.getString("idEstablecimiento").toString();
        usuario = extras.getString("usuario").toString();
        //envio de encuestas y fotos
        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectivity.isConnected (getBaseContext ())) {
                    new AsynckEncuestas (FinEncuesta.this, idEncuesta, idEstablecimiento, idTienda, usuario).execute ();
                } else {
                    showMessage ();
                }


            }
        });
    }

    @Override
    protected int setLayout () {
        return R.layout.activity_fin_encuesta;
    }

    @Override
    protected String setTitleToolBar () {
        return "Enviar Encuesta";
    }

    @Override
    protected void createPresenter () {

    }

    @Override
    protected BasePresenter getmPresenter () {
        return null;
    }

    public void showMessage () {
        final AlertDialog alertD = new AlertDialog.Builder (getBaseContext (), android.R.style.Theme_Material_Light_Dialog).create ();
        alertD.setTitle ("Mensaje");
        alertD.setMessage ("Debe Estar Conectado a una red para continuar.");
        alertD.setButton (AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener () {
                    public void onClick (DialogInterface dialog, int which) {
                        dialog.dismiss ();
                    }
                });

        alertD.setOnShowListener (new DialogInterface.OnShowListener () {
            @Override
            public void onShow (DialogInterface arg0) {
                alertD.getButton (AlertDialog.BUTTON_POSITIVE).setTextColor (getResources ().getColor (R.color.colorPrimary));

            }
        });

        alertD.show ();
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
