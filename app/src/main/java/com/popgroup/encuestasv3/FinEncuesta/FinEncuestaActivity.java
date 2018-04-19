package com.popgroup.encuestasv3.FinEncuesta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.Base.BasePresenter;
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
    private String idEncuesta, encuesta, idTienda, idEstablecimiento, idArchivo, usuario;

    private FotoEncuesta fotoEncuesta;
    private FinEncuestaPresenter mPresenter;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mLoader.setTextLoader ("Guardando Datos..");
        mLoader.disableTouch (true);
        mLoader.initUI ();

        fotoEncuesta = new FotoEncuesta ().getInstace ();

        Bundle extras = getIntent ().getExtras ();
        //recivimos las variables
        idEncuesta = extras.getString ("idEncuesta").toString ();
        encuesta = extras.getString ("encuesta").toString ();
        idTienda = extras.getString ("idTienda").toString ();
        idArchivo = extras.getString ("idArchivo").toString ();
        idEstablecimiento = extras.getString ("idEstablecimiento").toString ();
        usuario = extras.getString ("usuario").toString ();
        //envio de encuestas y fotos
        btnTerminar.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (NetWorkUtil.checkConnection (getBaseContext ())) {
                    mPresenter.enviarEncuesta (idEncuesta, idEstablecimiento, idTienda, usuario);
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
        final AlertDialog alertD = new AlertDialog.Builder (this).create ();
        alertD.setTitle ("Mensaje");
        alertD.setMessage ("No cuentas con internet.\nLos datos se guardaran localmente. ");
        alertD.setButton (AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener () {
            public void onClick (DialogInterface dialog, int which) {
                dialog.dismiss ();
                mPresenter.saveEncuesta (idEstablecimiento, idEncuesta);
                mPresenter.saveFotosEnc (fotoEncuesta, idEstablecimiento, idEncuesta);
                Intent i = new Intent (FinEncuestaActivity.this, MainActivity.class);
                i.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity (i);

            }
        });

        alertD.setOnShowListener (new DialogInterface.OnShowListener () {
            @Override
            public void onShow (DialogInterface arg0) {
                alertD.getButton (AlertDialog.BUTTON_POSITIVE).setTextColor (getResources ().getColor (R.color.colorPrimary));

            }
        });

        alertD.show ();
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
