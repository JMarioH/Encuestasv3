package com.popgroup.encuestasv3.Model;

import java.io.Serializable;

public class EncuestaData implements Serializable {
    private static EncuestaData instance = null;
    private String idEncuesta;
    private String encuesta;
    private String idTienda;
    private String idEstablecimiento;
    private String idArchivo;

    private EncuestaData () {
    }

    public static synchronized EncuestaData getInstance () {
        if (instance == null) {
            instance = new EncuestaData ();
        }
        return instance;
    }

    public void clearData () {
        idEncuesta = "";
        encuesta = "";
        idTienda = "";
        idEstablecimiento = "";
        idArchivo = "";
    }

    public String getIdEncuesta () {
        return idEncuesta;
    }

    public void setIdEncuesta (String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getEncuesta () {
        return encuesta;
    }

    public void setEncuesta (String encuesta) {
        this.encuesta = encuesta;
    }

    public String getIdTienda () {
        return idTienda;
    }

    public void setIdTienda (String idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdEstablecimiento () {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento (String idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getIdArchivo () {
        return idArchivo;
    }

    public void setIdArchivo (String idArchivo) {
        this.idArchivo = idArchivo;
    }


}

