package com.popgroup.encuestasv3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Model.Proyecto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesus.hernandez on 13/12/16.
 */
public class Proyectos extends AppCompatActivity {

    public String TAG= getClass().getSimpleName();
    Bundle bundle;

    public ListView listProyectos;
    public List<Proyecto> proyectoList;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> arrayProyectos;
    Proyecto proyecto;
    String cliente;

    public DBHelper mDBHelper;
    Dao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_proyectos);

        bundle = new Bundle();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayProyectos){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);

            }
        };

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
