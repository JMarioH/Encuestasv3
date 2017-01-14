package com.popgroup.encuestasv3.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by jesus.hernandez on 08/12/16.
 * modelo base timpoe encuestas
 */
@DatabaseTable
public class TipoEncuesta implements Serializable{

    public static final String ID= "id";
    public static final String ENCUESTA= "encuesta";
    public static final String IDARCHIVO =  "idArchivo";
    public static final String IDTIENDA =  "idTienda";
    public static final String IDENCUESTA =  "idEncuesta";
    public static final String NUMERO_TEL =  "numero_tel";
    public static final String NOMBRE =  "nombre";
    public static final String IDPROYECTO =  "idProyecto";
    public static final String IDESTABLECIMIENTO =  "idEstablecimiento";

    @DatabaseField(generatedId = true ,columnName = ID)
    private int id;

    @DatabaseField(columnName = ENCUESTA)
    private String encuesta;

    @DatabaseField(columnName = IDARCHIVO)
    private String idArchivo;

    @DatabaseField(columnName = IDTIENDA)
    private String idTienda;

    @DatabaseField(columnName = IDENCUESTA)
    private String idEncuesta;

    @DatabaseField(columnName = NUMERO_TEL)
    private String numeroTel;

    @DatabaseField(columnName = NOMBRE)
    private String nombre;

    @DatabaseField(columnName = IDPROYECTO)
    private String idProyecto;

    @DatabaseField(columnName = IDESTABLECIMIENTO)
    private String Idestablecimiento;

    public TipoEncuesta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(String encuesta) {
        this.encuesta = encuesta;
    }

    public String getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(String idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getNumerTel() {
        return numeroTel;
    }

    public void setNumerTel(String numerTel) {
        this.numeroTel = numerTel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getIdestablecimiento() {
        return Idestablecimiento;
    }

    public void setIdestablecimiento(String idestablecimiento) {  Idestablecimiento = idestablecimiento; }
}


