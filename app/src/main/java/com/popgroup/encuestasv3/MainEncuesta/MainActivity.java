package com.popgroup.encuestasv3.MainEncuesta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.Cuestionario.Cuestionario;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Dialog.DialogAlert;
import com.popgroup.encuestasv3.Dialog.DialogChoice;
import com.popgroup.encuestasv3.Dialog.DialogFactory;
import com.popgroup.encuestasv3.Login.LoginActivity;
import com.popgroup.encuestasv3.Model.Preguntas;
import com.popgroup.encuestasv3.Model.Proyecto;
import com.popgroup.encuestasv3.Model.TipoEncuesta;
import com.popgroup.encuestasv3.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements IMainView, View.OnClickListener {

    public static String mUsuario;
    @BindView (R.id.btnCambiarUser)
    Button btnCambiarUser;
    @BindView (R.id.btnInicio)
    Button btnInicio;
    @BindView (R.id.btnEncPnedientes)
    Button btnEncPendientes;
    @BindView (R.id.btnFotosPendientes)
    Button btnFotosPendientes;
    @BindView (R.id.txtTelefono)
    TextView txtLog;
    @BindView (R.id.txtUsuario)
    TextView txtUser;
    @BindView (R.id.txtEncPendientes)
    TextView txtPendientes;
    @BindView (R.id.txtEncTerminadas)
    TextView txtTerminadas;

    DBHelper mDBHelper;
    private String TAG = getClass ().getSimpleName ();
    private int encuestasPendientes;
    private MainPresenter mPresenter;
    private String idEncuesta;
    private String idpregunta;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mLoader.setTextLoader ("Enviando Datos..");
        mLoader.disableTouch (true);
        mLoader.initUI ();

        mPresenter.getUsuario ();
        mPresenter.validateEncPendientes ();
        mPresenter.validateFotosPendientes ();

        btnCambiarUser.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                mPresenter.nextLoginOperation ();
            }
        });
        getEncPendientes();
        getEncTerminada();
        getProyecto ();
        getEncuesta ();
        getPregrunta ();
        btnInicio.setOnClickListener (this);
        btnEncPendientes.setOnClickListener (this);
        btnFotosPendientes.setOnClickListener (this);
        txtTerminadas.setText("Encuestas Terminadas " + getEncTerminada());
        txtPendientes.setText("Encuestas Pendientes " + getEncPendientes());
    }

    @Override
    protected int setLayout () {
        return R.layout.activity_main;
    }

    @Override
    protected String setTitleToolBar () {
        return "Encuestas";
    }

    @Override
    protected void createPresenter () {
        mPresenter = new MainPresenter (new MainInteractor (this));
        mPresenter.attachView (this);
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
    protected MainPresenter getmPresenter () {
        return mPresenter != null ? (MainPresenter) mPresenter : null;
    }

    private String getProyecto () {
        String idproyecto = null;
        long max = 0;
        Dao dao;
        try {
            dao = getmDBHelper ().getProyectoDao ();
            max = dao.queryRawValue ("SELECT max(idproyecto) from proyecto");
            Proyecto proyecto = (Proyecto) dao.queryBuilder ().where ().eq ("idproyecto", max).queryForFirst ();
            idproyecto = String.valueOf (proyecto.getIdproyecto ());
            dao.clearObjectCache ();
            Log.e (TAG, "idproyecto" + idproyecto);
        } catch (SQLException e) {
            e.printStackTrace ();
        }
        return idproyecto;
    }

    @Override
    public void showUsuario (Boolean show, String usuario) {
        this.mUsuario = usuario;
        txtUser.setVisibility (show ? View.VISIBLE : View.GONE);
        txtUser.setText (usuario);
    }

    @Override
    public void showAlert () {
        final DialogChoice dialogAlert = DialogFactory.build(this, "Cambiar de usuario borrara los datos existentes", true, true, mPresenter);
        dialogAlert.show(getSupportFragmentManager(), DialogAlert.class.getSimpleName());
    }

    @Override
    public void nextOperation () {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override

    public void showButtonEncuestasPendientes (Boolean show, Integer pendientes) {
        if (btnEncPendientes != null) {
            encuestasPendientes = pendientes;
            btnEncPendientes.setVisibility(show ? View.VISIBLE : View.GONE);
            btnEncPendientes.setText("Encuestas Pendientes " + " ( " + pendientes + " )");
        }
    }

    @Override
    public void showButtonFotosPendientes (Boolean show, Integer pendientes) {
        if (btnFotosPendientes != null) {
            btnFotosPendientes.setVisibility(show ? View.VISIBLE : View.GONE);
            btnFotosPendientes.setText("Fotos Pendientes " + " ( " + pendientes + " )");
        }
    }

    private DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper (this, DBHelper.class);
        }
        return mDBHelper;
    }

    private String getEncuesta () {
        long max = 0;
        Dao dao;
        try {
            dao = getmDBHelper ().getTipoEncDao ();
            max = dao.queryRawValue ("SELECT max(idencuesta) from tipoencuesta");
            Log.e (TAG, "max idencuesta" + max);
            TipoEncuesta tipoEncuesta = (TipoEncuesta) dao.queryBuilder ().where ()
                    .eq ("idencuesta", max).and ().eq ("idproyecto", getProyecto ()).queryForFirst ();
            idEncuesta = String.valueOf (tipoEncuesta.getIdEncuesta ());
            dao.clearObjectCache ();
            Log.e (TAG, "idencuesta" + idEncuesta);
        } catch (SQLException e) {
            e.printStackTrace ();
        }
        return idEncuesta;
    }

    private int getEncPendientes () {
        Dao dao;
        int encuestas = 0;
        try {
            dao = getmDBHelper().getCatMasterDao();
            encuestas = (int) dao.queryRawValue("SELECT COUNT(*) FROM catmaster WHERE flag = 1 ; ");
            Log.e(TAG, "encuestas " + encuestas);
            dao.clearObjectCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return encuestas;
    }

    private int getEncTerminada () {
        Dao dao;
        int encuestas = 0;
        try {
            dao = getmDBHelper().getCatMasterDao();
            encuestas = (int) dao.queryRawValue("SELECT COUNT(*) FROM catmaster WHERE flag = 0 ; ");
            Log.e(TAG, "encuestas " + encuestas);
            dao.clearObjectCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return encuestas;
    }

    private String getPregrunta () {
        List<Preguntas> arrayPreguntas = new ArrayList<> ();
        Dao dao;
        try {
            dao = getmDBHelper ().getPregutasDao ();
            arrayPreguntas = (ArrayList<Preguntas>) dao.queryBuilder ().where ().eq ("idEncuesta", getEncuesta ()).and ().eq ("orden", 1).query ();
            for (Preguntas preguntas : arrayPreguntas) {
                idpregunta = String.valueOf (preguntas.getIdPregunta ());
            }
            dao.clearObjectCache ();
        } catch (SQLException e) {
            e.printStackTrace ();
        }
        return idpregunta;
    }

    @Override
    public void showLoader (boolean show) {
        if (mLoader != null) {
            mLoader.setVisibility (show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showError (Throwable throwable) {
        final DialogChoice dialogAlert = DialogFactory.build (this, throwable.getMessage (),
                true, false);
        dialogAlert.show (getSupportFragmentManager (), DialogAlert.class.getSimpleName ());
    }

    @Override
    public void onBackPressed () {
        Intent a = new Intent (Intent.ACTION_MAIN);
        a.addCategory (Intent.CATEGORY_HOME);
        a.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity (a);
    }

    @Override
    public void onClick (View view) {
        if (view.getId () == R.id.btnInicio) {
            iniciarProceso ();
        } else if (view.getId () == R.id.btnEncPnedientes) {
            mPresenter.enviarEncuestaPendientes (mUsuario, encuestasPendientes);
        } else if (view.getId () == R.id.btnFotosPendientes) {
            mPresenter.enviarFotosPendientes ();
        }
    }

    public void iniciarProceso () {
        Bundle bundle = new Bundle ();
        Intent i = new Intent (MainActivity.this, Cuestionario.class);
        i.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        bundle.putString ("idEncuesta", idEncuesta);
        bundle.putString ("numPregunta", idpregunta);
        bundle.putString ("numRespuesta", "0");
        i.putExtras (bundle);
        startActivity (i);

    }





}
