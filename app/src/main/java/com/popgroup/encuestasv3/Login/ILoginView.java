package com.popgroup.encuestasv3.Login;

import com.popgroup.encuestasv3.Base.IView;

/**
 * Created by Orion on 14/04/2018.
 */

public interface ILoginView extends IView {

    void nextAction (String result);

    void actionMenu ();
}
