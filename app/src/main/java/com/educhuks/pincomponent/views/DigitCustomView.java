package com.educhuks.pincomponent.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.educhuks.pincomponent.R;

/**
 * Created by Edu Chuks on 13/04/2017.
 */
public class DigitCustomView extends RelativeLayout {

    public EditText digitView;

    public String hintChar;

    public DigitCustomView(Context context) {
        super(context);
        inflateView();
    }

    public DigitCustomView(Context context, String hintChar) {
        super(context);
        this.hintChar = hintChar;
        inflateView();
    }

    public DigitCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    private void inflateView() {
        inflate(getContext(), R.layout.layout_digit, this);
        digitView = (EditText) findViewById(R.id.digitView);
        digitView.setHint(hintChar);
    }

}
