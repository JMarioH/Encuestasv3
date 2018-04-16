package com.popgroup.encuestasv3.FinEncuesta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.popgroup.encuestasv3.AsynckTask.AsynckEncuestas;
import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.MainActivity;
import com.popgroup.encuestasv3.Model.CatMaster;
import com.popgroup.encuestasv3.Model.FotoEncuesta;
import com.popgroup.encuestasv3.Model.Fotos;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;
import com.popgroup.encuestasv3.R;
import com.popgroup.encuestasv3.Utility.Connectivity;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by jesus.hernandez on 26/12/16.
 * envia los datos de la encuesta al servidor
 */
public class FinEncuestaActivity extends BaseActivity implements IFinEncuestaView {
    private final String TAG = getClass().getSimpleName();
    @BindView (R.id.txtTitle)
    TextView txtTitle;
    @BindView (R.id.btnTerminar)
    Button btnTerminar;
    private String idEncuesta, encuesta, idTienda, idEstablecimiento, idArchivo, usuario;
    private Bundle bundle;
    private Connectivity connectivity;
    private Dao dao;
    private DBHelper mDBHelper;
    private FotoEncuesta fotoEncuesta;
    private String base64;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = new Bundle();
        fotoEncuesta = new FotoEncuesta().getInstace();

        Bundle extras = getIntent().getExtras();
        connectivity = new Connectivity();
        mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);

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
            public void onClick (View view) {
                if (connectivity.isConnected(getBaseContext())) {
                    new AsynckEncuestas(FinEncuestaActivity.this, idEncuesta, idEstablecimiento, idTienda, usuario).execute();
                } else {
                    showMessage();
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
        mPresenter = new FinEncuestaPresenter(new FinEncuestaInteractor(this));
        mPresenter.attachView(this);
    }

    @Override
    protected BasePresenter getmPresenter () {
        return null;
    }

    public void showMessage () {
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.setTitle("Mensaje");
        alertD.setMessage("No cuentas con internet.\nLos datos se guardaran localmente. ");
        alertD.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog, int which) {
                dialog.dismiss();
                guadarLocal();
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

    private void guadarLocal () {

        // datos guaradados localmente

        try {
            // cambiamos el status de la lista de encuestas
            dao = getmDBHelper().getCatMasterDao();
            UpdateBuilder<CatMaster, Integer> updateBuilder = dao.updateBuilder();
            updateBuilder.updateColumnValue("flag", false);
            updateBuilder.where().eq("idTienda", idEstablecimiento).and().eq("idEncuesta", idEncuesta);
            updateBuilder.update();
            dao.clearObjectCache();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "SQLEXception CatMaster" + e.getMessage());
        }
        try {
            //cambiamos el estatus de las respuestas
            dao = getmDBHelper().getRespuestasCuestioanrioDao();
            UpdateBuilder<RespuestasCuestionario, Integer> updateBuilder = dao.updateBuilder();
            updateBuilder.updateColumnValue("flag", true);
            updateBuilder.where().eq("idEstablecimiento", idEstablecimiento).and().eq("idEncuesta", idEncuesta);
            updateBuilder.update();
            dao.clearObjectCache();
            guardaFotos();

        } catch (SQLException e) {
            Log.e(TAG, "SQlException Respuestas Local" + e.getMessage());
        }
        Toast.makeText(this, "guardando pendientes ", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(i);

    }

    private DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return mDBHelper;
    }

    public void guardaFotos () {
        int x = 0;
        ArrayList<String> arrayBase64;
        if (fotoEncuesta.getArrayFotos() != null && fotoEncuesta.getArrayFotos().size() > 0) {
            arrayBase64 = fotoEncuesta.getArrayFotos();
            try {
                dao = getmDBHelper().getFotosDao();
                for (x = 0; x < arrayBase64.size(); x++) {
                    Fotos Objfotos = new Fotos();
                    base64 = fotoEncuesta.getArrayFotos().get(x);
                    Objfotos.setIdEstablecimiento(Integer.parseInt(idEstablecimiento));
                    Objfotos.setIdEncuesta(Integer.parseInt(idEncuesta));
                    Objfotos.setNombre(fotoEncuesta.getNombre().get(x));
                    Objfotos.setBase64(base64);
                    dao.create(Objfotos);
                }
                dao.clearObjectCache();
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e(TAG, "SQLException Foto" + e.getMessage());
            }

        }
    }

    @Override
    public void showLoader (boolean show) {

    }

    @Override
    public void showError (Throwable throwable) {

    }

    @Override
    public void nextAction (Object result) {

    }

    @Override
    public void actionMenu () {

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menuInicio) {
            //Display Toast
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.menuSalir) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

}
