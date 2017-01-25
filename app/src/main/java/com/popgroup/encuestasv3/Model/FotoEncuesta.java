package com.popgroup.encuestasv3.Model;

import java.util.ArrayList;

/**
 * Created by jesus.hernandez on 26/12/16.
 * modelo foto encuesta
 */
public class FotoEncuesta {

    private String idEstablecimiento;
    private String idEncuesta;
    private ArrayList<String> nombre;
    private ArrayList<String> arrayFotos;
    private ArrayList<byte []> arrayByte;
    public FotoEncuesta() {
    }

    public FotoEncuesta(String idEstablecimiento, String idEncuesta, ArrayList<String> nombre, ArrayList<String> arrayFotos , ArrayList<byte []> arrayByte) {
        this.idEstablecimiento = idEstablecimiento;
        this.idEncuesta = idEncuesta;
        this.nombre = nombre;
        this.arrayFotos = arrayFotos;
        this.arrayByte = arrayByte;
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

    public ArrayList<byte[]> getArrayByte() {
        return arrayByte;
    }

    public void setArrayByte(ArrayList<byte[]> arrayByte) {
        this.arrayByte = arrayByte;
    }

    public static synchronized FotoEncuesta getInstace(){
        if(instace == null){
            instace = new FotoEncuesta();
        }
        return instace;
    }
}
