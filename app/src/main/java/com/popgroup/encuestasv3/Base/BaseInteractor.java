package com.popgroup.encuestasv3.Base;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.popgroup.encuestasv3.DataBase.DBHelper;

/**
 * Created by Orion on 14/04/2018.
 */

public abstract class BaseInteractor<T> implements LifeCycle {

    public DBHelper mDBHelper;

    @Override
    public void onCreate () {
    }

    @Override
    public void onResume () {
    }

    @Override
    public void onStop () {
    }

    @Override
    public void onDestroy () {
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper ();
            mDBHelper = null;
        }
    }

    public abstract void attachCallBack (final ICallBack<T> callBack);

    protected abstract DBHelper getmDBHelper ();
}
