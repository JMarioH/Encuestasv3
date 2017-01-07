package com.popgroup.encuestasv3;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.CatMaster;
import com.popgroup.encuestasv3.Model.Preguntas;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by jesus.hernandez on 14/12/16.
 */
public class Encuestas extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    Bundle bundle;
    DBHelper mDBHelper;
    Dao dao ;
    Toolbar toolbar;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    ArrayList<CatMaster> arrayCatmaster;
    ListView listEncuestas;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayEnc;
    ArrayList<Preguntas> arrayPreguntas;
    String idArchivoSel,idEncuesta, idTienda,usuario;

    String idpregunta = "0";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ini_encuestas);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtTitle.setText("Encuestas");
        txtTitle.setTextSize(18);
        txtTitle.setTextColor(getBaseContext().getResources().getColor(R.color.colorTextPrimary));
        setSupportActionBar(toolbar);

        bundle = new Bundle();
        Bundle extras = getIntent().getExtras();

        idArchivoSel = extras.getString("idArchivo");
        idEncuesta = extras.getString("idEncuesta");
        idTienda = extras.getString("idTienda");
        usuario = extras.getString("usuario");

        arrayEnc = new ArrayList<>();
        try {
            dao = getmDBHelper().getCatMasterDao();
            arrayCatmaster = (ArrayList<CatMaster>) dao.queryBuilder().distinct().selectColumns("nombre")
                    .where().eq("idArchivo",idArchivoSel).and().eq("flag",true).query();
            for(CatMaster item:arrayCatmaster){
                Log.e(TAG,"item encuestas : "+item.getNombre());
                arrayEnc.add(item.getNombre());
            }

            arrayPreguntas = new ArrayList<>();
            dao = getmDBHelper().getPregutasDao();

            arrayPreguntas = (ArrayList<Preguntas>) dao.queryBuilder().where().eq("idEncuesta",idEncuesta).and().eq("orden",1).query();

            for (Preguntas preguntas: arrayPreguntas){
                idpregunta = String.valueOf(preguntas.getIdPregunta());

            }
            dao.clearObjectCache();

            dao.clearObjectCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        adapter= new ArrayAdapter<String>(this,R.layout.simple_list_item,arrayEnc){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView  = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                textView.setGravity(Gravity.CENTER);
                return view;
            }
        };
        listEncuestas = (ListView) findViewById(R.id.listEncuestas);
        listEncuestas.setAdapter(adapter);
        listEncuestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getAdapter().getItem(i).toString();
                String id = null;
                String idArchivo= null;
                try {
                    dao = getmDBHelper().getCatMasterDao();
                    arrayCatmaster = (ArrayList<CatMaster>) dao.queryBuilder().distinct().selectColumns("idTienda","idArchivo").where().in("idArchivo",idArchivoSel).and().eq("nombre",value).query();
                    for(CatMaster item:arrayCatmaster){
                        id = item.getIdTienda();
                        idArchivo = item.getIdArchivo();
                    }
                    dao.clearObjectCache();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                bundle.putString("usuario",usuario);
                bundle.putString("idEncuesta",idEncuesta);
                bundle.putString("encuesta",value);
                bundle.putString("idEstablecimiento",id);
                bundle.putString("idTienda",idTienda);
                bundle.putString("idArchivo",idArchivo);
                bundle.putString("numPregunta",idpregunta);
                bundle.putString("numRespuesta","0");

                Intent intent = new Intent(Encuestas.this,Cuestionario.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtras(bundle);
                startActivity(intent);


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
            finish();
        }
        return super.onOptionsItemSelected(item);

    }


}
