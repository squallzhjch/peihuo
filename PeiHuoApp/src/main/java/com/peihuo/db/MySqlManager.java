package com.peihuo.db;

import android.content.Context;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.entity.UserInfo;
import com.peihuo.thread.ThreadManager;
import com.peihuo.ui.dialog.LoadingDialog;
import com.peihuo.util.MyLogManager;

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

    public boolean openDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://47.94.134.88:3306/yichuDev?characterEncoding=UTF-8", "root", "1234");
        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getText(R.string.toast_db_connect_error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            MyLogManager.writeLogtoFile("数据库查询", "链接数据库失败", e.toString());
            return false;
        }
        return true;
    }

    public void closeDB() {
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
            mLoadingDialog.setText(mContext.getText(R.string.toast_logining));
        }
        mLoadingDialog.show();
    }

    private void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }



    public Connection getConnection() {
        return conn;
    }
}
