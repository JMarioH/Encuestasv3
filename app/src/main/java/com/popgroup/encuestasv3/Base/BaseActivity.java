package com.popgroup.encuestasv3.Base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.popgroup.encuestasv3.Loader;
import com.popgroup.encuestasv3.R;

import butterknife.ButterKnife;

/**
 * Created by jmario on 14/04/2018.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;
    public Loader mLoader;
    protected BasePresenter mPresenter;
    private Toolbar toolbar;
    private TextView txtTitle;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (setLayout ());
        ButterKnife.bind (this);

        toolbar = (Toolbar) findViewById (R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle ("");
        }
        if (getSupportActionBar () != null) // Habilitar up button
            getSupportActionBar ().setDisplayHomeAsUpEnabled (true);

        txtTitle = (TextView) toolbar.findViewById (R.id.txtTitle);
        txtTitle.setText (setTitleToolBar ());
        txtTitle.setTextSize (18);
        txtTitle.setTextColor (getBaseContext ().getResources ().getColor (R.color.colorTextPrimary));
        setSupportActionBar (toolbar);
        createPresenter ();
        initLoader ();
    }

    @LayoutRes
    protected abstract int setLayout ();

    protected abstract String setTitleToolBar ();

    protected abstract void createPresenter ();

    private void initLoader () {
        mProgressDialog = new ProgressDialog (this);
        mProgressDialog.setProgressStyle (ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setOnShowListener (new DialogInterface.OnShowListener () {
            @Override
            public void onShow (final DialogInterface dialog) {
                final ProgressBar progressBar = (ProgressBar) mProgressDialog.findViewById (android.R.id.progress);
                progressBar.getProgressDrawable ().setColorFilter (
                        ContextCompat.getColor (getBaseContext (), R.color.colorAccent),
                        android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });
        mProgressDialog.setCancelable (false);

        mLoader = (Loader) findViewById (R.id.layout_loader);


    }

    @Override
    protected void onStop () {
        super.onStop ();
        if (mPresenter != null) {
            mPresenter.onStop ();
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        if (mPresenter != null) {
            mPresenter.onDestroy ();
        }

    }

    protected abstract BasePresenter getmPresenter ();

    @Override
    protected void onResume () {
        super.onResume ();
        if (mPresenter != null) {
            mPresenter.onResume ();
        }
    }

    protected void showProgressDialog (final String message, final Integer progress) {
        if (mProgressDialog != null) {
            mProgressDialog.setProgress (progress);
            mProgressDialog.setMessage (message);

            if (!mProgressDialog.isShowing ()) {
                mProgressDialog.show ();
            }
        }
    }

    protected void hideProgtessDialog () {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss ();
        }
    }

}
