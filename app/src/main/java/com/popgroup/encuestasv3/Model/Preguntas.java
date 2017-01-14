package com.popgroup.encuestasv3.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by jesus.hernandez on 12/12/16.
 * modelo base preguntas
 */
@DatabaseTable
public class Preguntas implements Serializable {

    public static final String ID="id";
    public static final String IDPREGUNTA="idpregunta";
    public static final String PREGUNTA="pregunta";
    public static final String MULTIPLE="multiple";
    public static final String ORDEN="orden";
    public static final String IDENCUESTA="idEncuesta";

    @DatabaseField(generatedId = true ,index = true,columnName = ID)
    private int id;

    @DatabaseField(columnName = IDPREGUNTA)
    private int idPregunta;

    @DatabaseField(columnName = PREGUNTA)
    private String pregunta;

    @DatabaseField(columnName = MULTIPLE)
    private int multiple;

    @DatabaseField(columnName = ORDEN)
    private int orden;

    @DatabaseField(columnName = IDENCUESTA)
    private int idEncuesta;

    public Preguntas() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }
}
