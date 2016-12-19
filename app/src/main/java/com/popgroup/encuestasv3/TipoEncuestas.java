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
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.TipoEncuesta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.id;
import static android.R.attr.value;
import static android.os.Build.VERSION_CODES.M;
import static com.popgroup.encuestasv3.R.id.toolbar;
import static com.popgroup.encuestasv3.R.id.txtTitle;

/**
 * Created by jesus.hernandez on 14/12/16.
 * Selecciona tipo de encuesta
 */
public class TipoEncuestas extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    Bundle bundle;
    String usuario,cliente,idproyecto;
    Toolbar  toolbar;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    TipoEncuesta tipoEncuesta;
    ArrayList<TipoEncuesta> arrayTipoEnc;
    ArrayList<String> arrayEncuestas;
    List<TipoEncuesta> listTipoEncuesta;


    ArrayAdapter<String> adapter;
    ListView listView;

    DBHelper mDBHelper;
    Dao dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_encuestas);
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

        usuario = extras.getString("usuario");
        cliente = extras.getString("cliente");
        idproyecto = extras.getString("idproyecto");
        Log.e(TAG,"usuario" + usuario);
        Log.e(TAG,"cliente" + cliente);
        Log.e(TAG,"idproyecto" + idproyecto);

        arrayEncuestas = new ArrayList<>();
        //recupermos los tipo de encuesta de la base de datos
        try {
            dao = getmDBHelper().getTipoEncDao();
            //arrayTipoEnc = (ArrayList<TipoEncuesta>)dao.queryForAll();
            arrayTipoEnc = (ArrayList<TipoEncuesta>)dao.queryBuilder().distinct().selectColumns("encuesta").where().eq("numero_tel",usuario).and().eq("idProyecto",idproyecto).and().eq("nombre",cliente).query();
            for (TipoEncuesta item : arrayTipoEnc){
                arrayEncuestas.add(item.getEncuesta());
                Log.e(TAG,"a :" + item.getEncuesta());
            }
            dao.clearObjectCache();
        } catch (SQLException e) {
            Log.i(TAG,"error",e);
        }


        adapter = new ArrayAdapter<String>(this,R.layout.simple_list_item,arrayEncuestas){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position,convertView,parent);
                TextView textView  = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                textView.setGravity(Gravity.CENTER);
                return view;

            }
        };

        listView = (ListView) findViewById(R.id.listTipoEnc);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  Toast.makeText(getBaseContext(),"tipo seleccionado " + id + "value " + value,Toast.LENGTH_SHORT).show();
                String idArchivoSel = null;
                String idEncuestaSel = null;
                try {
                    //obtenemos el idarchivo y idencuesta de la encuesta seleccionada
                    dao = getmDBHelper().getTipoEncDao();
                    arrayTipoEnc = (ArrayList<TipoEncuesta>) dao.queryBuilder().distinct()
                            .selectColumns("idArchivo","idEncuesta")
                            .where().eq("numero_tel",usuario)
                            .and().eq("idProyecto",idproyecto)
                            .and().eq("nombre",cliente)
                            .and().eq("encuesta",adapterView.getAdapter().getItem(i).toString())
                            .query();

                    for (TipoEncuesta item:arrayTipoEnc){
                        idArchivoSel = item.getIdArchivo();
                        idEncuestaSel = item.getIdEncuesta();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Log.e(TAG,"idArchivo " + idArchivoSel);
                Log.e(TAG,"idEncuesta " + idEncuestaSel);

                bundle.putString("idArchivo",idArchivoSel);
                bundle.putString("idEncuesta",idEncuestaSel);
                bundle.putString("usuario",usuario);

                Intent intent = new Intent(TipoEncuestas.this,Encuestas.class);
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