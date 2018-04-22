package com.popgroup.encuestasv3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.CatMaster;
import com.popgroup.encuestasv3.Model.Preguntas;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;
import com.popgroup.encuestasv3.Proyectos.Proyectos;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by jesus.hernandez on 14/12/16.
 */
public class Encuestas extends BaseActivity {
    String TAG = getClass ().getSimpleName ();
    Bundle bundle;
    DBHelper mDBHelper;
    Dao dao;
    @BindView (R.id.txtTitle)
    TextView txtTitle;
    ArrayList<CatMaster> arrayCatmaster;
    ListView listEncuestas;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayEnc;
    ArrayList<Preguntas> arrayPreguntas;
    String idArchivoSel, idEncuesta, idTienda;

    String idpregunta = "0";

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        bundle = new Bundle ();
        Bundle extras = getIntent ().getExtras ();

        idArchivoSel = extras.getString ("idArchivo");
        idEncuesta = extras.getString ("idEncuesta");
        idTienda = extras.getString ("idTienda");
        arrayEnc = new ArrayList<> ();
        try {

            dao = getmDBHelper ().getCatMasterDao ();
            arrayCatmaster = (ArrayList<CatMaster>) dao.queryBuilder ().distinct ().selectColumns ("nombre")
                    .where ().eq ("idArchivo", idArchivoSel).and ().eq ("flag", true).query ();

            for (CatMaster item : arrayCatmaster) {

                arrayEnc.add (item.getNombre ());
            }

            arrayPreguntas = new ArrayList<> ();
            dao = getmDBHelper ().getPregutasDao ();

            arrayPreguntas = (ArrayList<Preguntas>) dao.queryBuilder ().where ().eq ("idEncuesta", idEncuesta).and ().eq ("orden", 1).query ();

            for (Preguntas preguntas : arrayPreguntas) {
                idpregunta = String.valueOf (preguntas.getIdPregunta ());

            }
            dao.clearObjectCache ();

            dao.clearObjectCache ();
        } catch (SQLException e) {
            e.printStackTrace ();
        }

        if (arrayCatmaster != null && arrayCatmaster.size () > 0) {
            adapter = new ArrayAdapter<String> (this, R.layout.simple_list_item, arrayEnc) {
                @NonNull
                @Override
                public View getView (int position, View convertView, ViewGroup parent) {
                    View view = super.getView (position, convertView, parent);
                    TextView textView = (TextView) view.findViewById (android.R.id.text1);
                    textView.setTextSize (TypedValue.COMPLEX_UNIT_DIP, 16);
                    textView.setGravity (Gravity.CENTER);
                    return view;
                }
            };
            listEncuestas = (ListView) findViewById (R.id.listEncuestas);
            listEncuestas.setAdapter (adapter);
            listEncuestas.setOnItemClickListener (new AdapterView.OnItemClickListener () {
                @Override
                public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
                    String value = adapterView.getAdapter ().getItem (i).toString ();
                    String id = null;
                    String idArchivo = null;
                    try {
                        dao = getmDBHelper ().getCatMasterDao ();
                        arrayCatmaster = (ArrayList<CatMaster>) dao.queryBuilder ().distinct ().selectColumns ("idTienda", "idArchivo").where ().in ("idArchivo", idArchivoSel).and ().eq ("nombre", value).query ();
                        for (CatMaster item : arrayCatmaster) {
                            id = item.getIdTienda ();
                            idArchivo = item.getIdArchivo ();
                        }
                        dao.clearObjectCache ();

                    } catch (SQLException e) {
                        e.printStackTrace ();
                    }
                    bundle.putString ("idEncuesta", idEncuesta);
                    bundle.putString ("encuesta", value);
                    bundle.putString ("idEstablecimiento", id);
                    bundle.putString ("idTienda", idTienda);
                    bundle.putString ("idArchivo", idArchivo);
                    bundle.putString ("numPregunta", idpregunta);
                    bundle.putString ("numRespuesta", "0");
                    //limpiemoas la base de deregistros antes de usarla
                    try {
                        dao = getmDBHelper ().getRespuestasCuestioanrioDao ();
                        DeleteBuilder<RespuestasCuestionario, Integer> deleteBuilder = dao.deleteBuilder ();
                        deleteBuilder.where ().eq ("idEstablecimiento", id).and ().eq ("idTienda", idTienda);
                        deleteBuilder.delete ();
                        dao.clearObjectCache ();
                    } catch (SQLException e) {
                        e.printStackTrace ();
                    }
                    Intent intent = new Intent (Encuestas.this, Cuestionario.class);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtras (bundle);
                    startActivity (intent);
                }
            });
        } else {
            showMessage ();
        }


    }

    private DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper (this, DBHelper.class);
        }
        return mDBHelper;
    }

    public void showMessage () {
        AlertDialog alertDialog = new AlertDialog.Builder (this).create ();
        alertDialog.setTitle ("Mensaje");
        alertDialog.setMessage ("No hay encuestas disponibles.. ");
        alertDialog.setButton (AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener () {

            public void onClick (DialogInterface dialog, int which) {
                dialog.dismiss ();
                Intent intent = new Intent (Encuestas.this, Proyectos.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtras (bundle);
                startActivity (intent);
            }
        });

        alertDialog.setOnDismissListener (new DialogInterface.OnDismissListener () {
            @Override
            public void onDismiss (DialogInterface dialogInterface) {
                Intent intent = new Intent (Encuestas.this, Proyectos.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtras (bundle);
                startActivity (intent);
            }
        });
        alertDialog.show ();
    }

    @Override
    protected int setLayout () {
        return R.layout.activity_ini_encuestas;
    }

    @Override
    protected String setTitleToolBar () {
        return "Encuestas";
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
}
