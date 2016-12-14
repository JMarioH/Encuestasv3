package com.popgroup.encuestasv3.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by jesus.hernandez on 12/12/16.
 * modelo proyecto
 */
@DatabaseTable
public class Proyecto {

    private static final String ID="id";
    private static final String NOMBRE = "nombre";
    private static final String IDPROYECTO = "idproyecto";
    private static final String CLIENTE = "cliente";

    @DatabaseField(generatedId = true ,columnName = ID)
    private int id;

    @DatabaseField(columnName = NOMBRE)
    private String nombre;

    @DatabaseField(columnName = IDPROYECTO)
    private String idproyecto;

    @DatabaseField(columnName = CLIENTE)
    private String cliente;

    public Proyecto(){}

    public Proyecto(String idproyecto, String nombre) {
        this.idproyecto = idproyecto;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() { return nombre; }

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
