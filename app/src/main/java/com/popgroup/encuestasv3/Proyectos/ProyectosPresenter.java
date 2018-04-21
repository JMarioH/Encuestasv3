package com.popgroup.encuestasv3.Proyectos;

import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Base.IView;
import com.popgroup.encuestasv3.Model.Proyecto;

import java.util.List;

/**
 * Created by Orion on 21/04/2018.
 */

public class ProyectosPresenter extends BasePresenter implements IProyectoPresenter {

    private ProyectosInteractor mBaseInteractor;

    public ProyectosPresenter (BaseInteractor mBaseInteractor) {
        super (mBaseInteractor);
        this.mBaseInteractor = (ProyectosInteractor) mBaseInteractor;
        ((ProyectosInteractor) mBaseInteractor).attachCallBack (this);
    }

    @Override
    public void onSuccess (Object result) {

    }

    @Override
    public void onFailed (Throwable throwable) {

    }

    @Override
    public String getCliente () {
        return mBaseInteractor.getCliente ();
    }

    @Override
    public List<Proyecto> getListProyectos () {
        return mBaseInteractor.getListProyectos ();
    }

    @Override
    public IView getView () {
        return null;
    }

    @Override
    public BaseInteractor getInteractor () {
        return mBaseInteractor != null ? mBaseInteractor : null;
    }
}
