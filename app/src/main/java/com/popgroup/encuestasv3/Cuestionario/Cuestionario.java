package com.popgroup.encuestasv3.Cuestionario;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.popgroup.encuestasv3.Base.PermisionActivity;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Dialog.DialogAlert;
import com.popgroup.encuestasv3.Dialog.DialogChoice;
import com.popgroup.encuestasv3.Dialog.DialogFactory;
import com.popgroup.encuestasv3.FinEncuesta.FinEncuestaActivity;
import com.popgroup.encuestasv3.Model.GeoLocalizacion;
import com.popgroup.encuestasv3.Model.Preguntas;
import com.popgroup.encuestasv3.Model.Respuestas;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;
import com.popgroup.encuestasv3.R;

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
public class Cuestionario extends PermisionActivity implements ICuestionarioView {

    public ArrayList<CharSequence> arrayOpcSelecionadas = new ArrayList<>();
    protected String arreglo[];
    String TAG = getClass().getSimpleName();
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    DBHelper mDBHelper;
    Dao dao;
    Bundle bundle;
    String idTienda, idEncuesta, idArchivo, idEstablecimiento, encuesta, numRespuesta;
    String numPregunta = "0";
    String respSpinner;
    Preguntas preguntas;
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
    private ArrayAdapter arrayAdapter;
    // localizacion
    private RespuestasCuestionario respCuestionario;
    private StringBuilder stringBuilder = new StringBuilder();
    private Boolean spinnerRespuesta = false;
    private String tipoResp = "";
    private String fecha = "";
    private int idpregunta = 0;
    private CuestionarioPresenter mPresenter;
    // validamos una seleccion sobre el spinner
    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                spinnerRespuesta = true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        bundle = new Bundle();

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            idEncuesta = extras.getString("idEncuesta");
            encuesta = extras.getString("encuesta");
            idTienda = extras.getString("idTienda");
            idEstablecimiento = extras.getString("idEstablecimiento");
            idArchivo = extras.getString("idArchivo");
            numPregunta = extras.getString("numPregunta");
            numRespuesta = extras.getString("numRespuesta");
        }
        // creanos de nuevo las variables del bundle
        bundle.putString("idEncuesta", idEncuesta);
        bundle.putString("encuesta", encuesta);
        bundle.putString("idTienda", idTienda);
        bundle.putString("idEstablecimiento", idEstablecimiento);
        bundle.putString("idArchivo", idArchivo);

        getmPresenter().setGeo(idEncuesta, idEstablecimiento);
        preguntas = new Preguntas();

        if (!numPregunta.equals("FOTO")) { // primera pregunta
            //recuperamos las preguntas
            try {
                arrayPreguntas = new ArrayList<>();
                dao = getmDBHelper().getPregutasDao();

                arrayPreguntas = (ArrayList<Preguntas>) dao.queryBuilder().selectColumns("idpregunta", "pregunta", "multiple", "orden", "idEncuesta").where().eq("idpregunta", numPregunta).and().eq("idEncuesta", idEncuesta).query();
                dao.clearObjectCache();

                //recuperamos las preguntas para esta pregunta
                for (Preguntas item : arrayPreguntas) {

                    String pregunta = item.getPregunta();
                    idpregunta = item.getIdPregunta();
                    // seteamos las preguntas  . . .
                    txtPregunta.setText(pregunta);
                    //armamos el array de respuetas para cada pregunta

                    arrayRespuestas = new ArrayList<>();
                    dao = getmDBHelper().getRespuestasDao();
                    arrayRespuestas = (ArrayList<Respuestas>) dao.queryBuilder().selectColumns("idpregunta", "idrespuesta", "respuesta", "sigPregunta", "respuestaLibre", "idEncuesta").where().eq("idpregunta", idpregunta).and().eq("idEncuesta", idEncuesta).query();
                    dao.clearObjectCache();
                    final ArrayList arrayResp = new ArrayList<String>();
                    final ArrayList arrayOpcCombo = new ArrayList<String>();
                    for (final Respuestas resp : arrayRespuestas) { // armamos las opciones para cada tipo de pregunta

                        if (resp.getIdRespuesta() == 1875) {
                            tipoResp = "1";
                            editRespLibre.setVisibility(View.VISIBLE);
                            numRespuesta = editRespLibre.getText().toString();
                            bundle.putString("numPregunta", resp.getSigPregunta());
                            bundle.putString("numRespuesta", resp.getRespuesta());

                        } else {
                            if (item.getMultiple() == 0) {
                                // RESPUESTAS DE OPCION MULTIPLE

                                tipoResp = "2";
                                spnOpciones.setVisibility(View.VISIBLE);
                                arrayResp.add(resp.getRespuesta());
                                sppinerOpciones(arrayResp, idpregunta);
                                spnOpciones.setOnTouchListener(spinnerOnTouch);
                            } else {
                                tipoResp = "3";
                                /*  Log.e(TAG,"respuesta checkbox " );*/
                                // RESPUESTAS DE MULTI SELECCION
                                arrayOpcCombo.add(resp.getRespuesta());
                                btnOpciones.setVisibility(View.VISIBLE);
                                btnOpciones.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // opcion para multicheck
                                        showOpciones(arrayOpcCombo);

                                    }
                                });
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {

            bundle.putString("numPregunta", numPregunta);
            Intent intent = new Intent(getBaseContext(), FinEncuestaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean respuestaLibre = null;
                Boolean error = false; // validamos que tengamos una respuesta para cada opcion
                if ("2".equals(tipoResp)) {
                    if (spinnerRespuesta != true) {
                        showMessage();
                        error = true;
                    } else {
                        numRespuesta = respSpinner;

                    }
                }
                if ("3".equals(tipoResp)) {
                    if (arrayOpcSelecionadas.isEmpty()) {
                        showMessage();
                        error = true;
                    }

                }
                if ("1".equals(tipoResp)) {
                    if (editRespLibre.getText().toString().equals("")) {
                        showMessageRespLibre();
                        error = true;
                    } else {
                        respuestaLibre = true;
                        numRespuesta = editRespLibre.getText().toString();
                    }
                }
                if (!error) {
                    // TODO guardamos las respuestas en la base de datos
                    if (!arrayOpcSelecionadas.isEmpty()) {
                        onChangeOpcSelecionada();
                    }

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date dt = new Date();
                    fecha = dateFormat.format(dt);
                    respCuestionario = new RespuestasCuestionario();
                    respCuestionario.setIdEncuesta(Integer.parseInt(idEncuesta));
                    respCuestionario.setFecha(fecha);
                    respCuestionario.setIdArchivo(idArchivo);
                    respCuestionario.setIdPregunta(numPregunta);
                    respCuestionario.setIdRespuesta(numRespuesta);
                    respCuestionario.setIdTienda(idTienda);
                    respCuestionario.setIdEstablecimiento(idEstablecimiento);
                    respCuestionario.setRespuestLibre(respuestaLibre);
                    getmPresenter().setRespuestaCuestionario(respCuestionario);

                    Intent intent = new Intent(getBaseContext(), Cuestionario.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    protected int setLayout() {
        return R.layout.activity_cuestionario;
    }

    @Override
    protected String setTitleToolBar() {
        return "Cuestionario";
    }

    @Override
    protected void createPresenter() {
        mPresenter = new CuestionarioPresenter(new CuestionarioInteractor(this));
        mPresenter.attachView(this);
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
    protected CuestionarioPresenter getmPresenter() {
        return mPresenter;
    }

    private DBHelper getmDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return mDBHelper;
    }

    private void sppinerOpciones(ArrayList<String> arrayRespuestas, final int idPregSel) {
        arrayAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item, arrayRespuestas);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spnOpciones.setAdapter(arrayAdapter);
        spnOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Respuestas> arrayRespSel;
                String value = adapterView.getAdapter().getItem(i).toString();

                try {

                    dao = getmDBHelper().getRespuestasDao();
                    arrayRespSel = (ArrayList<Respuestas>) dao.queryBuilder().selectColumns("idrespuesta", "respuesta", "sigPregunta", "respuestaLibre", "idEncuesta")
                            .where().eq("respuesta", value).and().eq("idpregunta", idPregSel).and().eq("idEncuesta", idEncuesta).query();
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
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void showOpciones(final ArrayList<String> arrayResp) {
        boolean[] chekedOpcion = new boolean[arrayResp.size()];
        for (int i = 0; i < arrayResp.size(); i++) {
            chekedOpcion[i] = arrayOpcSelecionadas.contains(arrayResp.get(i));
        }
        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    arrayOpcSelecionadas.add(arrayResp.get(which));
                } else {
                    arrayOpcSelecionadas.remove(arrayResp.get(which));
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        builder.setTitle("Respuestas");

        arreglo = new String[arrayResp.size()];
        for (int j = 0; j < arrayResp.size(); j++) {
            arreglo[j] = arrayResp.get(j);
        }
        builder.setMultiChoiceItems(arreglo, chekedOpcion, coloursDialogListener);
        AlertDialog dialog = builder.create();

        dialog.show();

    }

    //Mensajes de Validacion y Error
    public void showMessage() {

        DialogChoice dialogChoice = DialogFactory.build(getBaseContext(), "Debe seleccionar una respuesta para continuar", true, false);
        dialogChoice.show(getSupportFragmentManager(), DialogAlert.class.getSimpleName());

    }

    // menu inferior
    public void showMessageRespLibre() {

        DialogChoice dialogChoice = DialogFactory.build(getBaseContext(), "Debe contestar esta pregunta", true, false);
        dialogChoice.show(getSupportFragmentManager(), DialogAlert.class.getSimpleName());

    }

    private void onChangeOpcSelecionada() {
        String valSelecionado;
        for (int i = 0; i < arrayOpcSelecionadas.size(); i++) {
            CharSequence value = arrayOpcSelecionadas.get(i);
            valSelecionado = String.valueOf(value);
            /*    Log.e(TAG,"Valor Seleccionado " + valSelecionado);*/
            stringBuilder.append(valSelecionado + " ,");
            //recuperamos los id de las respuestas seleccionadas
            arrayRespuestas = new ArrayList<>();
            String pregSig = null;
            String idResp = null;
            try {
                dao = getmDBHelper().getRespuestasDao();
                arrayRespuestas = (ArrayList<Respuestas>) dao.queryBuilder().selectColumns("idpregunta", "idrespuesta", "sigPregunta", "respuesta")
                        .where().eq("respuesta", valSelecionado)
                        .and().eq("idpregunta", idpregunta)
                        .and().eq("idEncuesta", idEncuesta).query();
                dao.clearObjectCache();
                for (Respuestas respItem : arrayRespuestas) {
                        pregSig = respItem.getSigPregunta();
                        idResp = String.valueOf(respItem.getIdRespuesta());
                        try {
                            dao = getmDBHelper().getRespuestasCuestioanrioDao();
                            respCuestionario = new RespuestasCuestionario();
                            respCuestionario.setIdEncuesta(Integer.parseInt(idEncuesta));
                            respCuestionario.setFecha(fecha);
                            respCuestionario.setIdArchivo(idArchivo);
                            respCuestionario.setIdPregunta(numPregunta);
                            respCuestionario.setIdRespuesta(idResp);
                            respCuestionario.setIdTienda(idTienda);
                            respCuestionario.setRespuestLibre(false);
                            respCuestionario.setIdEstablecimiento(idEstablecimiento);
                            dao.create(respCuestionario);
                            dao.clearObjectCache();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            bundle.putString("numPregunta", pregSig);
            bundle.putString("numRespuesta", idResp);
        }
    }

    @Override
    public void showLoader(boolean show) {
        if (mLoader != null) {
            mLoader.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showError(Throwable throwable) {
        final DialogChoice dialogAlert = DialogFactory.build(this, throwable.getMessage(),
                true, false);
        dialogAlert.show(getSupportFragmentManager(), DialogAlert.class.getSimpleName());
    }

    @Override
    protected void onSuccesPermissions(boolean result) {

        btnSiguiente.setVisibility(checkPermissions() ? View.VISIBLE : View.GONE);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getIntent().getExtras() != null) {
                Bundle extras = getIntent().getExtras();
                numPregunta = extras.getString("numPregunta");
                try {
                    dao = getmDBHelper().getRespuestasCuestioanrioDao();
                    DeleteBuilder<RespuestasCuestionario, Integer> deleteBuilder = dao.deleteBuilder();
                    deleteBuilder.where().eq("idPregunta", numPregunta).and().eq("idEstablecimiento", idEstablecimiento);
                    deleteBuilder.delete();
                    dao.clearObjectCache();
                } catch (SQLException e) {
                    Log.e(TAG, "SqlException onBackPress" + e.getMessage());
                }

                try {

                    dao = getmDBHelper().getGeosDao();
                    DeleteBuilder<GeoLocalizacion, Integer> deleteBuilder = dao.deleteBuilder();
                    deleteBuilder.where().eq("idEstablecimiento", idEstablecimiento).and().eq("idEncuesta", idEncuesta);
                    deleteBuilder.delete();
                    dao.clearObjectCache();

                } catch (SQLException e) {

                }
            }
        }
        super.onKeyDown(keyCode, event);
        return true;
    }

}
