package com.popgroup.encuestasv3.AsynckTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.popgroup.encuestasv3.Base.ICallBack;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.MainEncuesta.IMainCallback;
import com.popgroup.encuestasv3.MainEncuesta.MainActivity;
import com.popgroup.encuestasv3.Model.CatMaster;
import com.popgroup.encuestasv3.Model.FotoEncuesta;
import com.popgroup.encuestasv3.Model.Fotos;
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
 * Created by jesus.hernandez on 26/12/16.
 * envias las encuestas en el proceso normal
 */
public class AsynckEncuestas extends AsyncTask<String, String, String> {
    public JSONArray jsonArray;
    Dao dao;
    Context mContext;
    Constantes constantes;
    String URL;
    String success;
    String latitud = null, longitud = null;
    FotoEncuesta fotoEncuesta = new FotoEncuesta ().getInstace ();
    private String TAG = getClass ().getSimpleName ();
    private DBHelper mDBHelper;
    private JSONObject jsonObject;
    private ArrayList<RespuestasCuestionario> arrayResultados;
    private ArrayList<NameValuePair> data;
    private String mEncuesta;
    private String mTienda;
    private String mEstablecimiento;
    private String mUsuario;
    private String nomArchivo, base64;
    private byte[] bytefoto;
    private ArrayList<String> arrayFotos, arrayNomFoto;
    private ArrayList<NameValuePair> datosPost;
    private JSONArray jsonFotos;

    private IMainCallback iCallBack;

    public AsynckEncuestas (Context context, String idEncuesta, String idEstablecimiento,
                            String idTienda, String usuario, IMainCallback iCallBack) {
        this.mContext = context;
        this.mEncuesta = idEncuesta;
        this.mTienda = idTienda;
        this.mUsuario = usuario;
        this.mEstablecimiento = idEstablecimiento;
        this.iCallBack = iCallBack;
    }

