package com.popgroup.encuestasv3.Cuestionario;

import com.popgroup.encuestasv3.Model.RespuestasCuestionario;

public interface ICuestionarioInteractor {
    void SetGeo (String idEncuesta, String idEstablecimiento);

    void SaveRespuesta (RespuestasCuestionario respuestasCuestionario);
}
