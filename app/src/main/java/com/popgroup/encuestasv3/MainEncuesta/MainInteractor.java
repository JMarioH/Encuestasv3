package com.popgroup.encuestasv3.MainEncuesta;

import android.app.Activity;
import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.ICallBack;
import com.popgroup.encuestasv3.DataBase.DBHelper;

/**
 * Created by JMario. on 18/4/2018.
 */

public class MainInteractor extends BaseInteractor implements IMainInteractor {
    private final String TAG = getClass().getSimpleName();

    private ICallBack callBack;
    private Context context;
    private Dao dao;

    public MainInteractor (Activity activity) {
        this.context = activity;
    }

    @Override
    public void enviarEncPendientes () {

    }

    @Override
    public void enviarFotosPendientes () {

    }

    @Override
    public void onDestroy () {
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
    }

    @Override
    public void attachCallBack (ICallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        }
        return mDBHelper;
    }
}
