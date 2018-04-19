package com.popgroup.encuestasv3.MainEncuesta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.popgroup.encuestasv3.AsynckTask.AsyncUploadFotos;
import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Login.LoginActivity;
import com.popgroup.encuestasv3.Model.Fotos;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;
import com.popgroup.encuestasv3.Model.User;
import com.popgroup.encuestasv3.Proyectos;
import com.popgroup.encuestasv3.R;
import com.popgroup.encuestasv3.Utility.Connectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


public class MainActivity extends BaseActivity implements IMainView {

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
    boolean connectionAvailable;
    private Dao dao;
    private Bundle bundle;
    private ArrayList<Fotos> fotosPendientes;
    private ArrayList<RespuestasCuestionario> encuestasPendientes;
    private ArrayList<User> arrayUser;
    private String mUsuario;
    private TextView txtTitle;
    private Connectivity connectivity;
    private String TAG = getClass ().getSimpleName ();
    private DBHelper mDBHelper;
    private MainPresenter mPresenter;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        ButterKnife.bind (this);

        bundle = new Bundle ();
        connectionAvailable = connectivity.isConnected (this);
        mPresenter.getUsuario ();
        mPresenter.validateEncPendientes ();
        mPresenter.validateFotosPendientes ();

        btnCambiarUser.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                //revisamos si existe un telefono logeado
                try {
                    dao = getmDBHelper ().getUserDao ();
                    arrayUser = (ArrayList<User>) dao.queryForAll ();
                    for (User item : arrayUser) {
                        mUsuario = item.getNombre ();

                    }
                    dao.clearObjectCache ();

                } catch (SQLException e) {
                    Log.e (TAG, "sqlException " + e);
                }
                if (!arrayUser.isEmpty ()) {
                    showAlert ();
                } else {
                    Intent i = new Intent (MainActivity.this, LoginActivity.class);
                    i.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity (i);
                }
            }
        });
        btnInicio.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                iniciarProceso ();
            }
        });
        btnEncPendientes.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                mPresenter.enviarEncuestaPendientes (mUsuario, encuestasPendientes);
            }
        });
        btnFotosPendientes.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                mPresenter.enviarFotosPendientes ();
            }
        });

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
        mPresenter = new MainPresenter(new MainInteractor(this));
        mPresenter.attachView(this);
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

    private DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper (this, DBHelper.class);
        }
        return mDBHelper;
    }

    public void showAlert () {
        final AlertDialog alertDialog = new AlertDialog.Builder (MainActivity.this).create ();
        alertDialog.setTitle ("Aviso");

        alertDialog.setMessage ("Cambiar de usuario borrara los datos existentes");
        alertDialog.setButton (AlertDialog.BUTTON_POSITIVE, "Si",
                new DialogInterface.OnClickListener () {
                    public void onClick (DialogInterface dialog, int which) {

                        try { // recuperamos los datos de la base de datos

                            dao = getmDBHelper ().getUserDao ();
                            DeleteBuilder<User, Integer> deleteBuilder = dao.deleteBuilder ();
                            deleteBuilder.delete ();
                            dao.clearObjectCache ();
                            txtUser.setText ("00000000");

                            dao = getmDBHelper ().getUserDao ();
                            dao.deleteBuilder ().delete ();
                            dao.clearObjectCache ();

                            dao = getmDBHelper ().getProyectoDao ();
                            dao.deleteBuilder ().delete ();
                            dao.clearObjectCache ();

                            dao = getmDBHelper ().getClienteDao ();
                            dao.deleteBuilder ().delete ();
                            dao.clearObjectCache ();

                            dao = getmDBHelper ().getTipoEncDao ();
                            dao.deleteBuilder ().delete ();
                            dao.clearObjectCache ();

                            dao = getmDBHelper ().getCatMasterDao ();
                            dao.deleteBuilder ().delete ();
                            dao.clearObjectCache ();

                            dao = getmDBHelper ().getPregutasDao ();
                            dao.deleteBuilder ().delete ();
                            dao.clearObjectCache ();

                            dao = getmDBHelper ().getRespuestasDao ();
                            dao.deleteBuilder ().delete ();
                            dao.clearObjectCache ();

                            dao = getmDBHelper ().getRespuestasCuestioanrioDao ();
                            dao.deleteBuilder ().delete ();
                            dao.clearObjectCache ();

                        } catch (SQLException e) {
                            Log.e (TAG, "sql->" + e);
                        }
                        dialog.dismiss ();
                        bundle.putString ("bandera", "1");
                        Intent intent = new Intent (getBaseContext (), LoginActivity.class);
                        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtras (bundle);
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

    public void iniciarProceso () {

        Intent i = new Intent (MainActivity.this, Proyectos.class);
        i.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity (i);

    }

    @Override
    public void showUsuario (Boolean show, String usuario) {
        txtUser.setVisibility (show ? View.VISIBLE : View.GONE);
        txtUser.setText (usuario);
    }

    @Override
    public void sincronizarEncuestas () {

    }

    @Override
    public void sincronizarFotos () {

    }

    @Override
    public void showButtonEncuestasPendientes (Boolean show, Integer pendientes) {
        if (btnEncPendientes != null) {
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

    public void fotosPendientes () {
        connectionAvailable = connectivity.isConnected(getBaseContext());
        JSONArray jsonFotos;
        ArrayList<NameValuePair> datosPost = null;
        if (connectionAvailable) {
            try {
                dao = getmDBHelper().getFotosDao();
                fotosPendientes = (ArrayList<Fotos>) dao.queryForAll();

                String nomArchivo;
                int j = 0;
                jsonFotos = new JSONArray();
                try {
                    for (int x = 0; x < fotosPendientes.size(); x++) {
                        datosPost = new ArrayList<>();
                        JSONObject jsonFoto = new JSONObject();

                        nomArchivo = fotosPendientes.get(x).getIdEncuesta() + "_" + fotosPendientes.get(x).getIdEstablecimiento() + "_" + fotosPendientes.get(x).getNombre() + "_" + x + ".jpg";
                        jsonFoto.put("idEstablecimiento", fotosPendientes.get(x).getIdEstablecimiento());
                        jsonFoto.put("idEncuesta", fotosPendientes.get(x).getIdEncuesta());
                        jsonFoto.put("nombreFoto", nomArchivo);
                        jsonFoto.put("base64", fotosPendientes.get(x).getBase64());
                        jsonFotos.put(jsonFoto);
                        j++;
                        datosPost.add(new BasicNameValuePair("subeFotos", jsonFotos.toString()));
                        new AsyncUploadFotos(this, datosPost, String.valueOf(fotosPendientes.get(x).getIdEncuesta()), String.valueOf(fotosPendientes.get(x).getIdEstablecimiento())).execute();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "jsonE -> " + e);
                }

                if (j == fotosPendientes.size()) {

                    dao = getmDBHelper().getFotosDao();
                    DeleteBuilder<Fotos, Integer> deleteBuilder = dao.deleteBuilder();
                    deleteBuilder.delete();
                    dao.clearObjectCache();

                    btnFotosPendientes.setVisibility(View.GONE);
                }

                dao.clearObjectCache();
            } catch (SQLException e) {
                Log.e(TAG, "sql->" + e);
            }

        } else {
            // showMessage();
        }
    }

    @Override
    public void showLoader (boolean show) {
        if (mLoader != null) {
            mLoader.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showError (Throwable throwable) {
        AlertDialog alertDialog = new AlertDialog.Builder (MainActivity.this).create ();
        alertDialog.setTitle ("Mensaje");
        alertDialog.setMessage (throwable.getMessage ());
        alertDialog.setButton (AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener () {
            public void onClick (DialogInterface dialog, int which) {
                dialog.dismiss ();
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
