package com.popgroup.encuestasv3.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by jesus.hernandez on 12/12/16.
 */
@DatabaseTable
public class Respuestas implements Serializable {

    public static final String ID="id";
    public static final String IDPREGUNTA="idpregunta";
    public static final String IDRESPUESTA="idrespuesta";
    public static final String RESPUESTA="respuesta";
    public static final String SIGPREGUNTA="sigPregunta";
    public static final String RESPUESTALIBRE="respuestaLibre";
    public static final String IDENCUESTA="idEncuesta";

    @DatabaseField(generatedId = true ,index = true,columnName = ID)
    private int id;

    @DatabaseField(columnName = IDPREGUNTA)
    private int idPregunta;

    @DatabaseField(columnName = IDRESPUESTA)
    private int idRespuesta;

    @DatabaseField(columnName = RESPUESTA)
    private String respuesta;

    @DatabaseField(columnName = SIGPREGUNTA)
    private String sigPregunta;

    @DatabaseField(columnName = RESPUESTALIBRE)
    private String respLibre;

    @DatabaseField(columnName = IDENCUESTA)
    private int idEncuesta;

    public Respuestas() {
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

    public int getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) { this.idRespuesta = idRespuesta; }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getSigPregunta() {
        return sigPregunta;
    }

    public void setSigPregunta(String sigPregunta) {
        this.sigPregunta = sigPregunta;
    }

    public String getRespLibre() {
        return respLibre;
    }

    public void setRespLibre(String respLibre) {
        this.respLibre = respLibre;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }
}
