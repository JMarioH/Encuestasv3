package com.popgroup.encuestasv3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by jesus.hernandez on 14/12/16.
 */
public class Encuestas extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    Bundle bundle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ini_encuestas);

        bundle = new Bundle();
        Bundle extras = getIntent().getExtras();
        Log.e(TAG,"idarchivo"+extras.getString("idArchivo").toString());
        Log.e(TAG,"idEncuesta"+extras.getString("idEncuesta").toString());
    }
}
