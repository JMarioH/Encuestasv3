package com.popgroup.encuestasv3.FinEncuesta;

import com.popgroup.encuestasv3.Model.FotoEncuesta;

/**
 * Created by Orion on 16/04/2018.
 */

interface IFinEncuestaInter {
    void enviarEnc (String idEncuesta, String idEstablecimiento, String idTienda, String usuario);

    void saveEncLocal (String idEstablecimiento, String idEncuesta);

    void saveFotos (FotoEncuesta fotoEncuesta, String idEstablecimiento, String idEncuesta);
}
