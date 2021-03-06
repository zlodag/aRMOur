package com.skepticalone.armour.ui.common;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.skepticalone.armour.R;

public abstract class CoordinatorActivity extends AppCompatActivity implements com.skepticalone.armour.util.Snackbar {

    CoordinatorLayout mCoordinatorLayout;

    @LayoutRes
    protected abstract int getContentView();

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mCoordinatorLayout = findViewById(R.id.coordinator);
    }

    @Override
    public final void showSnackbar(@StringRes int text) {
        Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_LONG).show();
    }

}
