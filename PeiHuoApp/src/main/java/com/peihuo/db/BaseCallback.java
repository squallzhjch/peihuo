package com.peihuo.db;

import android.content.Context;
import android.content.DialogInterface;

import com.peihuo.R;
import com.peihuo.thread.ThreadManager;
import com.peihuo.thread.ThreadManager.OnDatabaseOperationRunnable;
import com.peihuo.ui.dialog.LoadingDialog;

/**
 * Created by 123 on 2017/8/31.
 */

public abstract class BaseCallback {

    private LoadingDialog mLoadingDialog;
    private Context mActivity;
    protected int loadingString = R.string.toast_loading;
    protected MySqlManager mySqlManager;

    public BaseCallback(Context activity) {
        mActivity = activity;
        mySqlManager = MySqlManager.getInstance();
        loadData();
    }

    protected abstract void loadData();

    protected void showLoading(int id, OnDatabaseOperationRunnable runnable){
        loadingString = id;
        showLoading(runnable);
    }

    protected void showLoading(final OnDatabaseOperationRunnable runnable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mActivity);
            mLoadingDialog.setText(mActivity.getText(loadingString));
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    runnable.cancel();
                }
            });
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
