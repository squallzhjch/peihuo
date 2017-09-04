package com.peihuo.db;

import android.content.Context;

import com.peihuo.entity.UserInfo;
import com.peihuo.thread.ThreadManager;
import com.peihuo.util.MyLogManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by 123 on 2017/8/28.
 */

public class LoginCallback  extends BaseCallback{

    private Context mContext;
    private String mAccount;
    private String mPassword;

    private OnLoginCallbackListener mListener;

    public interface OnLoginCallbackListener{
        void onSuccess(UserInfo value);
        void onError();
    }

    public LoginCallback(Context context, String account, final String password, OnLoginCallbackListener listener){
        super(context);
        mAccount = account;
        mPassword = password;
        mListener = listener;
    }


    @Override
    protected void loadData() {

        ThreadManager.OnDatabaseOperationRunnable runnable = new ThreadManager.OnDatabaseOperationRunnable<UserInfo>() {
            @Override
            public UserInfo doInBackground() {
                UserInfo userInfo = null;
                if (mySqlManager.openDB()) {
                    Statement statement = null;
                    ResultSet result = null;

                    String sql = "SELECT * FROM t_user WHERE account = \"" + mAccount + "\" AND upassword = \"" + mPassword + "\";";
                    MyLogManager.writeLogtoFile("数据库查询", "登录", sql);
                    try {
                        statement = mySqlManager.getConnection().createStatement();
                        statement.setQueryTimeout(20);
                        result = statement.executeQuery(sql);
                        if (result != null && result.first()) {
                            userInfo = new UserInfo();
                            int nameIndex = result.findColumn("uname");
                            int uIdIndex = result.findColumn("uid");
                            userInfo.setUserName(result.getString(nameIndex));
                            userInfo.setUserId(result.getString(uIdIndex));
                            userInfo.setAccount(mAccount);
                        }
                    } catch (SQLException e) {
                        MyLogManager.writeLogtoFile("数据库查询", "失败", e.toString());
                        e.printStackTrace();
                    } finally {
                        try {
                            if (result != null) {
                                result.close();
                                result = null;
                            }
                            if (statement != null) {
                                statement.close();
                                statement = null;
                            }
                        } catch (SQLException sqle) {

                        }
                        mySqlManager.closeDB();
                    }
                }
                return userInfo;
            }

            @Override
            public void onSuccess(UserInfo value) {
                dismissLoading();
                if (mListener != null) {
                    if (value == null) {
                        mListener.onError();
                    } else {
                        mListener.onSuccess(value);
                    }
                }
            }

            @Override
            public void onOperationFailed(Exception e) {
                super.onOperationFailed(e);
                dismissLoading();
            }
        };
        showLoading(runnable);
        ThreadManager.getInstance().getHandler().post(runnable);
    }
}
