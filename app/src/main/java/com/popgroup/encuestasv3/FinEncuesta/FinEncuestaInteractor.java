package com.popgroup.encuestasv3.FinEncuesta;

import android.app.Activity;
import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.ICallBack;
import com.popgroup.encuestasv3.DataBase.DBHelper;

/**
 * Created by JMario. on 16/4/2018.
 */

public class FinEncuestaInteractor extends BaseInteractor {

    private Context ctx;

    public FinEncuestaInteractor (Activity activity) {
        this.ctx = activity;
    }

    @Override
    public void attachCallBack (ICallBack callBack) {

    }

    @Override
    protected DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(ctx, DBHelper.class);
        }
        return mDBHelper;
    }
}
