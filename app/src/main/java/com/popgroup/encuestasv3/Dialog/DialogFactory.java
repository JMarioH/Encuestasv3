package com.popgroup.encuestasv3.Dialog;

import android.app.Activity;
import android.content.Intent;

import com.popgroup.encuestasv3.Login.LoginActivity;

public class DialogFactory {
    // dialog basico solo muestra un mensaje

    public static DialogChoice build (final Activity ctrx, final String msg, final boolean showConfirm, final boolean showCancel) {
        final DialogAlert dialog = DialogAlert.newInstance(msg, showConfirm, showCancel);
        dialog.setDialogActions(new DialogActions() {
            @Override
            public void Confirm (Object... params) {
                dialog.dismiss();
                Intent intent = new Intent(ctrx, LoginActivity.class);
                ctrx.startActivity(intent);

            }

            @Override
            public void Cancel (Object... params) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

}
