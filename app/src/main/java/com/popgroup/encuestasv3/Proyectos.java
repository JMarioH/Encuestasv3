package com.popgroup.encuestasv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.CatMaster;
import com.popgroup.encuestasv3.Model.Cliente;
import com.popgroup.encuestasv3.Model.Proyecto;
import com.popgroup.encuestasv3.Model.TipoEncuesta;
import com.popgroup.encuestasv3.Model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesus.hernandez on 13/12/16.
 * Selecciona el proyecto
 */
public class Proyectos extends AppCompatActivity {

    public String TAG= getClass().getSimpleName();
    Bundle bundle;

    public ListView listProyectos;
    public List<Proyecto> proyectoList;
    public List<Cliente> clienteList;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> arrayProyectos;
    Proyecto proyecto;
    Cliente cliente;
    User user;
    public DBHelper mDBHelper;
    Dao dao;
    String nomCliente = "";
    String usuario  = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_proyectos);

        bundle = new Bundle();
        arrayProyectos = new ArrayList<>();
        proyecto = new Proyecto();
        cliente = new Cliente();
        try {

            dao = getmDBHelper().getUserDao();
            user = (User) dao.queryForId(1);
            usuario = user.getNombre();
            dao.clearObjectCache();

            dao = getmDBHelper().getClienteDao();
            clienteList = (List<Cliente>) dao.queryBuilder().distinct().selectColumns("nombre").query();
            for(Cliente item : clienteList){
                nomCliente = item.getNombre();
            }
            dao.clearObjectCache();

            dao = getmDBHelper().getProyectoDao();
          //  proyecto = (Proyecto) dao.queryForId(1);
            proyectoList = (List<Proyecto>) dao.queryBuilder().distinct().selectColumns("nombre").where().in("cliente",nomCliente).query();
            dao.clearObjectCache();

            for (Proyecto item : proyectoList) {
               arrayProyectos.add(item.getNombre()  );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayProyectos){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position,convertView,parent);
                TextView textView  = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                return view;
            }
        };
        listProyectos = (ListView) findViewById(R.id.listProyectos);
        listProyectos.setAdapter(adapter);
        listProyectos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getAdapter().getItem(i).toString();
                String  id = null;
                try {
                    dao = getmDBHelper().getProyectoDao();
                    proyectoList = (List<Proyecto>) dao.queryBuilder().distinct().selectColumns("idProyecto").where().in("nombre",value).query();
                    for (Proyecto item : proyectoList){
                        id = item.getIdproyecto();
                    }
                    dao.clearObjectCache();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                bundle.putString("usuario", usuario);
                bundle.putString("cliente", nomCliente);
                bundle.putString("idproyecto", String.valueOf(id));

                Intent intent = new Intent(Proyectos.this, TipoEncuestas.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtras(bundle);
                startActivity(intent);

                Toast.makeText(getBaseContext(),"proyecto seleccionado " + id + "value " + value,Toast.LENGTH_SHORT).show();
            }
        });
   }
    private DBHelper getmDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return mDBHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
    }
}
