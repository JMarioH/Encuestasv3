package com.popgroup.encuestasv3.Model;

import java.util.ArrayList;

/**
 * Created by jesus.hernandez on 26/12/16.
 */
public class FotoEncuesta {

    private String idEstablecimiento;
    private String idEncuesta;
    private ArrayList<String> nombre;
    private ArrayList<String> arrayFotos;

    public FotoEncuesta() {
    }

    public FotoEncuesta(String idEstablecimiento, String idEncuesta, ArrayList<String> nombre, ArrayList<String> arrayFotos) {
        this.idEstablecimiento = idEstablecimiento;
        this.idEncuesta = idEncuesta;
        this.nombre = nombre;
        this.arrayFotos = arrayFotos;
    }

    private static FotoEncuesta instace;

    public String getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(String idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public ArrayList<String> getNombre() {
        return nombre;
    }

    public void setNombre(ArrayList<String> nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getArrayFotos() {
        return arrayFotos;
    }

    public void setArrayFotos(ArrayList<String> arrayFotos) {
        this.arrayFotos = arrayFotos;
    }

    public static synchronized FotoEncuesta getInstace(){
        if(instace == null){
            instace = new FotoEncuesta();
        }
        return instace;
    }
}
