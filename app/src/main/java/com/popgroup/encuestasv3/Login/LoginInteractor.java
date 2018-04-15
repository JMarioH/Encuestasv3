package com.popgroup.encuestasv3.Login;

import com.popgroup.encuestasv3.Model.User;

/**
 * Created by Orion on 14/04/2018.
 */

public interface LoginInteractor {

    void login (String user, String pass);

    User getUser ();


}
