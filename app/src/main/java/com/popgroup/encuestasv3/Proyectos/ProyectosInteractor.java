package com.popgroup.encuestasv3.Proyectos;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.ICallBack;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.Cliente;
import com.popgroup.encuestasv3.Model.Proyecto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orion on 21/04/2018.
 */

public class ProyectosInteractor extends BaseInteractor implements IProyectoInteractor {
    private String TAG = getClass ().getSimpleName ();
    private Context context;
    private Dao dao;
    private String nomCliente;
    private ICallBack mCallBack;

    public ProyectosInteractor (Context ctx) {
        this.context = ctx;
    }

    @Override
    public String getCliente () {
        List<Cliente> clienteList = new ArrayList<> ();
        String user = "";
        try {

            dao = getmDBHelper ().getClienteDao ();
            clienteList = (List<Cliente>) dao.queryBuilder ().distinct ().selectColumns ("nombre").query ();
            if (clienteList != null && clienteList.size () > 0) {

                for (Cliente item : clienteList) {
                    nomCliente = item.getNombre ();
                }
                dao.clearObjectCache ();
                return nomCliente;
            } else {
                mCallBack.onFailed (new Throwable ("Debe hacer Login de nuevo para continuar. "));
                return "";
            }

        } catch (SQLException e) {
            Log.e (TAG, "SQLException" + e.getMessage ());

            return user = "";
        }
    }

    @Override
    public List<Proyecto> getListProyectos () {
        List<Proyecto> proyectoList = new ArrayList<> ();
        try {

            dao = getmDBHelper ().getProyectoDao ();
            proyectoList = (List<Proyecto>) dao.queryForAll ();
            dao.clearObjectCache ();
            if (proyectoList != null && proyectoList.size () > 0) {
                return proyectoList;
            } else {
                mCallBack.onFailed (new Throwable ("Debe hacer Login de nuevo para continuar. "));
                return null;
            }


        } catch (SQLException e) {
            Log.e (TAG, "SQLException" + e.getMessage ());
            return null;
        }
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper ();
            mDBHelper = null;
        }
    }

    @Override
    public void attachCallBack (ICallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    protected DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper (context, DBHelper.class);
        }
        return mDBHelper;
    }
}
