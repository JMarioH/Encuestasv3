package com.popgroup.encuestasv3.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by jesus.hernandez on 20/12/16.
 */
@DatabaseTable
public class GeoLocalizacion {

    public static final String ID="id";
    public static final String IDENCUESTA ="idEncuesta";
    public static final String IDESTABLECIMIENTO ="idEstablecimiento";
    public static final String FECHA="Fecha";
    public static final String LATITUD="latitud";
    public static final String LONGITUD="longitud";

    @DatabaseField(generatedId =true ,index = true, columnName = ID)
    private int id;

    @DatabaseField(columnName = IDENCUESTA)
    private int idEncuesta;

    @DatabaseField(columnName = FECHA)
    private String fecha;

    @DatabaseField(columnName = IDESTABLECIMIENTO)
    private String idTienda;

    @DatabaseField(columnName = LATITUD)
    private String latitud;

    @DatabaseField(columnName = LONGITUD)
    private String longitud;


    public GeoLocalizacion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
