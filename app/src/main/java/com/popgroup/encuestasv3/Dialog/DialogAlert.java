package com.popgroup.encuestasv3.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.popgroup.encuestasv3.R;

public class DialogAlert extends DialogChoice {

    private static final String TAG = DialogAlert.class.getSimpleName();
    private String message;

    public static DialogAlert newInstance (final String msg, final boolean showConfirm, final boolean showCancel) {
        Bundle args = new Bundle();
        args.putString("Msg", msg);
        args.putBoolean(KEY_SHOW_CONFIRM, showConfirm);
        args.putBoolean(KEY_SHOW_CANCEL, showCancel);
        DialogAlert fragment = new DialogAlert();
        fragment.setCancelable (false);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString("Msg");
            showConfirmBtn = getArguments().getBoolean(KEY_SHOW_CONFIRM);
            showCancelBtn = getArguments().getBoolean(KEY_SHOW_CANCEL);
        }

    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtMsg = (TextView) view.findViewById(R.id.txtAlert);
        txtMsg.setText(message);
    }

    @Override
    public int getLayoutDialogContent () {
        return R.layout.dialog_alert;
    }
}


