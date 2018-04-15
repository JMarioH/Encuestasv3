package com.popgroup.encuestasv3;

import android.app.Application;

import com.facebook.stetho.Stetho;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Orion on 14/04/2018.
 */

public class AppEnc extends Application {


    private static AppEnc mInstance;
    private Executor customSequentialExecutor;


    public AppEnc () {
    }

    public static AppEnc getInstance () {
        return mInstance;
    }

    public Executor getExecutor () {
        if (customSequentialExecutor == null) {
            customSequentialExecutor = Executors.newSingleThreadExecutor ();
        }
        return customSequentialExecutor;
    }

    @Override
    public void onCreate () {
        super.onCreate ();
        initApplication (this);
        Stetho.initializeWithDefaults (this);
    }

    private static void initApplication (final AppEnc appEnc) {
        mInstance = appEnc;
    }
}
