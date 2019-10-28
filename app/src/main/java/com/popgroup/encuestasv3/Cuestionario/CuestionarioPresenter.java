package com.popgroup.encuestasv3.Cuestionario;

import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Base.IView;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;

class CuestionarioPresenter extends BasePresenter implements ICuestionarioPresenter {
    CuestionarioInteractor mBaseInteractor;

    public CuestionarioPresenter (CuestionarioInteractor cuestionarioInteractor) {
        super (cuestionarioInteractor);
        this.mBaseInteractor = cuestionarioInteractor;
    }

    @Override
    public void setGeo (String idEncuesta, String idEstablecimiento) {
        getInteractor ().SetGeo (idEncuesta, idEstablecimiento);
    }

    @Override
    public void setRespuestaCuestionario (RespuestasCuestionario respuestaCuestionario) {
        if (respuestaCuestionario != null) {
            getInteractor ().SaveRespuesta (respuestaCuestionario);
        }
    }

    @Override
    public IView getView () {
        return null;
    }

    @Override
    public CuestionarioInteractor getInteractor () {
        return mBaseInteractor;
    }

    @Override
    public void onSuccess (String result) {

    }

    @Override
    public void onFailed (Throwable throwable) {

    }
}
