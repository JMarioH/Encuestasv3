package com.popgroup.encuestasv3;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.popgroup.encuestasv3.AsynckTask.AsynckEncuestas;
import com.popgroup.encuestasv3.DataBase.DBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by jesus.hernandez on 26/12/16.
 * envia los datos de la encuesta al servidor
 */
public class FinEncuesta extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    DBHelper mDBHelper;
    Toolbar toolbar;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.btnTerminar)
    Button btnTerminar;
    public String idEncuesta,encuesta,idTienda,idEstablecimiento,idArchivo,usuario;
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_encuesta);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtTitle.setText("Encuestas");
        txtTitle.setTextSize(18);
        txtTitle.setTextColor(getBaseContext().getResources().getColor(R.color.colorTextPrimary));
        setSupportActionBar(toolbar);
        bundle= new Bundle();
        Bundle extras = getIntent().getExtras();
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
              new AsynckEncuestas(FinEncuesta.this, idEncuesta, idEstablecimiento, idTienda, usuario).execute();

            }
        });
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
