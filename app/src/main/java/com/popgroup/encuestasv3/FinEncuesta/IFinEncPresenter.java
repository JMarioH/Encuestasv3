package com.popgroup.encuestasv3.FinEncuesta;

import com.popgroup.encuestasv3.Model.FotoEncuesta;

/**
 * Created by JMario. on 16/4/2018.
 */

public interface IFinEncPresenter {
    void enviarEncuesta (String idEncuesta, String idEstablecimiento, String idTienda, String usuario);

    void saveEncuesta (String idEstablecimiento, String idEncuesta);

    void saveFotosEnc (FotoEncuesta fotoEncuesta, String idEstablecimiento, String idEncuesta);
}
