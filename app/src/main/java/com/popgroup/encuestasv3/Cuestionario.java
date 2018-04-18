package com.popgroup.encuestasv3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Base.PermisionActivity;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.GeoEstatica;
import com.popgroup.encuestasv3.Model.GeoLocalizacion;
import com.popgroup.encuestasv3.Model.Preguntas;
import com.popgroup.encuestasv3.Model.Respuestas;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;
import com.popgroup.encuestasv3.Utility.GPSTracker;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by jesus.hernandez on 14/12/16.
 * clase principal para responder el cuestionario y recorrer las preguntas
 */
public class Cuestionario extends PermisionActivity {

    public ArrayList<CharSequence> arrayOpcSelecionadas = new ArrayList<CharSequence> ();
    public String respLibre = "";
    protected String arreglo[];
    String TAG = getClass ().getSimpleName ();
    Toolbar toolbar;
    @BindView (R.id.txtTitle)
    TextView txtTitle;
    DBHelper mDBHelper;
    Dao dao;
    Bundle bundle;
    String idTienda, idEncuesta, idArchivo, idEstablecimiento, usuario, encuesta, numRespuesta;
    String numPregunta = "0";
    String respSpinner;
    Preguntas preguntas;
    Respuestas respuestas;
    //opciones para preguntas
    ArrayList<Preguntas> arrayPreguntas;
    ArrayList<Respuestas> arrayRespuestas;
    @BindView (R.id.txtPregunta)
    TextView txtPregunta;
    @BindView (R.id.btnSiguiente)
    Button btnSiguiente;
    //opciones de respuestas
    @BindView (R.id.editRespLibre)
    EditText editRespLibre;
    @BindView (R.id.btnOpc)
    Button btnOpciones;
    @BindView (R.id.spnOpciones)
    Spinner spnOpciones;
    @BindView (R.id.LblMensaje)
    TextView textMensaje;
    ArrayAdapter arrayAdapter;
    // localizacion
    GPSTracker gpsTracker;
    GeoEstatica geoEstatica;
    GeoLocalizacion geoLocalizacion;
    RespuestasCuestionario respCuestionario;
    double longitud = 0.0, latitud = 0.0;
    StringBuilder stringBuilder = new StringBuilder ();
    Boolean spinnerRespuesta = false;
    String tipoResp = "";
    String fecha = "";
    int idpregunta = 0;
    // validamos una seleccion sobre el spinner
    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener () {
        public boolean onTouch (View v, MotionEvent event) {
            if (event.getAction () == MotionEvent.ACTION_UP) {
                spinnerRespuesta = true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        checkPermissions ();
        bundle = new Bundle ();

        gpsTracker = new GPSTracker (this);
        geoEstatica = new GeoEstatica ().getInstance ();
        //recivimos las variables del bundle
        Bundle extras = getIntent ().getExtras ();
        idEncuesta = extras.getString ("idEncuesta").toString ();
        encuesta = extras.getString ("encuesta").toString ();
        idTienda = extras.getString ("idTienda").toString ();
        idEstablecimiento = extras.getString ("idEstablecimiento").toString ();
        idArchivo = extras.getString ("idArchivo").toString ();
        usuario = extras.getString ("usuario").toString ();
        numPregunta = extras.getString ("numPregunta").toString ();
        numRespuesta = extras.getString ("numRespuesta").toString ();

        // creanos de nuevo las variables del bundle
        bundle.putString ("idEncuesta", idEncuesta);
        bundle.putString ("encuesta", encuesta);
        bundle.putString ("idTienda", idTienda);
        bundle.putString ("idEstablecimiento", idEstablecimiento);
        bundle.putString ("idArchivo", idArchivo);
        bundle.putString ("usuario", usuario);

        preguntas = new Preguntas ();
        //recuperamos las fecha y tiempo al inciar la encuesta
        DateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        Date dt = new Date ();
        fecha = dateFormat.format (dt);
        // validamos que exista una ubicacion dsiponible
        if (!geoEstatica.ismEstatus ()) {

            longitud = gpsTracker.getLongitude ();
            latitud = gpsTracker.getLatitude ();
            if (longitud != 0.0 && latitud != 0.0) {
                geoEstatica.setmEstatus (true);
                geoEstatica.setsLatitud (gpsTracker.getLatitude ());
                geoEstatica.setsLongitud (gpsTracker.getLongitude ());
                // guardamos la geolocalizacion en la base de datos
                try {
                    dao = getmDBHelper ().getGeosDao ();
                    geoLocalizacion = new GeoLocalizacion ();
                    geoLocalizacion.setFecha (fecha);
                    geoLocalizacion.setIdEncuesta (Integer.parseInt (idEncuesta));
                    geoLocalizacion.setIdTienda (idTienda);
                    geoLocalizacion.setLatitud (String.valueOf (latitud));
                    geoLocalizacion.setLongitud (String.valueOf (longitud));
                    dao.create (geoLocalizacion);
                    dao.clearObjectCache ();

                } catch (SQLException e) {
                    Log.e(TAG, "SQLException Geos " + e.getMessage());
                }
            }
        }
        if (!numPregunta.equals ("FOTO")) { // primera pregunta
            //recuperamos las preguntas
            try {
                arrayPreguntas = new ArrayList<Preguntas> ();
                dao = getmDBHelper ().getPregutasDao ();

                arrayPreguntas = (ArrayList<Preguntas>) dao.queryBuilder ()
                        .selectColumns ("idpregunta", "pregunta", "multiple", "orden", "idEncuesta")
                        .where ().eq ("idpregunta", numPregunta)
                        .and ().eq ("idEncuesta", idEncuesta)
                        .query ();
                dao.clearObjectCache ();

                //recuperamos las preguntas para este cuestionario
                for (Preguntas item : arrayPreguntas) {

                    String pregunta = item.getPregunta ();
                    idpregunta = item.getIdPregunta ();
                    // seteamos las preguntas  . . .
                    txtPregunta.setText (pregunta);
                    //armamos el array de respuetas para cada pregunta

                    arrayRespuestas = new ArrayList<> ();
                    dao = getmDBHelper ().getRespuestasDao ();
                    arrayRespuestas = (ArrayList<Respuestas>) dao.queryBuilder ()
                            .selectColumns ("idpregunta", "idrespuesta", "respuesta", "sigPregunta", "respuestaLibre", "idEncuesta")
                            .where ().eq ("idpregunta", idpregunta)
                            .and ().eq ("idEncuesta", idEncuesta).query ();
                    dao.clearObjectCache ();
                    final ArrayList arrayResp = new ArrayList<String> ();
                    final ArrayList arrayOpcCombo = new ArrayList<String> ();
                    for (final Respuestas resp : arrayRespuestas) { // armamos las opciones para cada tipo de pregunta

                        if (resp.getIdRespuesta () == 1875) {
                            tipoResp = "1";
                            editRespLibre.setVisibility (View.VISIBLE);
                            numRespuesta = editRespLibre.getText ().toString ();
                            bundle.putString ("numPregunta", resp.getSigPregunta ());
                            bundle.putString ("numRespuesta", resp.getRespuesta ());

                        } else {
                            if (item.getMultiple () == 0) {
                                // RESPUESTAS DE OPCION MULTIPLE

                                tipoResp = "2";
                                spnOpciones.setVisibility (View.VISIBLE);
                                arrayResp.add (resp.getRespuesta ());
                                sppinerOpciones (arrayResp, idpregunta);
                                spnOpciones.setOnTouchListener (spinnerOnTouch);
                            } else {
                                tipoResp = "3";
                              /*  Log.e(TAG,"respuesta checkbox " );*/
                                // RESPUESTAS DE MULTI SELECCION
                                arrayOpcCombo.add (resp.getRespuesta ());
                                btnOpciones.setVisibility (View.VISIBLE);
                                btnOpciones.setOnClickListener (new View.OnClickListener () {
                                    @Override
                                    public void onClick (View view) {
                                        // opcion para multicheck
                                        showOpciones (arrayOpcCombo);

                                    }
                                });
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace ();
            }
        } else {

            bundle.putString ("numPregunta", numPregunta);
            Intent intent = new Intent (getBaseContext (), Fotografia.class);
            intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtras (bundle);
            startActivity (intent);
        }
        btnSiguiente.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Boolean respuestaLibre = null;
                Boolean error = false; // validamos que tengamos una respuesta para cada opcion
                if (tipoResp == "2") {
                    if (spinnerRespuesta != true) {
                        showMessage ();
                        error = true;
                    } else {
                        numRespuesta = respSpinner;

                    }
                }
                if (tipoResp == "3") {
                    if (arrayOpcSelecionadas.isEmpty ()) {
                        showMessage ();
                        error = true;
                    }

                }
                if (tipoResp == "1") {
                    if (editRespLibre.getText ().toString ().equals ("")) {
                        showMessageRespLibre ();
                        error = true;
                    } else {
                        respuestaLibre = true;
                        numRespuesta = editRespLibre.getText ().toString ();
                    }
                }
                if (!error) {
                    // TODO guardamos las respuestas en la base de datos
                    if (!arrayOpcSelecionadas.isEmpty ()) {
                        onChangeOpcSelecionada ();
                    }
                    try {
                        dao = getmDBHelper ().getRespuestasCuestioanrioDao ();
                        respCuestionario = new RespuestasCuestionario ();
                        respCuestionario.setIdEncuesta (Integer.parseInt (idEncuesta));
                        respCuestionario.setFecha (fecha);
                        respCuestionario.setIdArchivo (idArchivo);
                        respCuestionario.setIdPregunta (numPregunta);
                        respCuestionario.setIdRespuesta (numRespuesta);
                        respCuestionario.setIdTienda (idTienda);
                        respCuestionario.setIdEstablecimiento (idEstablecimiento);
                        respCuestionario.setRespuestLibre (respuestaLibre);
                        dao.create (respCuestionario);
                        dao.clearObjectCache ();
                    } catch (SQLException e) {
                        e.printStackTrace ();
                    }

                    Intent intent = new Intent (getBaseContext (), Cuestionario.class);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtras (bundle);
                    startActivity (intent);
                }
            }
        });
    }

    @Override
    protected int setLayout () {
        return R.layout.activity_cuestionario;
    }

    @Override
    protected String setTitleToolBar () {
        return "Cuestionario";
    }

    @Override
    protected void createPresenter () {

    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper ();
            mDBHelper = null;
        }
    }

    @Override
    protected BasePresenter getmPresenter () {
        return null;
    }

    private DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return mDBHelper;
    }

    private void sppinerOpciones (ArrayList<String> arrayRespuestas, final int idPregSel) {
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, arrayRespuestas);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spnOpciones.setAdapter(arrayAdapter);
        spnOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Respuestas> arrayRespSel = new ArrayList<>();
                String value = adapterView.getAdapter().getItem(i).toString();

                try {

                    dao = getmDBHelper().getRespuestasDao();
                    arrayRespSel = (ArrayList<Respuestas>) dao.queryBuilder().selectColumns("idrespuesta", "respuesta", "sigPregunta", "respuestaLibre", "idEncuesta").where().eq("respuesta", value).and().eq("idpregunta", idPregSel).and().eq("idEncuesta", idEncuesta).query();
                    dao.clearObjectCache();

                    for (Respuestas itemSel : arrayRespSel) {

                        bundle.putString("numPregunta", String.valueOf(itemSel.getSigPregunta()));
                        bundle.putString("numRespuesta", String.valueOf(itemSel.getIdRespuesta()));
                        respSpinner = String.valueOf(itemSel.getIdRespuesta());

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected (AdapterView<?> adapterView) {
            }
        });
    }

