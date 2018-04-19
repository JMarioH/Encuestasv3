package com.popgroup.encuestasv3.MainEncuesta;

import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Base.IView;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;

import java.util.ArrayList;

/**
 * Created by JMario. on 18/4/2018.
 */

public class MainPresenter extends BasePresenter implements IMainPresenter, IMainCallback {

    private MainInteractor mainInteractor;

    public MainPresenter (MainInteractor mBaseInteractor) {
        super (mBaseInteractor);
        this.mainInteractor = (MainInteractor) mBaseInteractor;
        mBaseInteractor.attachCallBack (this);
    }

    @Override
    public void getUsuario () {
        mainInteractor.getUsuario ();
    }

    @Override
    public void enviarEncuestaPendientes (String usuario, ArrayList<RespuestasCuestionario> respuestasCuestionarios) {

        if (getView () != null) {
            getView ().showLoader (true);
        }
        mainInteractor.enviarEncPendientes (usuario, respuestasCuestionarios);
    }

    @Override
    public void enviarFotosPendientes () {
        if (getView () != null) {
            getView ().showLoader (true);
        }
        mainInteractor.enviarFotosPendientes ();
    }

    @Override
    public void validateEncPendientes () {
        mainInteractor.validateEncPendientes ();
    }

    @Override
    public void validateFotosPendientes () {
        mainInteractor.validateFotosPendientes ();
    }

    @Override
    public IMainView getView () {
        return mView != null ? (IMainView) mView : null;
    }

    @Override
    public void attachView (IView view) {
        super.attachView (view);
    }

    @Override
    public BaseInteractor getInteractor () {
        return mBaseInteractor != null ? mBaseInteractor : null;
    }

    @Override
    public void showUsuario (Boolean show, String user) {
        if (getView () != null) {
            getView ().showUsuario (show, user);
        }
    }

    @Override
    public void showBtnEncPendientes (Boolean show, Integer pendientes) {
        if (getView () != null) {
            getView ().showButtonEncuestasPendientes (show, pendientes);
        }
    }

    @Override
    public void showBtnFotoPendientes (Boolean show, Integer pendientes) {
        if (getView () != null) {
            getView ().showButtonFotosPendientes (show, pendientes);
        }
    }

    @Override
    public void onSuccess (Object result) {
        if (getView () != null) {
            getView ().showLoader (false);
        }
    }

    @Override
    public void onFailed (Throwable throwable) {
        if (getView () != null) {
            getView ().showLoader (false);
            getView ().showError (throwable);
        }
    }

}
