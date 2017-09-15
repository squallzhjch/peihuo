package com.peihuo.db;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.system.SystemConfig;
import com.peihuo.thread.ThreadManager;
import com.peihuo.util.MyLogManager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hb on 2017/9/5.
 */

public class UpdateAcceptanceErrorCallback extends BaseCallback {

    private String mCode;
    private String mOrderId;
    private OnUpdateDataListener mListener;
    public interface OnUpdateDataListener{
        void onSuccess();
    }


    /**
     * @param activity //@param userId    用户ID
     *                 //@param orderId   订单ID
     *                 //     * @param handcodes 加工好
     */
    public UpdateAcceptanceErrorCallback(Context activity, String code, String orderId, OnUpdateDataListener listener) {
        super(activity);
        mCode = code;
        mOrderId = orderId;
        mListener = listener;
    }

    @Override
    protected void loadData() {
        ThreadManager.OnDatabaseOperationRunnable<Boolean> runnable = new ThreadManager.OnDatabaseOperationRunnable<Boolean>() {
            @Override
            public Boolean doInBackground() {
                if (mySqlManager.openDB()) {
                    Statement statement = null;
                    ResultSet result = null;
//                    MyLogManager.writeLogtoFile("数据库查询", "分拣完成", sql);
                    try {
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                        Date date = new Date(System.currentTimeMillis());
                        String sql = "UPDATE t_acceptanceform set acceptancestate='3' " +
                                "where acceptanceformcode = '" + mCode + "'";
                        mySqlManager.getConnection().setAutoCommit(false);
                        statement = mySqlManager.getConnection().createStatement();
                        statement.setQueryTimeout(SystemConfig.DB_CONNECT_TIME);
                        MyLogManager.writeLogtoFile("数据库更新", "检验未通过", sql);
                        int count = statement.executeUpdate(sql);

                        sql = "UPDATE t_orders set ordersState='7' " +
                                "where ordersId = '" + mOrderId + "'";
                        mySqlManager.getConnection().setAutoCommit(false);
                        statement = mySqlManager.getConnection().createStatement();
                        MyLogManager.writeLogtoFile("数据库更新", "检验未通过", sql);
                        count = statement.executeUpdate(sql);

                        mySqlManager.getConnection().commit();
                        return true;
                    } catch (Exception e) {
                        try {
                            mySqlManager.getConnection().rollback();
                            MyLogManager.writeLogtoFile("数据库更新", "数据回滚", e.toString());
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        MyLogManager.writeLogtoFile("数据库更新", "失败", e.toString());
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
                } else {
                    MyLogManager.writeLogtoFile("数据库连接", "失败", "异常");
                    sendConnectDBErrorMsg();
                }
                return false;
            }

            @Override
            public void onSuccess(Boolean value) {
                dismissLoading();
                if (value) {
                    Toast.makeText(mActivity, mActivity.getText(R.string.toast_commit_success), Toast.LENGTH_SHORT).show();
                    if(mListener != null){
                        mListener.onSuccess();
                    }
                } else {
                    Toast.makeText(mActivity, mActivity.getText(R.string.toast_commit_error), Toast.LENGTH_SHORT).show();
                }
            }
        };
        showLoading(R.string.toast_updating, runnable);
        ThreadManager.getInstance().getHandler().post(runnable);
    }

}