    private void showOpciones (final ArrayList<String> arrayResp) {
        boolean[] chekedOpcion = new boolean[arrayResp.size ()];
        for (int i = 0; i < arrayResp.size (); i++) {
            chekedOpcion[i] = arrayOpcSelecionadas.contains (arrayResp.get (i));
        }
        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener () {
            @Override
            public void onClick (DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    arrayOpcSelecionadas.add (arrayResp.get (which));
                } else {
                    arrayOpcSelecionadas.remove (arrayResp.get (which));
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder (this, R.style.MyDialogTheme);

        builder.setTitle ("Respuestas");

        arreglo = new String[arrayResp.size ()];
        for (int j = 0; j < arrayResp.size (); j++) {
            arreglo[j] = arrayResp.get (j);
        }
        builder.setMultiChoiceItems (arreglo, chekedOpcion, coloursDialogListener);
        AlertDialog dialog = builder.create ();

        dialog.show ();

    }

    //Mensajes de Validacion y Error
    public void showMessage () {
        final AlertDialog alertD = new AlertDialog.Builder (this).create ();
        alertD.setTitle("Mensaje");
        alertD.setMessage("Debe seleccionar una respuesta para continuar");
        alertD.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertD.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow (DialogInterface arg0) {
                alertD.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        alertD.show();
    }

    // menu inferior
    public void showMessageRespLibre () {
        final AlertDialog alertDialog = new AlertDialog.Builder (this).create ();
        alertDialog.setTitle("Mensaje");
        alertDialog.setMessage("Debe contestar esta pregunta");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow (DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        });
        alertDialog.show();
    }

    private void onChangeOpcSelecionada () {
        String valSelecionado = "";
        for (int i = 0; i < arrayOpcSelecionadas.size (); i++) {
            CharSequence value = arrayOpcSelecionadas.get (i);
            valSelecionado = String.valueOf (value);
        /*    Log.e(TAG,"Valor Seleccionado " + valSelecionado);*/
            stringBuilder.append (valSelecionado + " ,");
            //recuperamos los id de las respuestas seleccionadas
            arrayRespuestas = new ArrayList<> ();
            String pregSig = null;
            String idResp = null;
            try {
                dao = getmDBHelper ().getRespuestasDao ();
                arrayRespuestas = (ArrayList<Respuestas>) dao.queryBuilder ()
                        .selectColumns ("idpregunta", "idrespuesta", "sigPregunta", "respuesta")
                        .where ().eq ("respuesta", valSelecionado)
                        .and ().eq ("idEncuesta", idEncuesta)
                        .query ();
                dao.clearObjectCache ();
                for (Respuestas respItem : arrayRespuestas) {
                    pregSig = respItem.getSigPregunta ().toString ();
                    idResp = String.valueOf (respItem.getIdRespuesta ());
                    try {
                        dao = getmDBHelper ().getRespuestasCuestioanrioDao ();
                        respCuestionario = new RespuestasCuestionario ();
                        respCuestionario.setIdEncuesta (Integer.parseInt (idEncuesta));
                        respCuestionario.setFecha (fecha);
                        respCuestionario.setIdArchivo (idArchivo);
                        respCuestionario.setIdPregunta (numPregunta);
                        respCuestionario.setIdRespuesta (idResp);
                        respCuestionario.setIdTienda (idTienda);
                        respCuestionario.setRespuestLibre (false);
                        respCuestionario.setIdEstablecimiento (idEstablecimiento);
                        dao.create (respCuestionario);
                        dao.clearObjectCache ();

                    } catch (SQLException e) {
                        e.printStackTrace ();
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace ();
            }
            bundle.putString ("numPregunta", pregSig);
            bundle.putString ("numRespuesta", idResp);
        }
    }

    @Override
    protected void onSuccesPermissions (boolean result) {

        btnSiguiente.setVisibility (checkPermissions () ? View.VISIBLE : View.GONE);

    }

    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Bundle extras = getIntent ().getExtras ();
            numPregunta = extras.getString ("numPregunta");

            try {
                dao = getmDBHelper ().getRespuestasCuestioanrioDao ();
                DeleteBuilder<RespuestasCuestionario, Integer> deleteBuilder = dao.deleteBuilder ();
                deleteBuilder.where ().eq ("idPregunta", numPregunta).and ().eq ("idEstablecimiento", idEstablecimiento);
                deleteBuilder.delete ();
                dao.clearObjectCache ();
            } catch (SQLException e) {
                e.printStackTrace ();
            }
        }
        super.onKeyDown (keyCode, event);
        return true;
    }

}
