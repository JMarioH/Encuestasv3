package com.popgroup.encuestasv3.FinEncuesta;

import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Base.IView;
import com.popgroup.encuestasv3.Model.FotoEncuesta;

/**
 * Created by JMario. on 16/4/2018.
 */

public class FinEncuestaPresenter extends BasePresenter implements IFinEncPresenter {

    private FinEncuestaInteractor mInteractor;

    public FinEncuestaPresenter (BaseInteractor mBaseInteractor) {
        super(mBaseInteractor);
        this.mInteractor = (FinEncuestaInteractor) mBaseInteractor;
        mBaseInteractor.attachCallBack(this);

    }

    @Override
    public void enviarEncuesta (String idEncuesta, String idEstablecimiento, String idTienda, String usuario) {
        if (idEncuesta != null && idEstablecimiento != null && idTienda != null && usuario != null) {
            if (getView () != null) {
                getView ().showLoader (true);
            }
            mInteractor.enviarEnc (idEncuesta, idEstablecimiento, idTienda, usuario);
        }
    }

    @Override
    public void saveEncuesta (String IdEstablecimiento, String IdEncuesta) {
        if (IdEstablecimiento != null && IdEncuesta != null) {
            mInteractor.saveEncLocal (IdEstablecimiento, IdEncuesta);
        }
    }

    @Override
    public void saveFotosEnc (FotoEncuesta fotoEncuesta, String idEstablecimiento, String idEncuesta) {
        if (fotoEncuesta != null && idEstablecimiento != null && idEncuesta != null) {
            mInteractor.saveFotos (fotoEncuesta, idEstablecimiento, idEncuesta);
        }

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
        return mBaseInteractor != null ? mBaseInteractor : null;
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
    }

    @Override
    public void onSuccess (String result) {
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
