package com.popgroup.encuestasv3.Login;

/**
 * Created by Orion on 14/04/2018.
 * interfaz loginPresenter
 */

public interface LoginPresenter {

    void validateUser (final String user, final String password);

    void isRegisterUser ();

    void onDestroy ();

}
