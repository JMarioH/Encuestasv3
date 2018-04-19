package com.popgroup.encuestasv3.MainEncuesta;

/**
 * Created by JMario. on 18/4/2018.
 */

public interface IMainInteractor {

    void getUsuario ();

    void enviarEncPendientes (String usuario, Integer respuestasCuestionarios);

    void enviarFotosPendientes ();

    void validateEncPendientes ();

    void validateFotosPendientes ();

    void checkUsuario ();

    void clearDataBase ();
}
