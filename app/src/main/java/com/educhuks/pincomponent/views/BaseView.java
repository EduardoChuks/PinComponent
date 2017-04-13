package com.educhuks.pincomponent.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.educhuks.pincomponent.data.PinConstants;

/**
 * Created by Edu Chuks on 13/04/2017.
 */
public class BaseView extends RelativeLayout{

    private int resource;
    protected PinConstants pinConstants;

    public BaseView(Context context, int newResource) {
        super(context);
        resource = newResource;
        pinConstants = new PinConstants();
        inflateView();
    }

    public BaseView(Context context, AttributeSet attrs, int newResource) {
        super(context, attrs);
        resource = newResource;
        pinConstants = new PinConstants();
        inflateView();
    }

    public void inflateView() {
        inflate(getContext(), resource, this);
    }
}
