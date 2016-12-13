package com.popgroup.encuestasv3.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by jesus.hernandez on 12/12/16.
 */
@DatabaseTable
public class CatMaster implements Serializable {

    public static final String ID = "id";
    public static final String IDTIENDA = "idTienda";
    public static final String NOMBRE = "nombre";
    public static final String IDARCHIVO = "idArchivo";
    public static final String IDENCUESTA = "idENCUESTA";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;

    @DatabaseField(columnName = IDTIENDA)
    private String idTienda;

    @DatabaseField(columnName = NOMBRE)
    private String nombre;

    @DatabaseField(columnName = IDARCHIVO)
    private String idArchivo;

    @DatabaseField(columnName = IDENCUESTA)
    private String idEncuesta;

    public CatMaster(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(String idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }
}
