package com.popgroup.encuestasv3.MainEncuesta;

import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Base.IView;

/**
 * Created by JMario. on 18/4/2018.
 */

public class MainPresenter extends BasePresenter implements IMainPresenter {

    private MainInteractor mainInteractor;

    public MainPresenter (BaseInteractor mBaseInteractor) {
        super(mBaseInteractor);
        this.mainInteractor = (MainInteractor) mBaseInteractor;
        ((MainInteractor) mBaseInteractor).attachCallBack(this);
    }

    @Override
    public void enviarEncuestaPendientes () {

        if (getView() != null) {
            getView().showLoader(true);
        }
        mainInteractor.enviarEncPendientes();
    }

    @Override
    public void enviarFotosPendientes () {
        if (getView() != null) {
            getView().showLoader(true);
        }
        mainInteractor.enviarFotosPendientes();
    }

    @Override
    public IMainView getView () {
        return mView != null ? (IMainView) mView : null;
    }

    @Override
    public void attachView (IView view) {
        super.attachView(view);
    }

    @Override
    public BaseInteractor getInteractor () {
        return mBaseInteractor != null ? mBaseInteractor : null;
    }

    @Override
    public void onSuccess (Object result) {
        if (getView() != null) {
            getView().showLoader(false);
        }
    }

    @Override
    public void onFailed (Throwable throwable) {
        if (getView() != null) {
            getView().showLoader(false);
            getView().showError(throwable);
        }
    }
}
