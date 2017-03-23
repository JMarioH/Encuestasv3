package com.popgroup.encuestasv3.Model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import static com.popgroup.encuestasv3.Model.Fotos.BASE64;
import static com.popgroup.encuestasv3.Model.Fotos.BYTEFOTO;
import static com.popgroup.encuestasv3.Model.Fotos.ID;
import static com.popgroup.encuestasv3.Model.Fotos.IDENCUESTA;
import static com.popgroup.encuestasv3.Model.Fotos.IDESTABLECIMIENTO;
import static com.popgroup.encuestasv3.Model.Fotos.NOMBRE;

/**
 * Created by jesus.hernandez on 09/01/17.
 * modelo foto base de datos
 *
 */

public class Fotos {

    public static final String ID = "id";
    public static final String IDESTABLECIMIENTO = "idEstablecimiento";
    public static final String IDENCUESTA = "idEncuesta";
    public static final String NOMBRE = "nombre";
    public static final String BASE64 = "base64";
    public static final String BYTEFOTO = "byte";
    public Fotos(){}

    @DatabaseField(generatedId = true, index = true, columnName = ID)
    private int id;

    @DatabaseField(columnName = IDESTABLECIMIENTO)
    private int idEstablecimiento;

    @DatabaseField(columnName = IDENCUESTA)
    private int idEncuesta;

    @DatabaseField(columnName = NOMBRE)
    private String nombre;

    @DatabaseField(columnName = BASE64)
    private String base64;

    @DatabaseField(columnName = BYTEFOTO , dataType = DataType.BYTE_ARRAY)
    private byte[] bytebase;

    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public byte[] getBytebase() {
        return bytebase;
    }

    public  int getID() {
        return id;
    }

    public void setBytebase(byte[] bytebase) {
        this.bytebase = bytebase;
    }
}
