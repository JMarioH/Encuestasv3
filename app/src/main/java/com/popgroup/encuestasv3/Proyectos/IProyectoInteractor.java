package com.popgroup.encuestasv3.Proyectos;

import com.popgroup.encuestasv3.Model.Proyecto;

import java.util.List;

/**
 * Created by Orion on 21/04/2018.
 */

public interface IProyectoInteractor {
    String getCliente ();

    List<Proyecto> getListProyectos ();
}
