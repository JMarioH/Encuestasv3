package com.popgroup.encuestasv3.Login;

import com.popgroup.encuestasv3.Base.BaseInteractor;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.Base.IView;

/**
 * Created by Jmario on 14/04/2018.
 */

public class LoginPresenterImpl extends BasePresenter implements LoginPresenter {

    private LoginInteractorImpl mBaseInteractor;

    public LoginPresenterImpl (BaseInteractor mBaseInteractor) {
        super (mBaseInteractor);
        this.mBaseInteractor = (LoginInteractorImpl) mBaseInteractor;

        mBaseInteractor.attachCallBack (this);

    }

    @Override
    public void validateUser (String user, String password) {
        if (!user.isEmpty () && !password.isEmpty ()) {
            if (mView != null) {
                mView.showLoader (true);
            }
            mBaseInteractor.login (user, password);
        } else {
            if (mView != null) {
                mView.showError (new Throwable ("Debe completar los campos"));
            }
        }

    }

    @Override
    public void isRegisterUser () {
        if (mView != null && mBaseInteractor.getUser () != null) {
            mView.actionMenu ();
        }
    }

    @Override
    public void onSuccess (Object result) {
        if (mView != null) {
            mView.showLoader (false);
            mView.nextAction (result);
        }

    }

    @Override
    public void onFailed (Throwable throwable) {
        if (mView != null) {
            mView.showLoader (false);
            mView.showError (throwable);
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
        return mBaseInteractor != null ? mBaseInteractor : null;
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();

    }
}
