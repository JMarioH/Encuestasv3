package com.popgroup.encuestasv3.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.popgroup.encuestasv3.Model.CatMaster;
import com.popgroup.encuestasv3.Model.Cliente;
import com.popgroup.encuestasv3.Model.Fotos;
import com.popgroup.encuestasv3.Model.GeoLocalizacion;
import com.popgroup.encuestasv3.Model.Preguntas;
import com.popgroup.encuestasv3.Model.Proyecto;
import com.popgroup.encuestasv3.Model.Respuestas;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;
import com.popgroup.encuestasv3.Model.TipoEncuesta;
import com.popgroup.encuestasv3.Model.User;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    String TAG = getClass().getSimpleName();
    private static final String DB_NAME = "Encuestas.db";
    private static final int DB_VERSION = 1;

    private Dao<User, Integer> userDao = null;
    private Dao<Cliente, Integer> clienteDao = null;
    private Dao<Proyecto, Integer> proyectoDao = null;
    private Dao<TipoEncuesta, Integer> tipoEncDao = null;
    private Dao<CatMaster,Integer> catMastersDao= null;
    private Dao<Preguntas,Integer> pregutasDao = null;
    private Dao<Respuestas,Integer> respuestasDao = null;
    private Dao<RespuestasCuestionario,Integer> respuestasCuestioanrioDao = null;
    private Dao<GeoLocalizacion,Integer> geosDao = null;
    private Dao<Fotos,Integer> fotosDao = null;


    private RuntimeExceptionDao<User,Integer> userRuntime = null;
    private RuntimeExceptionDao<Cliente,Integer> clienteRuntime = null;
    private RuntimeExceptionDao<Proyecto,Integer> proyectoRuntime = null;
    private RuntimeExceptionDao<TipoEncuesta,Integer> tipoEncRuntime = null;
    private RuntimeExceptionDao<CatMaster,Integer> catMastersRuntime = null;
    private RuntimeExceptionDao<Preguntas,Integer> preguntasRuntime = null;
    private RuntimeExceptionDao<Respuestas,Integer> respuestasRuntime = null;
    private RuntimeExceptionDao<RespuestasCuestionario,Integer> respuestasCuestionaroiRuntime = null;
    private RuntimeExceptionDao<GeoLocalizacion,Integer> geosRuntime = null;
    private RuntimeExceptionDao<Fotos,Integer> fotosRuntime = null;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(DBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Cliente.class);
            TableUtils.createTable(connectionSource, TipoEncuesta.class);
            TableUtils.createTable(connectionSource, Proyecto.class);
            TableUtils.createTable(connectionSource, CatMaster.class);
            TableUtils.createTable(connectionSource, Preguntas.class);
            TableUtils.createTable(connectionSource, Respuestas.class);
            TableUtils.createTable(connectionSource,RespuestasCuestionario.class);
            TableUtils.createTable(connectionSource,GeoLocalizacion.class);
            TableUtils.createTable(connectionSource,Fotos.class);
        } catch (SQLException e) {
            Log.i(DBHelper.class.getName(), "no se pudo crear la base" , e);
            throw  new RuntimeException(e);
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
      try {
            Log.i(DBHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Cliente.class, true);
            TableUtils.dropTable(connectionSource, TipoEncuesta.class, true);
            TableUtils.dropTable(connectionSource, Proyecto.class, true);
            TableUtils.dropTable(connectionSource, CatMaster.class, true);
            TableUtils.dropTable(connectionSource, Preguntas.class,true);
            TableUtils.dropTable(connectionSource, Respuestas.class,true);
            TableUtils.dropTable(connectionSource,RespuestasCuestionario.class,true);
            TableUtils.createTable(connectionSource,GeoLocalizacion.class);
            TableUtils.dropTable(connectionSource, Fotos.class,true);

            onCreate(database, connectionSource);
        } catch (SQLException e) {
          Log.e(DBHelper.class.getName(), "Can't drop databases", e);
          throw new RuntimeException(e);

        }
    }
    public Dao<User, Integer> getUserDao() throws SQLException {
        if (userDao == null) {
            userDao = getDao(User.class);
        }
        return userDao;
    }


    public RuntimeExceptionDao<User, Integer> getsimpleUserDao() {
        if (userRuntime == null) {
            userRuntime = getRuntimeExceptionDao(User.class);
        }
        return userRuntime;
    }

    public Dao<Cliente, Integer> getClienteDao()  throws SQLException{
        if(clienteDao == null){
            clienteDao = getDao(Cliente.class);

        }
        return clienteDao;
    }
    public RuntimeExceptionDao<Cliente, Integer> getClienteRuntimeDao() {
        if (clienteRuntime == null) {
            clienteRuntime = getRuntimeExceptionDao(Cliente.class);
        }
        return clienteRuntime;
    }

    public Dao<Proyecto, Integer> getProyectoDao()  throws SQLException{
        if(proyectoDao == null){
            proyectoDao = getDao(Proyecto.class);

        }
        return proyectoDao;
    }
    public RuntimeExceptionDao<Proyecto, Integer> getProyectoRuntime() {
        if (proyectoRuntime == null) {
            proyectoRuntime = getRuntimeExceptionDao(Proyecto.class);
        }
        return proyectoRuntime;
    }

    public Dao<TipoEncuesta, Integer> getTipoEncDao()  throws SQLException{
        if(tipoEncDao == null){
            tipoEncDao = getDao(TipoEncuesta.class);

        }
        return tipoEncDao;
    }
    public RuntimeExceptionDao<TipoEncuesta, Integer> getTipoEncRuntimeDao() {
        if (tipoEncRuntime == null) {
            tipoEncRuntime = getRuntimeExceptionDao(TipoEncuesta.class);
        }
        return tipoEncRuntime;
    }

    public Dao<CatMaster, Integer> getCatMasterDao()  throws SQLException{
        if(catMastersDao == null){
            catMastersDao = getDao(CatMaster.class);

        }
        return catMastersDao;
    }
    public RuntimeExceptionDao<CatMaster, Integer> getCatMastersRuntime() {
        if (catMastersRuntime == null) {
            catMastersRuntime = getRuntimeExceptionDao(CatMaster.class);
        }
        return catMastersRuntime;
    }
    public Dao<Preguntas, Integer> getPregutasDao()  throws SQLException{
        if(pregutasDao == null){
            pregutasDao = getDao(Preguntas.class);

        }
        return pregutasDao;
    }
    public RuntimeExceptionDao<Preguntas, Integer> getPreguntasRuntime() {
        if (preguntasRuntime == null) {
            preguntasRuntime = getRuntimeExceptionDao(Preguntas.class);
        }
        return preguntasRuntime;
    }
    public Dao<Respuestas, Integer> getRespuestasDao()  throws SQLException{
        if(respuestasDao == null){
            respuestasDao = getDao(Respuestas.class);

        }
        return respuestasDao;
    }
    public RuntimeExceptionDao<Respuestas, Integer> getRespuestasRuntime() {
        if (respuestasRuntime == null) {
            respuestasRuntime = getRuntimeExceptionDao(Respuestas.class);
        }
        return respuestasRuntime;
    }

    public Dao<RespuestasCuestionario,Integer> getRespuestasCuestioanrioDao() throws SQLException{
        if(respuestasCuestioanrioDao == null){
            respuestasCuestioanrioDao = getDao(RespuestasCuestionario.class);
        }
        return respuestasCuestioanrioDao;
    }
    public RuntimeExceptionDao<RespuestasCuestionario,Integer> getRespuestasCuestionaroiRuntime(){
        if (respuestasCuestionaroiRuntime == null){
            respuestasCuestionaroiRuntime = getRuntimeExceptionDao(RespuestasCuestionario.class);
        }
        return respuestasCuestionaroiRuntime;
    }

    public Dao<GeoLocalizacion , Integer> getGeosDao() throws SQLException{
        if(geosDao == null){
            geosDao = getDao(GeoLocalizacion.class);
        }
        return  geosDao;
    }

    public RuntimeExceptionDao<GeoLocalizacion , Integer> getGeosRuntime(){
        if(geosRuntime == null){
            geosRuntime = getRuntimeExceptionDao(GeoLocalizacion.class);
        }
        return geosRuntime;
    }

    public Dao<Fotos,Integer> getFotosDao() throws SQLException{
        if(fotosDao == null){
            fotosDao = getDao(Fotos.class);
        }
        return fotosDao;
    }

    public RuntimeExceptionDao<Fotos,Integer> getFotosRuntime(){
        if(fotosRuntime == null ){
            fotosRuntime = getRuntimeExceptionDao(Fotos.class);
        }
        return fotosRuntime;
    }

    @Override
    public void close() {
        super.close();
        userDao= null;
        userRuntime = null;
        clienteDao = null;
        clienteRuntime = null;
        proyectoDao = null;
        proyectoRuntime = null;
        tipoEncDao = null;
        tipoEncRuntime = null;
        catMastersDao = null;
        catMastersRuntime = null;
        pregutasDao = null;
        preguntasRuntime = null;
        respuestasDao = null;
        respuestasRuntime = null;
        respuestasCuestioanrioDao = null;
        respuestasCuestionaroiRuntime = null;
        geosDao = null;
        geosRuntime = null;
        fotosDao = null;
        fotosRuntime = null;

    }
}

