package com.popgroup.encuestasv3.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by jesus.hernandez on 08/12/16.
 * modelo cliente
 */
@DatabaseTable
public class Cliente{

    public static final String ID="id";
    public static final String NOMBRE="nombre";

    @DatabaseField(generatedId = true , index = true ,columnName = ID)
    private int id;

    @DatabaseField(columnName = NOMBRE)
    private String nombre;


    public Cliente() {
    }
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
}
