package com.popgroup.encuestasv3.Login;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.ICallBack;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.User;
import com.popgroup.encuestasv3.Utility.Connectivity;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Orion on 14/04/2018.
 */

public class LoginInteractorImpl extends BaseInteractor implements LoginInteractor {
    private static final String TAG = LoginInteractorImpl.class.getSimpleName ();
    public Dao dao;
    private ICallBack mCallback;
    private Context context;
    private Connectivity connectivity;
    private ArrayList<User> arrayUser;

    public LoginInteractorImpl (Context ctx) {
        this.context = ctx;
        connectivity = new Connectivity ();
    }

    @Override
    public void login (String user, String pass) {

        if (connectivity.isConnected (context)) {
            new LoginAsynck (context, user, pass, mCallback, dao, getmDBHelper ()).execute ();
        } else {
            mCallback.onFailed (new Throwable ("Debe conectarse a una red"));
        }
    }

    @Override
    public User getUser () {
        User usuario = null;
        try {
            dao = getmDBHelper ().getUserDao ();
            arrayUser = (ArrayList<User>) dao.queryForAll ();
            dao.queryForAll ();
            for (User item : arrayUser) {
                usuario = item;
            }
            return usuario;
        } catch (SQLException e) {
            Log.d (TAG, "sql" + e);
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
        this.mCallback = callBack;
    }

    @Override
    protected DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper (context, DBHelper.class);
        }
        return mDBHelper;
    }
}
