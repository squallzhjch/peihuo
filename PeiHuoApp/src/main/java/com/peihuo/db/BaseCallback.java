package com.peihuo.db;

import android.app.Activity;
import android.content.Context;

import com.peihuo.R;
import com.peihuo.ui.dialog.LoadingDialog;

/**
 * Created by 123 on 2017/8/31.
 */

public abstract class BaseCallback {

    private LoadingDialog mLoadingDialog;
    private Context mActivity;
    private int loadingString = R.string.loading;
    protected MySqlManager mySqlManager;

    public BaseCallback(Context activity) {
        mActivity = activity;
        mySqlManager = MySqlManager.getInstance();
        loadData();
    }

    protected abstract void loadData();

    protected void showLoading(int id){
        loadingString = id;
        showLoading();
    }

    protected void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mActivity);
            mLoadingDialog.setText(mActivity.getText(loadingString));
        }
        mLoadingDialog.show();
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

}
