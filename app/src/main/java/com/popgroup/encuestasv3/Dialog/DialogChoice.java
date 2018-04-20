package com.popgroup.encuestasv3.Dialog;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.popgroup.encuestasv3.R;

/**
 * Created by JMario. on 20/4/2018.
 */

public abstract class DialogChoice extends DialogFragment {

    private static String TAG = DialogChoice.class.getSimpleName();
    public static final String KEY_BTN_CONFIRM_TITLE = "KEY_BTN_CONFIRM_TITLE";
    public static final String KEY_BTN_CONFIRM_CANCEL = "KEY_BTN_CONFIRM_CANCEL";
    public static final String KEY_SHOW_CONFIRM = "KEY_SHOW_BTN_CONFIRM";
    public static final String KEY_SHOW_CANCEL = "KEY_SHOW_BTN_CANCEL";
    protected static Typeface sansRegular, sansBold, sansLight, titliumRegular, titliumLight, titliumBold;
    public Boolean showConfirmBtn = true;
    public Boolean showCancelBtn = true;
    private String titleBtnConfirm;
    private String titleBtnCancel;
    private DialogActions dialogActions;

    @Override
    public void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            final Bundle arg = getArguments();
            titleBtnConfirm = arg.getString(KEY_BTN_CONFIRM_TITLE, "");
            titleBtnCancel = arg.getString(KEY_BTN_CONFIRM_CANCEL, "");
            showConfirmBtn = arg.getBoolean(KEY_SHOW_CONFIRM);
            showCancelBtn = arg.getBoolean(KEY_SHOW_CANCEL);
        }
    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mMainView = inflater.inflate(R.layout.dialog_choice_layout, container, false);
        final FrameLayout mFrameLayout = (FrameLayout) mMainView.findViewById(R.id.content);
        mFrameLayout.addView(inflater.inflate(getLayoutDialogContent(), mFrameLayout, false));
        return mMainView;
    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Button btnConfirmacion = (Button) view.findViewById(R.id.btnAceptar);
        btnConfirmacion.setTypeface(sansRegular);

        final Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setTypeface(sansRegular);

        btnConfirmacion.setVisibility(showConfirmBtn ? View.VISIBLE : View.GONE);
        btnCancel.setVisibility(showCancelBtn ? View.VISIBLE : View.GONE);


        if (!titleBtnConfirm.isEmpty()) {
            btnConfirmacion.setText(titleBtnConfirm);
        }
        if (!titleBtnCancel.isEmpty()) {
            btnCancel.setText(titleBtnCancel);
        }

        btnConfirmacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (final View view) {
                dialogActions.Confirm();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (final View view) {
                dialogActions.Cancel();
            }
        });

    }

    @Override
    public void onResume () {
        final ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) getResources().getDimension(R.dimen.dialog_choice_status_weight);
        params.height = (int) getResources().getDimension(R.dimen.dialog_choice_status_height);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    public abstract int getLayoutDialogContent ();

    public void setDialogActions (final DialogActions dialogActions) {
        this.dialogActions = dialogActions;
    }

}
