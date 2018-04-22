package com.popgroup.encuestasv3.Dialog;

import android.content.Context;
import android.content.Intent;

import com.popgroup.encuestasv3.Login.LoginActivity;
import com.popgroup.encuestasv3.MainEncuesta.MainPresenter;

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

    public static DialogChoice build (final Context ctrx, final String msg, final boolean showConfirm,
                                      final boolean showCancel, final Class sendClass) {
        final DialogAlert dialog = DialogAlert.newInstance (msg, showConfirm, showCancel);
        dialog.setDialogActions (new DialogActions () {
            @Override
            public void Confirm (Object... params) {
                dialog.dismiss ();

                Intent intent = new Intent (ctrx, sendClass);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
