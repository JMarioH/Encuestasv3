package com.popgroup.encuestasv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.CatMaster;
import com.popgroup.encuestasv3.Model.Preguntas;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.popgroup.encuestasv3.R.id.toolbar;
import static com.popgroup.encuestasv3.R.id.txtTitle;

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
    String idArchivoSel,idEncuesta,usuario;
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

        Log.e(TAG,"idarchivo"+extras.getString("idArchivo").toString());
        Log.e(TAG,"idEncuesta"+extras.getString("idEncuesta").toString());
        Log.e(TAG,"usuario"+ extras.getString("usuario").toString());

        idArchivoSel = extras.getString("idArchivo");
        idEncuesta = extras.getString("idEncuesta");
        usuario = extras.getString("idArchivo");

        arrayEnc = new ArrayList<>();
        try {
            dao = getmDBHelper().getCatMasterDao();
            arrayCatmaster = (ArrayList<CatMaster>) dao.queryBuilder().distinct().selectColumns("nombre").where().in("idArchivo",idArchivoSel).query();
            for(CatMaster item:arrayCatmaster){
                Log.e(TAG,"a"+item.getNombre());
                arrayEnc.add(item.getNombre());
            }
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
                try {
                    dao = getmDBHelper().getCatMasterDao();
                    arrayCatmaster = (ArrayList<CatMaster>) dao.queryBuilder().distinct().selectColumns("idTienda").where().in("idArchivo",idArchivoSel).and().eq("nombre",value).query();
                    for(CatMaster item:arrayCatmaster){
                        id = item.getIdTienda();
                    }
                    dao.clearObjectCache();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Preguntas pregunta = new Preguntas();
                String idpregunta ="";
                try {

                    dao = getmDBHelper().getPregutasDao();
                    arrayPreguntas = (ArrayList<Preguntas>) dao.queryBuilder().distinct().selectColumns("idPregunta").where().eq("orden",1).query();

                    for (Preguntas item: arrayPreguntas){
                         idpregunta = String.valueOf(item.getIdPregunta());
                    }
                    dao.clearObjectCache();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                bundle.putString("usuario",usuario);
                bundle.putString("idEncuesta",idEncuesta);
                bundle.putString("encuesta",value);
                bundle.putString("idTienda",id);
                bundle.putString("numPregunta",idpregunta);
                bundle.putString("numRespuesta","1");

                Log.e(TAG,"encuesta seleccionada "+value + " idtienda " + id + "idPregunta "+ idpregunta);

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
}
