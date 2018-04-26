package com.popgroup.encuestasv3.Cuestionario;

import com.popgroup.encuestasv3.Model.RespuestasCuestionario;

public interface ICuestionarioPresenter {
    void setGeo (String idEncuesta, String idEstablecimiento);

    void setRespuestaCuestionario (RespuestasCuestionario respuestaCuestionario);
}
