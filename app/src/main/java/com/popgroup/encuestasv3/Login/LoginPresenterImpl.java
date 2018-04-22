package com.popgroup.encuestasv3.Login;

import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Base.IView;

/**
 * Created by Jmario on 14/04/2018.
 * LoginPresenter
 */

public class LoginPresenterImpl extends BasePresenter implements LoginPresenter {

    private LoginInteractorImpl mBaseInteractor;

    public LoginPresenterImpl (BaseInteractor interactor) {
        super (interactor);
        this.mBaseInteractor = (LoginInteractorImpl) interactor;
        mBaseInteractor.attachCallBack (this);

    }

    @Override
    public void validateUser (String user, String password) {
        if (!user.isEmpty () && !password.isEmpty ()) {
            if (getView () != null) {
                getView ().showLoader (true);
            }
            mBaseInteractor.login (user, password);
        } else {
            if (getView () != null) {
                getView ().showError (new Throwable ("Debe completar los campos"));
            }
        }

    }

    @Override
    public void isRegisterUser () {
        if (getView () != null && mBaseInteractor.getUser () != null) {
            getView ().actionMenu ();
        }
    }

    @Override
    public ILoginView getView () {
        return mView != null ? (ILoginView) mView : null;
    }

    @Override
    public void attachView (IView view) {
        super.attachView (view);
    }

    @Override
    public LoginInteractorImpl getInteractor () {
        return mBaseInteractor;
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();

    }

    @Override
    public void onSuccess (Object result) {
        if (getView () != null) {
            getView ().showLoader (false);
            getView ().nextAction (result);
        }

    }

    @Override
    public void onFailed (Throwable throwable) {
        if (getView () != null) {
            getView ().showLoader (false);
            getView ().showError (throwable);
        }
    }
}
