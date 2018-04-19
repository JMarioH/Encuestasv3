package com.popgroup.encuestasv3.MainEncuesta;

import com.popgroup.encuestasv3.Base.IView;

/**
 * Created by JMario. on 18/4/2018.
 */

public interface IMainView extends IView {
    void showUsuario (Boolean show, String usuario);

    void showAlert ();
    void showButtonEncuestasPendientes (Boolean show, Integer pendientes);
    void showButtonFotosPendientes (Boolean show, Integer pendientes);
}
