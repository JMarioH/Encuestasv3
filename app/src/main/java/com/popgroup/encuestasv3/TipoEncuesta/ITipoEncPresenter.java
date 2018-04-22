package com.popgroup.encuestasv3.TipoEncuesta;

import com.popgroup.encuestasv3.Model.TipoEncuesta;

import java.util.List;

/**
 * Created by Orion on 22/04/2018.
 * interfaz TipoEnc presenter
 */

public interface ITipoEncPresenter {
    List<TipoEncuesta> getLisNomEncuesta (final String user, final String idProyecto);

    List<TipoEncuesta> getListTipoEnc (final String user, final String idProyecto, final String encuesta);
}
