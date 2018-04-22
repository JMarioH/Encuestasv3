package com.popgroup.encuestasv3.TipoEncuesta;

import com.popgroup.encuestasv3.Model.TipoEncuesta;

import java.util.List;

/**
 * Created by Orion on 22/04/2018.
 * interfaz TipoEncuesta interactor
 */

public interface ITipoEncInteractor {

    List<TipoEncuesta> getListNomEnc (String usuario, String idProyecto);

    List<TipoEncuesta> getListTipoEnc (String usuario, String idProyecto, String encuesta);
}
