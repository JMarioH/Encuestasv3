package com.popgroup.encuestasv3.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.popgroup.encuestasv3.AdminActivity;
import com.popgroup.encuestasv3.Base.BaseActivity;
import com.popgroup.encuestasv3.Base.BasePresenter;
import com.popgroup.encuestasv3.BuildConfig;
import com.popgroup.encuestasv3.MainEncuesta.MainActivity;
import com.popgroup.encuestasv3.R;

import java.util.ArrayList;

import butterknife.BindView;
import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by jesus.hernandez on 07/12/16.
 * loginActivity
 */

public class LoginActivity extends BaseActivity implements ILoginView {

    public String usuario;
    public ArrayList<NameValuePair> data;

    @BindView (R.id.editUser)
    EditText editUser;
    @BindView (R.id.editPassword)
    EditText editPassword;
    @BindView (R.id.btnLogin)
    Button btnLogin;

    private LoginPresenterImpl mPresenter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mPresenter.isRegisterUser ();
        mLoader.setTextLoader ("Validando Datos..");
        mLoader.disableTouch (true);
        mLoader.initUI ();
        if (BuildConfig.DEBUG) {
            editUser.setText (BuildConfig.USER);
            editPassword.setText (BuildConfig.PASS);
        }
        btnLogin.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                mPresenter.validateUser (editUser.getText ().toString (), editPassword.getText ().toString ());
            }
        });

    }

    @Override
    protected int setLayout () {
        return R.layout.activity_login;
    }

    @Override
    protected String setTitleToolBar () {
        return "Encuestas";
    }

    @Override
    protected void createPresenter () {
        mPresenter = new LoginPresenterImpl (new LoginInteractorImpl (this));
        mPresenter.attachView (this);
    }

    @Override
    protected BasePresenter getmPresenter () {
        return mPresenter != null ? (LoginPresenterImpl) mPresenter : null;
    }

    @Override
    public void showLoader (boolean show) {
        if (mLoader != null) {
            mLoader.setVisibility (show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showError (Throwable throwable) {
        final AlertDialog alertDialog = new AlertDialog.Builder (this).create ();
        alertDialog.setTitle ("Mensaje");
        alertDialog.setMessage (throwable.getMessage ());
        alertDialog.setButton (AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener () {
            public void onClick (DialogInterface dialog, int which) {
                dialog.dismiss ();
            }
        });
        alertDialog.setOnShowListener (new DialogInterface.OnShowListener () {
            @Override
            public void onShow (DialogInterface arg0) {
                alertDialog.getButton (AlertDialog.BUTTON_POSITIVE).setTextColor (getResources ().getColor (R.color.colorPrimary));
            }
        });
        alertDialog.show ();
    }

    @Override
    public void nextAction (Object result) {
        if (result.equals ("1")) {
            Intent intent = new Intent (this, MainActivity.class);
            intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.startActivity (intent);
        } else if (result.equals ("2")) {
            Intent intent = new Intent (this, AdminActivity.class);
            intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.startActivity (intent);
        } else {
            Toast.makeText (this, "Datos incorrectos", Toast.LENGTH_LONG).show ();
        }
    }

    @Override
    public void actionMenu () {
        Intent intent = new Intent (this, MainActivity.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity (intent);
    }

}
