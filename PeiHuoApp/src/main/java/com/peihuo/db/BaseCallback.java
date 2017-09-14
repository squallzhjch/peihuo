package com.peihuo.db;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.thread.ThreadManager;
import com.peihuo.thread.ThreadManager.OnDatabaseOperationRunnable;
import com.peihuo.ui.dialog.BaseToast;
import com.peihuo.ui.dialog.LoadingDialog;

/**
 * Created by 123 on 2017/8/31.
 */

public abstract class BaseCallback {

    private LoadingDialog mLoadingDialog;
    protected Context mActivity;
    protected int loadingString = R.string.toast_loading;
    protected MySqlManager mySqlManager;

    protected final int HANDLER_MES_WHAT_CONNECT_DB_ERROR = 0x001;


    protected  Handler msgHandler;

    public BaseCallback(Context activity) {
        mActivity = activity;
        mySqlManager = MySqlManager.getInstance();

        msgHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HANDLER_MES_WHAT_CONNECT_DB_ERROR:
                        BaseToast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_db_connect_error), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };

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

    protected void sendConnectDBErrorMsg(){
        if(msgHandler != null){
            msgHandler.sendEmptyMessage(HANDLER_MES_WHAT_CONNECT_DB_ERROR);
        }
    }

}
