package com.popgroup.encuestasv3.AsynckTask;

/**
 * Created by jesus.hernandez on 07/12/16.
 * variables de los webservices
 */

public class Constantes {


    public static String IPWBService = "http://popresearch8.cloudapp.net/b/acces2.php";
    public static String IPSetFoto = "http://popresearch8.cloudapp.net/b/fotosws.php";
    public static String IPSetEncuesta = "http://popresearch8.cloudapp.net/b/setEncuesta.php"; // todo solo prubea
    public static String ENC_USER = "ENC_USER";
    public static String ENC_CLIENTE = "ENC_CLIENTE";

    public static String getIPWBService () {
        return IPWBService;
    }

    public static String getIPWBSetService () {
        return IPSetEncuesta;
    }

    public static String getIPSetFoto () {
        return IPSetFoto;
    }

}
