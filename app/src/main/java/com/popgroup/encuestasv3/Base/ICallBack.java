package com.popgroup.encuestasv3.Base;

/**
 * Created by Orion on 14/04/2018.
 */

public interface ICallBack<T> {
    void onSuccess (String result);

    void onFailed (Throwable throwable);
}
