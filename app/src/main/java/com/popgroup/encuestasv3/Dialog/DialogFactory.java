package com.popgroup.encuestasv3.Dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.popgroup.encuestasv3.FinEncuesta.FinEncuestaPresenter;
import com.popgroup.encuestasv3.Login.LoginActivity;
import com.popgroup.encuestasv3.MainEncuesta.MainActivity;
import com.popgroup.encuestasv3.MainEncuesta.MainPresenter;
import com.popgroup.encuestasv3.Model.FotoEncuesta;

public class DialogFactory {
    // dialog basico solo muestra un mensaje

    public static DialogChoice build (final Context ctrx, final String msg, final boolean showConfirm,
                                      final boolean showCancel, final MainPresenter presenter) {
        final DialogAlert dialog = DialogAlert.newInstance(msg, showConfirm, showCancel);
        dialog.setDialogActions(new DialogActions() {
            @Override
            public void Confirm (Object... params) {
                dialog.dismiss();
                presenter.clearDataBase ();
                Intent intent = new Intent(ctrx, LoginActivity.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ctrx.startActivity(intent);

            }

            @Override
            public void Cancel (Object... params) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static DialogChoice build (final Context ctrx, final String msg, final boolean showConfirm, final boolean showCancel, final FinEncuestaPresenter mPresenter, final String idEstablecimiento, final String idEncuesta, final FotoEncuesta fotoEncuesta) {
        final DialogAlert dialog = DialogAlert.newInstance(msg, showConfirm, showCancel);
        dialog.setDialogActions(new DialogActions() {
            @Override
            public void Confirm (Object... params) {
                dialog.dismiss();

                mPresenter.saveEncuesta(idEstablecimiento, idEncuesta);
                mPresenter.saveFotosEnc(fotoEncuesta, idEstablecimiento, idEncuesta);
                Intent intent = new Intent(ctrx, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ctrx.startActivity(intent);

            }

            @Override
            public void Cancel (Object... params) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static DialogChoice build (final Context ctrx, final String msg, final boolean showConfirm,
                                      final boolean showCancel) {
        final DialogAlert dialog = DialogAlert.newInstance (msg, showConfirm, showCancel);
        dialog.setDialogActions (new DialogActions () {
            @Override
            public void Confirm (Object... params) {
                dialog.dismiss ();
            }

            @Override
            public void Cancel (Object... params) {
                dialog.dismiss ();
            }
        });
        return dialog;
    }

    public static DialogChoice build (final Context ctrx, final String msg, final boolean showConfirm, final boolean showCancel, final Class sendClass, final Bundle bundle) {
        final DialogAlert dialog = DialogAlert.newInstance (msg, showConfirm, showCancel);
        dialog.setDialogActions (new DialogActions () {
            @Override
            public void Confirm (Object... params) {
                dialog.dismiss ();

                Intent intent = new Intent (ctrx, sendClass);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                ctrx.startActivity (intent);

            }

            @Override
            public void Cancel (Object... params) {
                dialog.dismiss ();
            }
        });
        return dialog;
    }


}
