package com.popgroup.encuestasv3.AsynckTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.popgroup.encuestasv3.R.string.respuesta;

/**
 * Created by jesus.hernandez on 02/01/17.
 */
class AsyncUploadFotos extends AsyncTask<Void, Void, Boolean> {

    public ProgressDialog pDialog ;
    private Context context ;
    private ArrayList<NameValuePair> data;
    private String TAG = getClass().getSimpleName();
    private String Url = null;
    Constantes constantes;

    public AsyncUploadFotos(Context context , ArrayList<NameValuePair> data ) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Enviando Fotos ");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
        pDialog.hide();
        pDialog.dismiss();
        if(s) {
            Toast.makeText(context, "Fotos enviadas correctamente", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean respuesta = false;
        constantes = new Constantes();
        Url = constantes.getIPSetFoto();
        ServiceHandler jsonParser = new ServiceHandler();

        String jsonRes = jsonParser.makeServiceCall(Url, ServiceHandler.POST, data);
        try {
            JSONObject jsonObject = new JSONObject(jsonRes);
            JSONObject result = jsonObject.getJSONObject("result");
            String success = result.getString("success");
            if (success.equals("1")) {
                respuesta = true;
            }else{
                respuesta = false;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respuesta;
    }
}

