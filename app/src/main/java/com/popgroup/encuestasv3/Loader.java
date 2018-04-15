package com.popgroup.encuestasv3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Orion on 14/04/2018.
 */

public class Loader extends RelativeLayout {
    private String mText;
    private boolean touch_disabled = true;

    public Loader (final Context context) {
        super (context);
    }

    public Loader (final Context context, final AttributeSet attrs) {
        super (context, attrs);
    }

    public Loader (final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super (context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent (MotionEvent e) {
        return touch_disabled;
    }

    public void disableTouch (boolean value) {
        touch_disabled = value;
    }


    public void initUI () {
        final ProgressBar progressBarLoader = (ProgressBar) findViewById (R.id.loader_progress_bar);
        final TextView textView = (TextView) findViewById (R.id.txtLoader);
        if (progressBarLoader != null) {
            progressBarLoader.getIndeterminateDrawable ().setColorFilter (getResources ().getColor (R.color.loader), android.graphics.PorterDuff.Mode.MULTIPLY);
            if (mText != null && mText != "") {
                textView.setTextSize (22);
                textView.setText (mText);
            }
        }

    }

    public void setTextLoader (String text) {
        this.mText = text;
    }
}

