package com.popgroup.encuestasv3.TipoEncuesta;

import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Base.IView;
import com.popgroup.encuestasv3.Login.ILoginView;
import com.popgroup.encuestasv3.Model.TipoEncuesta;

import java.util.List;

/**
 * Created by Orion on 22/04/2018.
 * TipoEnc presenter
 */

public class TipoEncPresenter extends BasePresenter implements ITipoEncPresenter {

    TipoEncInteractor mBaseInteractor;

    public TipoEncPresenter (BaseInteractor mBaseInteractor) {
        super (mBaseInteractor);
        this.mBaseInteractor = (TipoEncInteractor) mBaseInteractor;
        mBaseInteractor.attachCallBack (this);
    }

    @Override
    public void onSuccess (Object result) {

    }

    @Override
    public void onFailed (Throwable throwable) {

    }

    @Override
    public List<TipoEncuesta> getLisNomEncuesta (String user, String idProyecto) {
        return mBaseInteractor.getListNomEnc (user, idProyecto);
    }

    @Override
    public List<TipoEncuesta> getListTipoEnc (String usuario, String idProyecto, String encuesta) {

        return mBaseInteractor.getListTipoEnc (usuario, idProyecto, encuesta);
    }

    @Override
    public IView getView () {
        return mView != null ? (ILoginView) mView : null;
    }

    @Override
    public void attachView (IView view) {
        super.attachView (view);
    }

    @Override
    public BaseInteractor getInteractor () {
        return null;
    }
}
