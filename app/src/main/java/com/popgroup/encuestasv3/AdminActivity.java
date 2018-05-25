package com.popgroup.encuestasv3;

import android.app.ProgressDialog;
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
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jesus.hernandez on 25/01/17.
 */
public class AdminActivity extends AppCompatActivity {
    public String TAG = getClass().getSimpleName();
    public String path = "";
    @BindView(R.id.listTxt)
    ListView listView;
    @BindView(R.id.txtContenido)
    TextView txtContenido;
    @BindView(R.id.btnEnviar)
    Button btnEnviar;
    private ArrayList<String> arrayList;
    private ArrayList<RespuestasCuestionario> arrayResp;
    private ArrayList<String> item;
    private String dataString;
    private JSONArray jsonArrayitem;
    private String strContent;
    private ArrayAdapter adapter;
    private ArrayList<String> contenido;
    private String itemtxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        path = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath());
        File filepath = new File(path);
        File[] files = filepath.listFiles();
        arrayList = new ArrayList<>();
        item = new ArrayList<>();
        arrayResp = new ArrayList<>();
        for (File f : files) {
            String fullPath = f.getAbsolutePath();
            int dot = fullPath.lastIndexOf(".");
            String ext = fullPath.substring(dot + 1);
            if (ext.equals("json")) {
                arrayList.add(f.getName());
                adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, arrayList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                        textView.setTextColor(getResources().getColor(R.color.colorTxt));
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
                itemtxt = adapterView.getAdapter().getItem(i).toString();
                Log.e(TAG, "txt Seleccionado " + itemtxt);
                StringBuilder txt = new StringBuilder();
                //show datos in txt
                try {
                    File mfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), itemtxt);
                    BufferedReader br = new BufferedReader(new FileReader(mfile));
                    String line;
                    while ((line = br.readLine()) != null) {
                        txt.append(line);
                        //txt.append('\n');
                        item.add(txt.toString());



                    }
                    strContent = item.toString();


                    br.close();
                } catch (IOException e) {
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


                new AsynckEncuestasTxt(AdminActivity.this, itemtxt).execute();
            }
        });

    }

    public class AsynckEncuestasTxt extends AsyncTask<String, String, String> {

        String success;
        Constantes rutas;
        Context mContext;
        JSONArray jsonArray ;
        JSONObject rJson;
        private String stringTxt;
        private ProgressDialog pDialog;
        private String URL;
        private ArrayList<NameValuePair> data;

        public AsynckEncuestasTxt(Context context, String item) {
            this.stringTxt = item;
            this.mContext = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            success = "0";
            rutas = new Constantes();
            URL = rutas.getIPWBSetService();
            data = new ArrayList<>();
            arrayResp = new ArrayList<>();
            item = new ArrayList<>();
            StringBuilder txtEnvio = new StringBuilder();
            jsonArray = new JSONArray();
            String mItem = null;
            JSONObject mJson ;
            try {
                File mfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), itemtxt);
                BufferedReader br = new BufferedReader(new FileReader(mfile));
                String line;
                while ((line = br.readLine()) != null) {
                    txtEnvio.append(line);
                    //txt.append('\n');
                    mItem = txtEnvio.toString();
                    item.add(txtEnvio.toString());
                    Log.e(TAG,"item" + item);
                    jsonArray= new JSONArray(mItem);
                }
                //TODO sube solo la primera
                data.add(new BasicNameValuePair("setEncuestas",jsonArray.toString()));
                Log.e(TAG,"data  : "+ data.toString());
                ServiceHandler serviceHandler = new ServiceHandler();
                String response = serviceHandler.makeServiceCall(URL, ServiceHandler.POST, data);
                JSONObject jsonObject = new JSONObject(response);
                JSONObject result = jsonObject.getJSONObject("result");
                success = result.getString("success").toString();
                Log.w(TAG,"success" + success);
                br.close();
            } catch (IOException e) {
                //You'll need to add proper error handling here
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPreExecute () {
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

            if (s.equals("1")) {
                //Log.e(TAG,"proceso terminado");
            } else {
                //Log.e(TAG,"error");
            }

        }
    }

}
