package com.popgroup.encuestasv3.MainEncuesta;

import com.popgroup.encuestasv3.Model.RespuestasCuestionario;

import java.util.ArrayList;

/**
 * Created by JMario. on 18/4/2018.
 */

public interface IMainPresenter {
    void getUsuario ();

    void enviarEncuestaPendientes (String usuario, ArrayList<RespuestasCuestionario> respuestasCuestionarios);

    void enviarFotosPendientes ();

    void validateEncPendientes ();

    void validateFotosPendientes ();
}
