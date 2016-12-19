package com.popgroup.encuestasv3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.GeoEstatica;
import com.popgroup.encuestasv3.Model.Preguntas;
import com.popgroup.encuestasv3.Model.Respuestas;
import com.popgroup.encuestasv3.Utility.GPSTracker;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.popgroup.encuestasv3.R.id.toolbar;
import static com.popgroup.encuestasv3.R.id.txtTitle;

/**
 * Created by jesus.hernandez on 14/12/16.
 */
public class Cuestionario extends AppCompatActivity{
    String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    DBHelper mDBHelper;
    Dao dao;
    Bundle bundle;
    String idTienda,idEncuesta,usuario ,encuesta,numRespuesta;
    String numPregunta = "0" ;
    Preguntas preguntas ;
    Respuestas respuestas;
    //opciones para preguntas
    ArrayList<Preguntas> arrayPreguntas;
    ArrayList<Respuestas> arrayRespuestas;
    @BindView(R.id.txtPregunta)
    TextView txtPregunta;
    @BindView(R.id.btnSiguiente)
    Button btnSiguiente;
    //opciones de respuestas
    @BindView(R.id.editRespLibre)
    EditText editRespLibre;
    @BindView(R.id.btnOpc)
    Button btnOpciones;
    @BindView(R.id.spnOpciones)
    Spinner spnOpciones;

    @BindView(R.id.LblMensaje)
    TextView textMensaje;
    ArrayAdapter arrayAdapter;
    // localizacion
    GPSTracker gpsTracker;
    GeoEstatica geoEstatica;
    double longitud = 0.0 , latitud = 0.0;


    public ArrayList<CharSequence> arrayOpcSelecionadas = new ArrayList<CharSequence>();
    protected String arreglo[];
    StringBuilder stringBuilder = new StringBuilder();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

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
        gpsTracker = new GPSTracker(this);
        geoEstatica = new GeoEstatica().getInstance();
        Bundle extras = getIntent().getExtras();
        idEncuesta = extras.getString("idEncuesta").toString();
        encuesta = extras.getString("encuesta").toString();
        idTienda = extras.getString("idTienda").toString();
        usuario = extras.getString("usuario").toString();
        numPregunta = extras.getString("numPregunta").toString();
        Log.e(TAG,"numPregunta"+ numPregunta);
        //recivimos las variables iniciales
        bundle.putString("idEncuesta",idEncuesta);
        bundle.putString("encuesta",encuesta);
        bundle.putString("idTienda",idTienda);
        bundle.putString("usuario",usuario);
        preguntas = new Preguntas();
        //revision de permisos antes de iniciar la encuesta

