package com.popgroup.encuestasv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.MainEncuesta.MainActivity;
import com.popgroup.encuestasv3.Model.Cliente;
import com.popgroup.encuestasv3.Model.Proyecto;
import com.popgroup.encuestasv3.Model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jesus.hernandez on 13/12/16.
 * Selecciona el proyecto
 */
public class Proyectos extends AppCompatActivity {

    public String TAG= getClass().getSimpleName();
    public List<Proyecto> proyectoList;
    public List<Cliente> clienteList;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> arrayProyectos;
    public ArrayList<User> arrayUsers;
    public DBHelper mDBHelper;
    public String nomCliente = "";
    public String usuario  = "";
    public ListView listProyectos;
    Bundle bundle;
    Proyecto proyecto;
    Cliente cliente;
    Dao dao;
    Toolbar toolbar;
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_proyectos);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtTitle.setText("Encuestas");
        txtTitle.setTextSize(18);
        txtTitle.setTextColor(getBaseContext().getResources().getColor(R.color.colorTextPrimary));
        setSupportActionBar(toolbar);

        bundle = new Bundle();
        arrayProyectos = new ArrayList<>();
        proyecto = new Proyecto();
        cliente = new Cliente();
        try {

            dao = getmDBHelper().getUserDao();
            arrayUsers = (ArrayList<User>) dao.queryForAll();
            for (User  item : arrayUsers){
                usuario = item.getNombre();
            }

            dao.clearObjectCache();

            dao = getmDBHelper().getClienteDao();
            clienteList = (List<Cliente>) dao.queryBuilder().distinct().selectColumns("nombre").query();

            for(Cliente item : clienteList){
                nomCliente = item.getNombre();
            }
            dao.clearObjectCache();

            dao = getmDBHelper().getProyectoDao();
          //  proyecto = (Proyecto) dao.queryForId(1);
          //  proyectoList = (List<Proyecto>) dao.queryBuilder().distinct().selectColumns("nombre").where().in("cliente",nomCliente).query();
            proyectoList = (List<Proyecto>) dao.queryForAll();
            dao.clearObjectCache();
            for (Proyecto item : proyectoList) {
               arrayProyectos.add(item.getNombre()  );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        adapter = new ArrayAdapter<String>(this,R.layout.simple_list_item,arrayProyectos){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position,convertView,parent);
                TextView textView  = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                textView.setGravity(Gravity.CENTER);
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

    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        super.onKeyDown(keyCode, event);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menuInicio) {
            //Display Toast
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }else if(id== R.id.menuSalir){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }
}

