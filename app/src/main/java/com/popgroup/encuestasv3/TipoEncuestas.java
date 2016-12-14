package com.popgroup.encuestasv3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by jesus.hernandez on 14/12/16.
 * Selecciona tipo de encuesta
 */
public class TipoEncuestas extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    Bundle bundle;
    String usuario,cliente,idproyecto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_encuestas);
        Bundle extras = getIntent().getExtras();

        usuario = extras.getString("usuario");
        cliente = extras.getString("cliente");
        idproyecto = extras.getString("idproyecto");


        Log.e(TAG,"usuario" + usuario);
        Log.e(TAG,"cliente" + cliente);
        Log.e(TAG,"idproyecto" + idproyecto);
    }
}