        int permisoUbicacion = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        Log.e(TAG,"permisois " + permisoUbicacion);
        // -1 sin permisos ,  0  permiso otorgado
        if (permisoUbicacion!= PackageManager.PERMISSION_GRANTED){
            Log.e(TAG , "check Permisos de ubicicion "+" permiso false" + permisoUbicacion);
            showPermisoGPS();
        }
        // validamos que exista una ubicacion dsiponible
        if(geoEstatica.ismEstatus()==false){
            geoEstatica.setmEstatus(true);
            longitud = gpsTracker.getLongitude();
            latitud = gpsTracker.getLatitude();

            if(longitud!=0.0 && latitud != 0.0) {
                geoEstatica.setsLatitud(gpsTracker.getLatitude());
                geoEstatica.setsLongitud(gpsTracker.getLongitude());
            }

            Log.e(TAG,"geo Longitud " + longitud);
            Log.e(TAG,"geo Latitud " + latitud);
        }
        if (numPregunta!=null){ // primera pregunta
            //recuperamos las preguntas
            try {
                arrayPreguntas = new ArrayList<Preguntas>();
                dao = getmDBHelper().getPregutasDao();

                arrayPreguntas = (ArrayList<Preguntas>) dao.queryBuilder().distinct()
                        .selectColumns("idpregunta","pregunta","orden")
                        .where().eq("idpregunta",numPregunta)
                        .and().eq("idencuesta",idEncuesta)
                        .query();

                dao.clearObjectCache();
                //recuperamos las preguntas para este cuestionario

                for (Preguntas item :arrayPreguntas) {
                    Log.e(TAG,"idPregunta" + item.getIdPregunta());
                    String pregunta = item.getPregunta();
                    final int idpregunta = item.getIdPregunta();
                    // seteamos las preguntas  . . .
                    txtPregunta.setText(pregunta);
                    //armamos el array de respuetas para cada pregunta
                    arrayRespuestas = new ArrayList<>();
                    dao = getmDBHelper().getRespuestasDao();
                    arrayRespuestas = (ArrayList<Respuestas>) dao.queryBuilder().distinct()
                            .selectColumns("idrespuesta","respuesta","sigPregunta","respuestaLibre","idEncuesta")
                            .where().eq("idPregunta",idpregunta)
                            .and().eq("idencuesta",idEncuesta)
                            .query();
                    dao.clearObjectCache();

                    final ArrayList arrayResp = new ArrayList<String>();
                    final ArrayList arrayOpcCombo = new ArrayList<String>();

                    for (final Respuestas resp : arrayRespuestas){ // armamos las opciones para cada tipo de pregunta
                        Log.w(TAG,"getIdrespuesta : " + resp.getRespuesta());
                        Log.w(TAG,"idRespuesta : " + resp.getIdRespuesta());
                        Log.w(TAG,"sigPregunta : " + resp.getSigPregunta());
                        Log.w(TAG,"Respuesta : " + resp.getRespuesta());
                        Log.w(TAG,"respuestaLibre : " + resp.getRespLibre());
                        Log.w(TAG,"idEncuesta : " + resp.getIdEncuesta());

                        if(resp.getIdRespuesta()==1875){
                            //Respuesra abierta
                            editRespLibre.setVisibility(View.VISIBLE);
                        }else{

                            if(item.getMultiple()!=0){
                                // RESPUESTAS DE OPCION MULTIPLE
                                spnOpciones.setVisibility(View.VISIBLE);
                                arrayResp.add(resp.getRespuesta());
                                comboOpciones(arrayResp,idpregunta);
                            }else{

                                // RESPUESTAS DE MULTI SELECCION
                                arrayOpcCombo.add(resp.getRespuesta());
                                btnOpciones.setVisibility(View.VISIBLE);
                                btnOpciones.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        showOpciones(arrayOpcCombo,idpregunta);
                                    }
                                });


                            }
                        }

                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeOpcSelecionada();

                Toast.makeText(getBaseContext(), "num Pregunta" + numPregunta,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(),Cuestionario.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });
    }

    private void showOpciones(final ArrayList<String> arrayResp, int idpregunta) {
        Log.e(TAG," idpregunta prueba checkbox " + idpregunta);
        Log.e(TAG," size array " + arrayResp.size());

        boolean[] chekedOpcion = new boolean[arrayResp.size()];
        int count = arrayResp.size();
        for (int i = 0; i < count; i++) {
            chekedOpcion[i] = arrayOpcSelecionadas.contains(arrayResp.get(i));
        }
        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked)
                    arrayOpcSelecionadas.add(arrayResp.get(which));
                else
                    arrayOpcSelecionadas.remove(arrayResp.get(which));
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Respuestas");

        arreglo = new String[arrayResp.size()];
        for (int j = 0; j < arrayResp.size(); j++) {
            arreglo[j] = arrayResp.get(j);
        }

        builder.setMultiChoiceItems(arreglo, chekedOpcion, coloursDialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void onChangeOpcSelecionada(){
        String valSelecionado= "";
        for (int i = 0; i < arrayOpcSelecionadas.size(); i++) {

            CharSequence value = arrayOpcSelecionadas.get(i);
            valSelecionado = String.valueOf(value);
            String id_respuestaMultiple = "";
            //obetenemos el id de cada respuesta selecionada
            //id_respuestaMultiple = db.getId_respuestaMultiple(colour1);
            //insertando en la tabla
            stringBuilder.append(valSelecionado + " ,");
        }
        bundle.putString("numPregunta","1433");
        //stringBuilder.append(valSelecionado + " ,");
        Log.e(TAG,"stringBuilder " + stringBuilder);
        textMensaje.setEllipsize(TextUtils.TruncateAt.END);
        textMensaje.setSingleLine(true);
    }

    private void comboOpciones(ArrayList<String>arrayRespuestas , final int idPregSel) {
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayRespuestas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOpciones.setAdapter(arrayAdapter);
        spnOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG,"itemSelecionado "+adapterView.getItemAtPosition(i) );
                ArrayList<Respuestas> arrayRespSel = new ArrayList<Respuestas>();
                String value = adapterView.getAdapter().getItem(i).toString();
                try {
                    dao = null;
                    dao = getmDBHelper().getRespuestasDao();
                    arrayRespSel = (ArrayList<Respuestas>) dao.queryBuilder().distinct()
                            .selectColumns("idrespuesta", "respuesta", "sigPregunta", "respuestaLibre", "idEncuesta")
                            .where().eq("respuesta",value)
                            .and().eq("idpregunta",idPregSel)
                            .and().eq("idencuesta", idEncuesta)
                            .query();
                    for (Respuestas itemSel : arrayRespSel){
                        Log.e(TAG,"pregunta siguiente  : " + itemSel.getSigPregunta());
                        bundle.putString("numPregunta", itemSel.getSigPregunta());
                    }
                    dao.clearObjectCache();
                }catch (SQLException e){

                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void showPermisoGPS(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Permisos ");
        alertDialog.setMessage("Debe proporcionar permisos de ubicacion para continuar ");
        alertDialog.setPositiveButton("ir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
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
     super.onBackPressed();
        Log.e(TAG,"onBackPRessed "+ numPregunta);
        bundle.putString("numPregunta", numPregunta);
        geoEstatica.setmEstatus(false);
    }
}
