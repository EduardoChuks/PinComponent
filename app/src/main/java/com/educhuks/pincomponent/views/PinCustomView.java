package com.educhuks.pincomponent.views;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.educhuks.pincomponent.util.KeyboardHelper;
import com.educhuks.pincomponent.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edu Chuks on 13/04/2017.
 */
public class PinCustomView extends BaseView{

    private EditText hiddenEditText;
    private LinearLayout pinContainer;
    private View helperView;

    private List<DigitCustomView> digits;
    private KeyboardHelper keyboardHelper;

    public PinCustomView(Context context) {
        super(context, R.layout.layout_pin);
        setUI();
        setListeners();
    }

    public PinCustomView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.layout_pin);
        setUI();
        setListeners();
    }

    private void setUI() {
        InputFilter[] hiddenEditTextFilters = {new InputFilter.LengthFilter(pinConstants.DIGITS_NUMBER)};
        hiddenEditText.setFilters(hiddenEditTextFilters);
    }

    private void setListeners() {
        helperView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hiddenEditText.hasFocus()) {
                    View view = ((Activity) getContext()).getCurrentFocus();
                    view.clearFocus();
                    hiddenEditText.requestFocus();
                    keyboardHelper.showKeyboard();
                } else if (!keyboardHelper.showingKeyboard) {
                    keyboardHelper.showKeyboard();
                }

            }
        });
        hiddenEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = hiddenEditText.length();
                String num = hiddenEditText.getText().toString();
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        digits.get(i).digitView.setText("" + num.charAt(i));
                    }
                    if (length < pinConstants.DIGITS_NUMBER) {
                        for (int i = length; i < pinConstants.DIGITS_NUMBER; i++) {
                            digits.get(i).digitView.setText("");
                        }
                    } else {
                        keyboardHelper.hideKeyboard();
                    }
                } else {
                    digits.get(0).digitView.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        hiddenEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    keyboardHelper.showingKeyboard = false;
                }
                return false;
            }
        });
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            KeyEvent.DispatcherState state = getKeyDispatcherState();
            if (state != null) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                    state.startTracking(event, this);
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled() && state.isTracking(event)) {
                    keyboardHelper.hideKeyboard();
                    return true;
                }
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }

    /**
     * INFLATE PIN VIEW
     ***********************************************************************************************
     */

    private void resizeHelperView(int width, int height) {
        LayoutParams layoutParams = new LayoutParams(width, height);
        helperView.setLayoutParams(layoutParams);
    }

    private void setHelperViewSize() {
        pinContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pinContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                resizeHelperView(pinContainer.getWidth(), pinContainer.getHeight());
            }
        });
    }

    @Override
    public void inflateView() {
        super.inflateView();
        digits = new ArrayList<>();
        keyboardHelper = new KeyboardHelper(getContext());
        hiddenEditText = (EditText) findViewById(R.id.hiddenEditText);
        pinContainer = (LinearLayout) findViewById(R.id.pinContainer);
        helperView = findViewById(R.id.helperView);
        for (int i = 0; i < pinConstants.DIGITS_NUMBER; i++) {
            DigitCustomView digit = new DigitCustomView(getContext());
            pinContainer.addView(digit);
            digits.add(digit);
        }
        setHelperViewSize();
    }
}