    public AsynckEncuestas (Context ctx, String idEncuesta, String idEstablecimiento,
                            String idTienda, String usuario, ICallBack iCallBack) {
        this.mContext = ctx;
        this.mEncuesta = idEncuesta;
        this.mTienda = idTienda;
        this.mUsuario = usuario;
        this.mEstablecimiento = idEstablecimiento;
        if (iCallBack instanceof ICallBack) {
            this.iCallBack = (IMainCallback) iCallBack;
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        success = "0";
        constantes = new Constantes();
        URL = constantes.getIPWBSetService();
        mDBHelper = OpenHelperManager.getHelper(mContext, DBHelper.class);
        ArrayList<GeoLocalizacion> arrayGeos;


        try {
            dao = getmDBHelper().getGeosDao();
            arrayGeos = (ArrayList<GeoLocalizacion>) dao.queryBuilder().selectColumns("latitud", "longitud")
                    .where().eq("idEncuesta", mEncuesta).and().eq("idEstablecimiento", mTienda).query();
            if (arrayGeos != null && arrayGeos.size() > 0) {
                for (GeoLocalizacion item : arrayGeos) {
                    latitud = item.getLatitud();
                    longitud = item.getLongitud();
                }
            } else {
                latitud = "0.0";
                longitud = "0.0";
            }
            dao.clearObjectCache();

            dao = getmDBHelper().getRespuestasCuestioanrioDao();
            dao.clearObjectCache(); // limpiamos el cache de de la base de datos

            arrayResultados = (ArrayList<RespuestasCuestionario>) dao.queryBuilder()
                    .selectColumns("idEncuesta", "fecha", "idTienda", "idEstablecimiento", "idPregunta", "idRespuesta", "respuestaLibre", "idArchivo")
                    .where().eq("idEncuesta", mEncuesta)
                    .and().eq("idTienda", mTienda)
                    .and().eq("idEstablecimiento", mEstablecimiento)
                    .query();

            data = new ArrayList<>();
            jsonArray = new JSONArray();

            for (RespuestasCuestionario item : arrayResultados) {

                jsonObject = new JSONObject();
                jsonObject.put("idEncuesta", item.getIdEncuesta());
                jsonObject.put("idEstablecimiento", mEstablecimiento);
                jsonObject.put("idTienda", item.getIdTienda());
                jsonObject.put("usuario", mUsuario);
                jsonObject.put("idPregunta", item.getIdPregunta());
                jsonObject.put("idRespuesta", item.getIdRespuesta());
                jsonObject.put("abierta", item.getRespuestLibre());
                jsonObject.put("latitud", latitud);
                jsonObject.put("longitud", longitud);
                jsonObject.put("fecha", item.getFecha().toString());

                jsonArray.put(jsonObject);

            }

            // grabar(jsonArray.toString());
            //  grabaJson(jsonArray.toString());
            data.add(new BasicNameValuePair("setEncuestas", jsonArray.toString()));
                try {
                    ServiceHandler serviceHandler = new ServiceHandler();
                    String response = serviceHandler.makeServiceCall(URL, ServiceHandler.POST, data);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("result");
                    success = result.getString("success").toString();

                    return success;
                } catch (Exception e) {
                    e.printStackTrace();
                    success = "0";
                }

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

        try {
            // cambiamos el status de la lista de encuestas
            dao = getmDBHelper().getCatMasterDao();
            UpdateBuilder<CatMaster, Integer> updateBuilder = dao.updateBuilder();
            updateBuilder.updateColumnValue("flag", false);
            updateBuilder.where().eq("idTienda", mEstablecimiento).and().eq("idEncuesta", mEncuesta);
            updateBuilder.update();
            dao.clearObjectCache();

            dao = getmDBHelper ().getGeosDao ();
            DeleteBuilder<GeoLocalizacion, Integer> deleteBuilder = dao.deleteBuilder ();
            deleteBuilder.where ().eq ("idEncuesta", mEncuesta)
                    .and ().eq ("idEstablecimiento", mTienda);
            deleteBuilder.delete ();
            dao.clearObjectCache ();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (success.equals("1")) {

            try { // borramos la encuesta enviada
                dao = getmDBHelper().getRespuestasCuestioanrioDao();
                DeleteBuilder<RespuestasCuestionario, Integer> deleteBuilder = dao.deleteBuilder();
                deleteBuilder.where().eq("idEncuesta", mEncuesta)
                        .and().eq("idTienda", mTienda)
                        .and().eq("idEstablecimiento", mEstablecimiento);
                deleteBuilder.delete();
                dao.clearObjectCache();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            enviaFotos();
            iCallBack.onSuccess (s);
            Intent i = new Intent(mContext, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mContext.startActivity(i);
        } else {
            guardaFotos();
            iCallBack.onFailed (new Throwable ("Error al enviar encuestas"));
        }
    }

    public void enviaFotos() { //  preparamos las fotos para enviarlas
        // iniciamos el envio de las fotos
        int x;
        arrayNomFoto = new ArrayList<>();
        arrayFotos = new ArrayList<>();

        if (fotoEncuesta.getNombre() != null) {

            arrayNomFoto = fotoEncuesta.getNombre();   //  array de  nombres de las fotos
            arrayFotos = fotoEncuesta.getArrayFotos(); // array de base64 de las fotos
            jsonFotos = new JSONArray();
            datosPost = new ArrayList<>();
            for (x = 0; x < arrayFotos.size(); x++) {
                nomArchivo = mEncuesta + "_" + mEstablecimiento + "_" + fotoEncuesta.getNombre().get(x) + "_" + x + ".jpg";
                base64 = fotoEncuesta.getArrayFotos().get(x);
                //bytefoto = fotoEncuesta.getArrayByte().get(x);
                // agregamos las fotos ala base de datos ;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("idEstablecimiento", mEstablecimiento);
                    jsonObject.put("idEncuesta", mEncuesta);
                    jsonObject.put("nombreFoto", nomArchivo);
                    jsonObject.put("base64", base64);
                    jsonFotos.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            datosPost.add(new BasicNameValuePair("subeFotos", jsonFotos.toString()));
            // envio de fotos
            new AsyncUploadFotos (mContext, datosPost, mEncuesta, mEstablecimiento, iCallBack).execute ();

        }
    }

    public void guardaFotos () {
        int x = 0;
        ArrayList<String> arrayBase64;
        if (fotoEncuesta.getNombre() != null) {
            arrayBase64 = fotoEncuesta.getArrayFotos();
            try {
                dao = getmDBHelper().getFotosDao();
                for (x = 0; x < arrayBase64.size(); x++) {
                    Fotos Objfotos = new Fotos();
                    nomArchivo = mEncuesta + "_" + mEstablecimiento + "_" + fotoEncuesta.getNombre().get(x) + "_" + x + ".jpg";
                    base64 = fotoEncuesta.getArrayFotos().get(x);
                    Objfotos.setIdEstablecimiento(Integer.parseInt(mEstablecimiento));
                    Objfotos.setIdEncuesta(Integer.parseInt(mEncuesta));
                    Objfotos.setNombre(fotoEncuesta.getNombre().get(x));
                    Objfotos.setBase64(base64);
                    // Objfotos.setBytebase(fotoEncuesta.getArrayByte().get(x));
                    dao.create(Objfotos);
                }
                dao.clearObjectCache();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private DBHelper getmDBHelper () {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper (mContext, DBHelper.class);
        }
        return mDBHelper;
    }

    public void grabar(String contenido) {
        File ubicacion = Environment.getExternalStorageDirectory();
        File logFile = new File(ubicacion.getAbsolutePath(), mUsuario + ".txt");

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
                //Log.e(TAG, "crear txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
          //  Log.e(TAG, "llena txt");
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(contenido.toString());
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void grabaJson(String data){

        try {
            File ubicacion = Environment.getExternalStorageDirectory();
            FileWriter file = new FileWriter(ubicacion.getAbsolutePath()+"/"+mUsuario+"_test.json");
            file.write(data);
            file.flush();
            file.close();
        } catch (IOException e) {
            Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
        }
    }
}
