package com.educhuks.pincomponent.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Edu Chuks on 13/04/2017.
 */
public class KeyboardHelper {

    private Context context;
    public boolean showingKeyboard;

    public KeyboardHelper(Context context) {
        showingKeyboard = false;
        this.context = context;
    }

    public void showKeyboard() {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showSoftInput(view, 0);
            showingKeyboard = true;
        }
    }

    public void hideKeyboard() {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
            showingKeyboard = false;
        }
    }

}
