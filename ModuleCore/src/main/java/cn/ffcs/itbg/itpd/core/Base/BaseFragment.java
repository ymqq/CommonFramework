package cn.ffcs.itbg.itpd.core.Base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenqq on 16-12-15.
 */

public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected AppCompatActivity mActivity;
    protected View mRootView;


    protected abstract int setLayout();
    protected abstract void initViews();
    protected abstract void initEvents();
    protected abstract void start();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRootView == null) {
            mContext = getContext();
            mActivity = (AppCompatActivity) getActivity();
            mRootView = inflater.inflate(setLayout(), container, false);
            initViews();
            initEvents();
            start();
        }
        return mRootView;
    }

    protected View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    protected void showSnackbar(String message) {
        Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT).show();
    }

    protected int getColor(int resId) {
        return getResources().getColor(resId);
    }
}
