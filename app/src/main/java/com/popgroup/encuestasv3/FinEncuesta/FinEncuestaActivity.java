package com.popgroup.encuestasv3.FinEncuesta;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Dialog.DialogAlert;
import com.popgroup.encuestasv3.Dialog.DialogChoice;
import com.popgroup.encuestasv3.Dialog.DialogFactory;
import com.popgroup.encuestasv3.MainEncuesta.MainActivity;
import com.popgroup.encuestasv3.Model.FotoEncuesta;
import com.popgroup.encuestasv3.R;
import com.popgroup.encuestasv3.Utility.NetWorkUtil;

import butterknife.BindView;

/**
 * Created by jesus.hernandez on 26/12/16.
 * envia los datos de la encuesta al servidor
 */
public class FinEncuestaActivity extends BaseActivity implements IFinEncuestaView {
    private final String TAG = getClass ().getSimpleName ();

    @BindView (R.id.btnTerminar)
    Button btnTerminar;
    private String idEncuesta, encuesta, idTienda, idEstablecimiento, idArchivo;

    private FotoEncuesta fotoEncuesta;
    private FinEncuestaPresenter mPresenter;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mLoader.setTextLoader ("Guardando Datos..");
        mLoader.disableTouch (true);
        mLoader.initUI ();

        fotoEncuesta = new FotoEncuesta ().getInstace ();

        final Bundle extras = getIntent ().getExtras ();
        //recivimos las variables
        if (getIntent ().getExtras () != null) {
            idEncuesta = extras.getString ("idEncuesta");
            encuesta = extras.getString ("encuesta");
            idTienda = extras.getString ("idTienda");
            idArchivo = extras.getString ("idArchivo");
            idEstablecimiento = extras.getString ("idEstablecimiento");
        }

        //envio de encuestas y fotos
        btnTerminar.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (NetWorkUtil.checkConnection (getBaseContext ())) {
                    Log.e (TAG, "bundle");
                    mPresenter.enviarEncuesta (idEncuesta, idEstablecimiento, idTienda, MainActivity.mUsuario);
                } else {
                    showMessage ();

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
        mPresenter = new FinEncuestaPresenter (new FinEncuestaInteractor (this));
        mPresenter.attachView (this);
    }

    @Override
    protected BasePresenter getmPresenter () {
        return null;
    }

    public void showMessage () {

        final DialogChoice dialogAlert = DialogFactory.build(this, "No cuentas con internet.\nLos datos se guardaran localmente.", true, true, mPresenter, idEstablecimiento, idEncuesta, fotoEncuesta);
        dialogAlert.show(getSupportFragmentManager(), DialogAlert.class.getSimpleName());
    }

    @Override
    public void showLoader (boolean show) {
        if (mLoader != null) {
            mLoader.setVisibility (show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showError (Throwable throwable) {
        Toast.makeText (this, "Error " + throwable.getMessage (), Toast.LENGTH_SHORT).show ();
    }
}
