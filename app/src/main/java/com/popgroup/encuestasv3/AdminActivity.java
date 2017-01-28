package com.popgroup.encuestasv3;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.popgroup.encuestasv3.AsynckTask.Constantes;
import com.popgroup.encuestasv3.AsynckTask.ServiceHandler;
import com.popgroup.encuestasv3.Model.Respuestas;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;
import com.popgroup.encuestasv3.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import static com.popgroup.encuestasv3.R.id.add;
import static com.popgroup.encuestasv3.R.id.contenido;

/**
 * Created by jesus.hernandez on 25/01/17.
 */
public class AdminActivity extends AppCompatActivity {
    public String TAG = getClass().getSimpleName();
    public String path = "";
    ArrayList<String> arrayList;
    ArrayList<RespuestasCuestionario> arrayResp;
    ArrayList<String> item;
    JSONArray jsonArrayitem;
    String strContent;

    @BindView(R.id.listTxt)
    ListView listView;
    @BindView(R.id.txtContenido)
    TextView txtContenido;
    @BindView(R.id.btnEnviar)
    Button btnEnviar;
    ArrayAdapter adapter;
    ArrayList<String> contenido ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        path = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath());
        File filepath = new File(path);
        File[] files = filepath.listFiles();

        Log.e(TAG, "files  size " + files.length);
        arrayList = new ArrayList<>();
        item = new ArrayList<>();
        arrayResp = new ArrayList<>();
        for (File f : files)
        {
            String fullPath = f.getAbsolutePath();
            int dot = fullPath.lastIndexOf(".");
            String ext = fullPath.substring(dot + 1);
            if(ext.equals("txt")) {
                arrayList.add(f.getName());
                adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, arrayList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                        textView.setTextColor(getResources().getColor( R.color.colorTxt));
                        textView.setGravity(Gravity.CENTER);
                        return view;

                    }
                };
            }
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemtxt = adapterView.getAdapter().getItem(i).toString();
                Log.e(TAG,"txt Seleccionado " + itemtxt);
                StringBuilder txt = new StringBuilder();

                try {
                    File mfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),itemtxt);
                    BufferedReader br = new BufferedReader(new FileReader(mfile));
                    String line;
                    while ((line = br.readLine()) != null) {
                        txt.append(line);
                        //txt.append('\n');
                        item.add(txt.toString());

                    }
                    strContent = item.toString();
                    br.close();
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }

                btnEnviar.setVisibility(View.VISIBLE);
                txtContenido.setText(txt.toString());
                txtContenido.setTextSize(10);


            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AsynckEncuestasTxt(AdminActivity.this,item).execute();
            }
        });

    }

    public class AsynckEncuestasTxt extends AsyncTask<String,String,String>{

        private ArrayList<String> stringTxt;
        private ProgressDialog pDialog;
        private String URL;
        String success;
        Constantes rutas;
        Context mContext;
        private ArrayList<NameValuePair> data;

        public AsynckEncuestasTxt(Context context,ArrayList<String> arrayList) {
            this.stringTxt = arrayList;
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Enviando datos");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pDialog.dismiss();
            pDialog.hide();

            if (s.equals("1")){
                //Log.e(TAG,"proceso terminado");
            }else{
                //Log.e(TAG,"error");
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            success = "0";
            rutas = new Constantes();
            URL = rutas.getIPWBSetService();
            data = new ArrayList<>();
            arrayResp = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(stringTxt);
                Log.e(TAG,"jsonArray" + jsonArray.length());

               String[] strArr = new String[jsonArray.length()];
                arrayList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                   strArr[i] = jsonArray.getString(i);
                    arrayList.add(strArr[i]);
                }


                Log.e(TAG,"JsonObj " + arrayList.toString());



               // Log.e(TAG,"jsonArritem " + jsonArray);
                //System.out.println(Arrays.toString(strArr));

               data.add(new BasicNameValuePair("setEncuestas",arrayList.toString()));
               //Log.w(TAG,"data : " + data);

              /*  ServiceHandler serviceHandler = new ServiceHandler();
                String response = serviceHandler.makeServiceCall(URL, ServiceHandler.POST, data);
                JSONObject jsonObject = new JSONObject(response);
                JSONObject result = jsonObject.getJSONObject("result");
                success = result.getString("success").toString();
                Log.w(TAG,"success" + success);*/

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

}
