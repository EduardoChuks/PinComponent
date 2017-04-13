package com.educhuks.pincomponent.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.educhuks.pincomponent.R;

/**
 * Created by Edu Chuks on 13/04/2017.
 */
public class DigitCustomView extends BaseView {

    public EditText digitView;

    public DigitCustomView(Context context) {
        super(context, R.layout.layout_digit);
    }

    public DigitCustomView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.layout_digit);
    }

    @Override
    public void inflateView() {
        super.inflateView();
        digitView = (EditText) findViewById(R.id.digitView);
        digitView.setHint("0");
    }
}
