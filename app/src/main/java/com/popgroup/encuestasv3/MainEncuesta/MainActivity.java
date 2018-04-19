package com.popgroup.encuestasv3.MainEncuesta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Login.LoginActivity;
import com.popgroup.encuestasv3.Proyectos;
import com.popgroup.encuestasv3.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements IMainView, View.OnClickListener {

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
    private String TAG = getClass ().getSimpleName ();
    private int encuestasPendientes;
    private String mUsuario;
    private DBHelper mDBHelper;
    private MainPresenter mPresenter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        ButterKnife.bind (this);
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

        btnInicio.setOnClickListener (this);
        btnEncPendientes.setOnClickListener (this);
        btnFotosPendientes.setOnClickListener (this);
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
    public void onClick (View view) {
        if (view.getId () == R.id.btnInicio) {
            iniciarProceso ();
        } else if (view.getId () == R.id.btnEncPnedientes) {
            mPresenter.enviarEncuestaPendientes (mUsuario, encuestasPendientes);
        } else if (view.getId () == R.id.btnFotosPendientes) {
            mPresenter.enviarFotosPendientes ();
        }
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

    @Override
    public void showUsuario (Boolean show, String usuario) {
        mUsuario = usuario;
        txtUser.setVisibility (show ? View.VISIBLE : View.GONE);
        txtUser.setText (usuario);
    }

    @Override
    public void showAlert () {
        final AlertDialog alertDialog = new AlertDialog.Builder (MainActivity.this).create ();
        alertDialog.setTitle ("Aviso");

        alertDialog.setMessage ("Cambiar de usuario borrara los datos existentes");
        alertDialog.setButton (AlertDialog.BUTTON_POSITIVE, "Aceptar",
                new DialogInterface.OnClickListener () {
                    public void onClick (DialogInterface dialog, int which) {
                        mPresenter.clearDataBase ();
                        dialog.dismiss ();
                        Intent intent = new Intent (getBaseContext (), LoginActivity.class);
                        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity (intent);
                    }
                });
        alertDialog.setButton (AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialog, int i) {
                dialog.dismiss ();
            }
        });
        alertDialog.setOnShowListener (new DialogInterface.OnShowListener () {
            @Override
            public void onShow (DialogInterface arg0) {
                alertDialog.getButton (AlertDialog.BUTTON_POSITIVE).setTextColor (getResources ().getColor (R.color.colorPrimary));
                alertDialog.getButton (AlertDialog.BUTTON_NEGATIVE).setTextColor (getResources ().getColor (R.color.colorPrimary));
            }
        });


        alertDialog.show ();
    }

    @Override
    public void showButtonEncuestasPendientes (Boolean show, Integer pendientes) {
        if (btnEncPendientes != null) {
            encuestasPendientes = pendientes;
            btnEncPendientes.setVisibility (show ? View.VISIBLE : View.GONE);
            btnEncPendientes.setText ("Encuestas Pendientes " + " ( " + pendientes + " )");
        }
    }
    @Override
    public void showButtonFotosPendientes (Boolean show, Integer pendientes) {
        if (btnFotosPendientes != null) {
            btnFotosPendientes.setVisibility (show ? View.VISIBLE : View.GONE);
            btnFotosPendientes.setText ("Fotos Pendientes " + " ( " + pendientes + " )");
        }
    }

    public void iniciarProceso () {

        Intent i = new Intent (MainActivity.this, Proyectos.class);
        i.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity (i);

    }

    @Override
    public void showLoader (boolean show) {
        if (mLoader != null) {
            mLoader.setVisibility (show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showError (Throwable throwable) {
        final AlertDialog alertDialog = new AlertDialog.Builder (MainActivity.this).create ();
        alertDialog.setTitle ("Mensaje");
        alertDialog.setMessage (throwable.getMessage ());
        alertDialog.setButton (AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener () {
            public void onClick (DialogInterface dialog, int which) {
                dialog.dismiss ();
            }
        });
        alertDialog.setOnShowListener (new DialogInterface.OnShowListener () {
            @Override
            public void onShow (DialogInterface arg0) {
                alertDialog.getButton (AlertDialog.BUTTON_POSITIVE).setTextColor (getResources ().getColor (R.color.colorPrimary));
            }
        });
        alertDialog.show ();
    }

    @Override
    public void onBackPressed () {
        Intent a = new Intent (Intent.ACTION_MAIN);
        a.addCategory (Intent.CATEGORY_HOME);
        a.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity (a);
    }


}
