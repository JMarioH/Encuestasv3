package com.popgroup.encuestasv3.AsynckTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.MainEncuesta.IMainCallback;
import com.popgroup.encuestasv3.MainEncuesta.MainActivity;
import com.popgroup.encuestasv3.Model.GeoLocalizacion;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jesus.hernandez on 09/01/17.
 * envia las encuestas pendientes
 */

public class AsynckEncPendientes extends AsyncTask<String,String,String> {

    private Dao dao;
    private Context mContext;
    private Constantes constantes;
    private String URL;
    private String success;
    private String latitud;
    private String longitud;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private String mUsuario;
    private int numeroReg;
    private ArrayList<RespuestasCuestionario> arrayEnc;
    private ArrayList<GeoLocalizacion> arrayGeos;
    private String TAG = getClass ().getSimpleName ();
    private DBHelper mDBHelper;
    private ArrayList<NameValuePair> data;
    private IMainCallback iMainCallback;

    public AsynckEncPendientes (Context context, String usuario, int registros, IMainCallback callback) {
        this.mContext = context;
        this.mUsuario = usuario;
        this.numeroReg = registros;
        this.iMainCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        success = "0";
        constantes = new Constantes();
        URL = constantes.getIPWBSetService();
        data = new ArrayList<>();

        try {
            arrayEnc = new ArrayList<>();
            arrayGeos = new ArrayList<>();
            jsonArray = new JSONArray();
            dao = getmDBHelper().getRespuestasCuestioanrioDao();
            arrayEnc = (ArrayList<RespuestasCuestionario>) dao.queryBuilder().where().eq(RespuestasCuestionario.FLAG, true).query();
            for(RespuestasCuestionario resp : arrayEnc){

                dao = getmDBHelper().getGeosDao();
                arrayGeos = (ArrayList<GeoLocalizacion>) dao.queryBuilder().selectColumns(GeoLocalizacion.LATITUD, GeoLocalizacion.LONGITUD).where().eq(GeoLocalizacion.IDENCUESTA, resp.getIdTienda()).and().eq(GeoLocalizacion.IDESTABLECIMIENTO, resp.getIdEstablecimiento()).query();

                for(GeoLocalizacion item :arrayGeos){
                    latitud =  item.getLatitud();
                    longitud = item.getLongitud();
                }

                dao.clearObjectCache();
                jsonObject = new JSONObject();
                jsonObject.put("idEncuesta",resp.getIdEncuesta());
                jsonObject.put("idEstablecimiento",resp.getIdEstablecimiento());
                jsonObject.put("idTienda",resp.getIdTienda());
                jsonObject.put("usuario",mUsuario);
                jsonObject.put("idPregunta",resp.getIdPregunta());
                jsonObject.put("idRespuesta",resp.getIdRespuesta());
                jsonObject.put("abierta",resp.getRespuestLibre());
                jsonObject.put("latitud",latitud);
                jsonObject.put("longitud",longitud);
                jsonObject.put("fecha",resp.getFecha());
                jsonArray.put(jsonObject);


            }
            // grabar(jsonArray.toString());
            data.add(new BasicNameValuePair("setEncuestas",jsonArray.toString()));
            ServiceHandler serviceHandler = new ServiceHandler();
            String response = serviceHandler.makeServiceCall(URL, ServiceHandler.POST, data);
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result");

            success = result.getString("success").toString();


        } catch (SQLException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return success;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s.equals("1")){
            try { // borramos las encuestas enviada
                dao = getmDBHelper().getRespuestasCuestioanrioDao();
                DeleteBuilder<RespuestasCuestionario,Integer> deleteBuilder = dao.deleteBuilder();
                deleteBuilder.where().eq("flag",true);
                deleteBuilder.delete();
                dao.clearObjectCache();
                iMainCallback.onSuccess ("1");
                iMainCallback.showBtnEncPendientes (false, 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            iMainCallback.onFailed (new Throwable ("Error enviando la informacion"));
        }
        Intent i = new Intent(mContext, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mContext.startActivity(i);
    }
    private DBHelper getmDBHelper(){
        if (mDBHelper == null){
            mDBHelper = OpenHelperManager.getHelper(mContext,DBHelper.class);
        }
        return mDBHelper;
    }
    public void grabar(String contenido) {
        File ubicacion = Environment.getExternalStorageDirectory();
        File logFile = new File(ubicacion.getAbsolutePath(), mUsuario + ".txt");

        if (!logFile.exists()) {

            
            try {
                logFile.createNewFile();
                Log.e(TAG, "crear txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Log.e(TAG, "llena txt");
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(contenido.toString());
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
