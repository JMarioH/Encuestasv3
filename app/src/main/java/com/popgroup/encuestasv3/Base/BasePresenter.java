package com.popgroup.encuestasv3.Base;

/**
 * Created by Orion on 14/04/2018.
 */

public abstract class BasePresenter<T> implements LifeCycle, ICallBack<T> {

    protected IView<T> mView;

    protected BaseInteractor mBaseInteractor;

    protected Object[] mParams;

    public BasePresenter (BaseInteractor mBaseInteractor) {
        this.mBaseInteractor = mBaseInteractor;
    }

    public abstract IView getView ();

    public void attachView (final IView<T> view) {
        this.mView = view;
    }

    public Object[] getmParams () {
        return mParams;
    }

    public void setmParams (Object[] mParams) {
        this.mParams = mParams;
    }

    @Override
    public void onCreate () {

        if (getInteractor () != null) {
            getInteractor ().onCreate ();
        }
    }

    public abstract BaseInteractor getInteractor ();

    @Override
    public void onResume () {

        if (getInteractor () != null) {
            getInteractor ().onResume ();
        }
    }

    @Override
    public void onStop () {

        if (getInteractor () != null) {
            getInteractor ().onStop ();
        }
    }

    @Override
    public void onDestroy () {
        detachView ();
        if (getInteractor () != null) {
            getInteractor ().onDestroy ();
        }
    }

    public void detachView () {
        mView = null;
    }
}