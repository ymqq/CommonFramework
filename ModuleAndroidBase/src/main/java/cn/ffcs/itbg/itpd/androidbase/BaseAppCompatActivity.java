package cn.ffcs.itbg.itpd.androidbase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by chenqq on 16-12-15.
 *
 *
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initEvents();
        start();
    }

    public void showSnackbar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    public View getView() {
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        FrameLayout content = (FrameLayout) viewGroup.findViewById(android.R.id.content);
        return content.getChildAt(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.removeActivity(this);
    }

    protected abstract void initViews();
    protected abstract void initEvents();
    protected abstract void start();

}
