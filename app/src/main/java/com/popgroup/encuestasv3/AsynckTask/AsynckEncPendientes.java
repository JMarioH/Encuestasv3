package com.popgroup.encuestasv3.AsynckTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.MainActivity;
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

    Dao dao;
    Context mContext;
    Constantes constantes;
    String URL ;
    String success;
    String latitud;
    String longitud;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String mUsuario;
    int numeroReg;
    ArrayList<RespuestasCuestionario> arrayEnc;
    ArrayList<GeoLocalizacion> arrayGeos;
    private String TAG = getClass ().getSimpleName ();
    private ProgressDialog progressDialog;
    private DBHelper mDBHelper;
    private ArrayList<NameValuePair> data;
    public AsynckEncPendientes(Context context , String usuario, int registros){
        this.mContext = context;
        this.mUsuario = usuario;
        this.numeroReg = registros;
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
            arrayEnc = (ArrayList<RespuestasCuestionario>) dao.queryBuilder().where().eq("flag",true).query();
            for(RespuestasCuestionario resp : arrayEnc){

                dao = getmDBHelper().getGeosDao();
                arrayGeos = (ArrayList<GeoLocalizacion>) dao.queryBuilder().selectColumns("latitud","longitud")
                        .where ().eq ("idEncuesta", resp.getIdTienda ()).and ().eq ("idEstablecimiento", resp.getIdEstablecimiento ()).query ();

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
            grabar(jsonArray.toString());
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
    protected void onPreExecute () {
        super.onPreExecute ();
        progressDialog = new ProgressDialog (mContext);
        progressDialog.setProgressStyle (ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage ("Enviando..." + numeroReg + " datos pendientes .");
        progressDialog.setIndeterminate (true);
        progressDialog.setCancelable (false);
        progressDialog.show ();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        progressDialog.hide();
        Log.e(TAG,"onpostExecute : " +s);
        if(s.equals("1")){
            try { // borramos las encuestas enviada
                dao = getmDBHelper().getRespuestasCuestioanrioDao();
                DeleteBuilder<RespuestasCuestionario,Integer> deleteBuilder = dao.deleteBuilder();
                deleteBuilder.where().eq("flag",true);
                deleteBuilder.delete();
                dao.clearObjectCache();
                Toast.makeText(mContext, "Datos enviados. .", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(mContext, "Error enviado pendientes. .", Toast.LENGTH_LONG).show();
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
