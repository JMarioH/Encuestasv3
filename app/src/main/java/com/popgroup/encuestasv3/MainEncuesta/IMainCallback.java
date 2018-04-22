package com.popgroup.encuestasv3.MainEncuesta;

import com.popgroup.encuestasv3.Base.ICallBack;

/**
 * Created by Orion on 18/04/2018.
 * interfaz main interactor
 */

public interface IMainCallback extends ICallBack {

    void showUsuario (Boolean show, String user);

    void showBtnEncPendientes (Boolean show, Integer pendientes);

    void showBtnFotoPendientes (Boolean show, Integer pendientes);

    void showAlertDB ();

    void nextOperation ();
}
