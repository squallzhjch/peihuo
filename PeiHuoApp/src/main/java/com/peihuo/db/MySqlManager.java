package com.peihuo.db;

import android.content.Context;
import android.widget.Toast;

import com.peihuo.entity.UserInfo;
import com.peihuo.thread.LoginCallback;
import com.peihuo.thread.ThreadManager;
import com.peihuo.ui.dialog.LoadingDialog;
import com.peihuo.util.LogManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by 123 on 2017/8/28.
 */

public class MySqlManager {

    private static volatile MySqlManager mInstance;
    private Connection conn = null;
    private LoadingDialog mLoadingDialog;
    private Context mContext;

    public static MySqlManager getInstance() {

        if (mInstance == null) {
            synchronized (MySqlManager.class) {
                if (mInstance == null) {
                    mInstance = new MySqlManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
    }

    private boolean openDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://47.94.134.88:3306/yichuDev?characterEncoding=UTF-8", "root", "1234");
        } catch (Exception e) {
            Toast.makeText(mContext, "数据库连接失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            LogManager.writeLogtoFile("数据库查询", "链接数据库失败", e.toString());
            return false;
        }
        return true;
    }

    private void closeDB() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
            mLoadingDialog.setText("正在登陆");
        }
        mLoadingDialog.show();
    }

    private void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

    public void login(final String uid, final String password, final LoginCallback loginCallback) {
        showLoading();
        ThreadManager.getInstance().getHandler().post(new ThreadManager.OnDatabaseOperationRunnable<UserInfo>() {
            @Override
            public UserInfo doInBackground() {
                UserInfo userInfo = null;
                if (openDB()) {
                    Statement statement = null;
                    ResultSet result = null;

                    String sql = "SELECT * FROM t_user WHERE uid = \"" + uid + "\" AND upassword = \"" + password + "\";";
                    LogManager.writeLogtoFile("数据库查询", "登录", sql);
                    try {
                        statement = conn.createStatement();
                        result = statement.executeQuery(sql);
                        if (result != null && result.first()) {
                            userInfo = new UserInfo();
                            int nameIndex = result.findColumn("uname");
                            int uIdIndex = result.findColumn("uid");
                            userInfo.setUserName(result.getString(nameIndex));
                            userInfo.setUserId(result.getString(uIdIndex));
//                int nameColumnIndex = result.findColumn("upassword");
//                while (!result.isAfterLast()) {
//                    Log.e("jingo", result.getString(idColumnIndex));
//                    Log.e("jingo", result.getString(nameColumnIndex));
//                    result.next();
//                }
                        }
                    } catch (SQLException e) {
                        LogManager.writeLogtoFile("数据库查询", "失败", e.toString());
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
                        closeDB();
                    }
                }
                return userInfo;
            }

            @Override
            public void onSuccess(UserInfo value) {
                dismissLoading();
                if (loginCallback != null) {
                    if (value == null) {
                        loginCallback.onError();
                    } else {
                        loginCallback.onSuccess(value);
                    }
                }
            }

            @Override
            public void onOperationFailed(Exception e) {
                super.onOperationFailed(e);
                dismissLoading();
            }
        });

    }
}
