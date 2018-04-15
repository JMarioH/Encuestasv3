package com.popgroup.encuestasv3.Base;

/**
 * Created by Orion on 14/04/2018.
 */

public interface IView<T> {
    void showLoader (boolean show);

    void showError (Throwable throwable);

    void nextAction (Object result);

    void actionMenu ();
}
