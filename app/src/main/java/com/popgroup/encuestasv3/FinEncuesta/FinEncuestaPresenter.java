package com.popgroup.encuestasv3.FinEncuesta;

import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Base.IView;

/**
 * Created by JMario. on 16/4/2018.
 */

public class FinEncuestaPresenter extends BasePresenter {

    public FinEncuestaPresenter (BaseInteractor mBaseInteractor) {
        super(mBaseInteractor);
        mBaseInteractor.attachCallBack(this);
    }

    @Override
    public void onSuccess (Object result) {

    }

    @Override
    public void onFailed (Throwable throwable) {

    }

    @Override
    public IFinEncuestaView getView () {
        return mView != null ? (IFinEncuestaView) mView : null;
    }

    @Override
    public void attachView (IView view) {
        super.attachView(view);
    }

    @Override
    public BaseInteractor getInteractor () {
        return mBaseInteractor != null ? (FinEncuestaInteractor) mBaseInteractor : null;
    }
}
