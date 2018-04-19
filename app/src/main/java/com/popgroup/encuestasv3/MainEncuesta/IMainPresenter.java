package com.popgroup.encuestasv3.MainEncuesta;

/**
 * Created by JMario. on 18/4/2018.
 */

public interface IMainPresenter {
    void getUsuario ();

    void enviarEncuestaPendientes (String usuario, Integer respuestasCuestionarios);

    void enviarFotosPendientes ();

    void validateEncPendientes ();

    void validateFotosPendientes ();

    void nextLoginOperation ();

    void clearDataBase ();
}
