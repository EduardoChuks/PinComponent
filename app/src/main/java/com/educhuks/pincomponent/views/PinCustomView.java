package com.educhuks.pincomponent.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.educhuks.pincomponent.R;
import com.educhuks.pincomponent.util.KeyboardHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edu Chuks on 13/04/2017.
 */
public class PinCustomView extends RelativeLayout {

    private EditText hiddenEditText;
    private LinearLayout pinContainer;
    private View helperView;

    private List<DigitCustomView> digits;
    private KeyboardHelper keyboardHelper;

    private int digitsQuant;
    private String hintChar;

    private OnClickListener helperListener = new OnClickListener() {
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
    };

    private TextWatcher mainTextWatcher = new TextWatcher() {
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
                if (length < digitsQuant) {
                    for (int i = length; i < digitsQuant; i++) {
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
    };

    private TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                keyboardHelper.showingKeyboard = false;
            }
            return false;
        }
    };

    public PinCustomView(Context context) {
        super(context);
        setContentView();
        initUI();
        setUI();
        setListeners();
    }

    public PinCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setContentView();
        setupAttrs(context, attrs);
        initUI();
        setUI();
        setListeners();
    }

    private void setContentView() {
        inflate(getContext(), R.layout.layout_pin, this);
        digitsQuant = 5;
        hintChar = "0";
    }

    private void setupAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PinCustomView, 0, 0);
        try {
            digitsQuant = ta.getInteger(R.styleable.PinCustomView_quantity, 5);
            hintChar = ta.getString(R.styleable.PinCustomView_hintChar);
        } finally {
            ta.recycle();
        }
    }

    private void initUI() {
        hiddenEditText = (EditText) findViewById(R.id.hiddenEditText);
        pinContainer = (LinearLayout) findViewById(R.id.pinContainer);
        helperView = findViewById(R.id.helperView);
    }

    private void setUI() {
        digits = new ArrayList<>();
        keyboardHelper = new KeyboardHelper(getContext());

        InputFilter[] hiddenEditTextFilters = {new InputFilter.LengthFilter(digitsQuant)};
        hiddenEditText.setFilters(hiddenEditTextFilters);

        for (int i = 0; i < digitsQuant; i++) {
            DigitCustomView digit = new DigitCustomView(getContext(), hintChar);
            pinContainer.addView(digit);
            digits.add(digit);
        }
        setHelperViewSize();
    }

    private void setListeners() {
        helperView.setOnClickListener(helperListener);

        hiddenEditText.addTextChangedListener(mainTextWatcher);
        hiddenEditText.setOnEditorActionListener(actionListener);
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

    public String getValue() {
        return hiddenEditText != null ? hiddenEditText.getText().toString() : "";
    }

    /**
     * INFLATE HELPERS
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

}
