package com.popgroup.encuestasv3.Proyectos;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.Dialog.DialogAlert;
import com.popgroup.encuestasv3.Dialog.DialogChoice;
import com.popgroup.encuestasv3.Dialog.DialogFactory;
import com.popgroup.encuestasv3.MainEncuesta.MainActivity;
import com.popgroup.encuestasv3.Model.Proyecto;
import com.popgroup.encuestasv3.R;
import com.popgroup.encuestasv3.TipoEncuesta.TipoEncuestas;

/**
 * Created by jesus.hernandez on 13/12/16.
 * Selecciona el proyecto
 */
public class Proyectos extends BaseActivity implements IProyectoView {

    public String TAG = getClass().getSimpleName();

    public ArrayAdapter<Proyecto> adapter;
    public DBHelper mDBHelper;
    public ListView listProyectos;
    private Bundle bundle;
    private ProyectosPresenter mPresenter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = new Bundle();
        if (mPresenter.getListProyectos() != null && mPresenter.getListProyectos().size() > 0) {
            adapter = new ArrayAdapter<Proyecto>(this, R.layout.simple_list_item, mPresenter.getListProyectos()) {

                @Override
                public View getView (int position, View convertView, ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    textView.setText(mPresenter.getListProyectos().get(position).getNombre());
                    textView.setGravity(Gravity.CENTER);
                    return view;
                }
            };
            listProyectos = (ListView) findViewById(R.id.listProyectos);
            listProyectos.setAdapter(adapter);

            listProyectos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
                    Proyecto proyecto = (Proyecto) adapterView.getAdapter().getItem(i);

                    bundle.putString("cliente", mPresenter.getCliente());
                    bundle.putString("idproyecto", proyecto.getIdproyecto());

                    Intent intent = new Intent(Proyectos.this, TipoEncuestas.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {
            showError(new Throwable("Sin proyectos disponibles"));
        }
    }

    @Override
    protected int setLayout () {
        return R.layout.activty_proyectos;
    }

    @Override
    protected String setTitleToolBar () {
        return "Proyeto";
    }

    @Override
    protected void createPresenter () {
        mPresenter = new ProyectosPresenter(new ProyectosInteractor(this));
        mPresenter.attachView(this);
    }

    @Override
    protected BasePresenter getmPresenter () {
        return mPresenter;
    }

    @Override
    public void showLoader (boolean show) {
    }

    @Override
    public void showError (Throwable throwable) {
        final DialogChoice dialogAlert = DialogFactory.build(this, throwable.getMessage(), true, false);
        dialogAlert.show(getSupportFragmentManager(), DialogAlert.class.getSimpleName());
    }

    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        super.onKeyDown(keyCode, event);
        return true;
    }

}

