package com.popgroup.encuestasv3.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static com.popgroup.encuestasv3.Model.Proyecto.NOMBRE;

/**
 * Created by jesus.hernandez on 12/12/16.
 */
@DatabaseTable
public class Proyecto {

    public static final String ID="id";
    public static final String NOMBRE = "nombre";
    public static final String IDPROYECTO = "idproyecto";
    public static final String CLIENTE = "cliente";

    @DatabaseField(generatedId = true ,columnName = ID)
    private int id;

    @DatabaseField(columnName = NOMBRE)
    private String nombre;

    @DatabaseField(columnName = IDPROYECTO)
    private String idproyecto;

    @DatabaseField(columnName = CLIENTE)
    private String cliente;

    public Proyecto(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdproyecto() {
        return idproyecto;
    }

    public void setIdproyecto(String idproyecto) {
        this.idproyecto = idproyecto;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}
