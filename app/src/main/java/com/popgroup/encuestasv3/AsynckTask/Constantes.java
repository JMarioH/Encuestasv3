package com.popgroup.encuestasv3.AsynckTask;

/**
 * Created by jesus.hernandez on 07/12/16.
 * variables de los webservices
 */

public class Constantes {
//http://info-sat.com.mx/
/*
    public static String IPWBService = "http://popresearch8.cloudapp.net/b/acces2.php";
    public static String IPSetFoto = "http://popresearch8.cloudapp.net/b/fotosws.php";
    public static String IPSetEncuesta = "http://popresearch8.cloudapp.net/b/setEncuesta.php"; // todo solo prubea
*/
    public static String IPWBService = "http://104.215.75.134/b/acces2.php";
    public static String IPSetFoto = "http://104.215.75.134/b/fotosws.php";
    public static String IPSetEncuesta = "http://104.215.75.134/b/setEncuesta.php";

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
