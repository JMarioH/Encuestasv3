package com.popgroup.encuestasv3.TipoEncuesta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Dialog.DialogAlert;
import com.popgroup.encuestasv3.Dialog.DialogChoice;
import com.popgroup.encuestasv3.Dialog.DialogFactory;
import com.popgroup.encuestasv3.Encuestas;
import com.popgroup.encuestasv3.MainEncuesta.MainActivity;
import com.popgroup.encuestasv3.Model.TipoEncuesta;
import com.popgroup.encuestasv3.Proyectos.Proyectos;
import com.popgroup.encuestasv3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesus.hernandez on 14/12/16.
 * Selecciona tipo de encuesta
 */
public class TipoEncuestas extends BaseActivity implements ITipoEncView {
    private String TAG = getClass().getSimpleName();
    private String idproyecto;
    private Bundle bundle;
    private List<TipoEncuesta> arrayEncuestas;
    private ArrayAdapter<TipoEncuesta> adapter;
    private ListView listView;
    private TipoEncPresenter mPresenter;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            idproyecto = getIntent().getStringExtra("idproyecto");
        }
        arrayEncuestas = new ArrayList<>();
        arrayEncuestas = mPresenter.getLisNomEncuesta(MainActivity.mUsuario, idproyecto);


        if (arrayEncuestas != null && arrayEncuestas.size() > 0) {
            adapter = new ArrayAdapter<TipoEncuesta>(this, R.layout.simple_list_item, arrayEncuestas) {
                @Override
                public View getView (int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    textView.setText(arrayEncuestas.get(position).getEncuesta());
                    textView.setGravity(Gravity.CENTER);
                    return view;

                }
            };

            listView = (ListView) findViewById(R.id.listTipoEnc);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
                    List<TipoEncuesta> arrayList = new ArrayList<>();
                    TipoEncuesta tipoEncuesta = (TipoEncuesta) adapterView.getAdapter().getItem(i);
                    arrayList = mPresenter.getListTipoEnc(MainActivity.mUsuario, idproyecto, tipoEncuesta.getEncuesta());
                    for (TipoEncuesta encuesta : arrayList) {
                        bundle.putString("idArchivo", encuesta.getIdArchivo());
                        bundle.putString("idEncuesta", encuesta.getIdEncuesta());
                        bundle.putString("idTienda", encuesta.getIdTienda());
                    }
                    Intent intent = new Intent(TipoEncuestas.this, Encuestas.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {
            showMessage();
        }
    }

    public void showMessage () {
        final DialogChoice dialogAlert = DialogFactory.build(this, "No hay encuestas disponibles.", true, false, Proyectos.class, null);
        dialogAlert.show(getSupportFragmentManager(), DialogAlert.class.getSimpleName());
    }

    @Override
    protected int setLayout () {
        return R.layout.activity_tipo_encuestas;
    }

    @Override
    protected String setTitleToolBar () {
        return "Encuestas";
    }

    @Override
    protected void createPresenter () {
        mPresenter = new TipoEncPresenter(new TipoEncInteractor(this));
        mPresenter.attachView(this);
    }

    @Override
    protected BasePresenter getmPresenter () {
        return null;
    }

    @Override
    public void showLoader (boolean show) {

    }

    @Override
    public void showError (Throwable throwable) {

    }
/*
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent (this, Proyectos.class);
            startActivity (intent);
        }
        super.onKeyDown (keyCode, event);
        return true;
    }
*/
}