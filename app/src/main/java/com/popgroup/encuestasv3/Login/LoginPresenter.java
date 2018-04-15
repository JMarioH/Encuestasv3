package com.popgroup.encuestasv3.Login;

/**
 * Created by Orion on 14/04/2018.
 */

public interface LoginPresenter {

    void validateUser (String user, String password);

    void isRegisterUser ();

    void onDestroy ();
}
