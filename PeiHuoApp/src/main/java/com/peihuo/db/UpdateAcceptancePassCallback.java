package com.peihuo.db;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.system.SystemConfig;
import com.peihuo.thread.ThreadManager;
import com.peihuo.util.MyLogManager;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;

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

public class UpdateAcceptancePassCallback extends BaseCallback {

    private String mCode;

    private OnUpdateDataListener mListener;

    public interface OnUpdateDataListener{
        void onSuccess();
    }

    /**
     * @param activity //@param userId    用户ID
     *                 //@param orderId   订单ID
     *                 //     * @param handcodes 加工好
     */

    public UpdateAcceptancePassCallback(Context activity, String code, OnUpdateDataListener listener) {
        super(activity);
        mCode = code;
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
                        String sql = "UPDATE t_acceptanceform set acceptancestate='2' " +
                                "where acceptanceformcode = '" + mCode + "'";

                        statement = mySqlManager.getConnection().createStatement();
                        statement.setQueryTimeout(SystemConfig.DB_CONNECT_TIME);
                        MyLogManager.writeLogtoFile("数据库更新", "更新分拣详情表", sql);
                        int count = statement.executeUpdate(sql);
                        if (count > 0) {
                            return true;
                        }
//                        String in = "";
//                        if (mHandCodes != null) {
//                            for (String code : mHandCodes) {
//                                in += code;
//                                in += ",";
//                            }
//                            if (in.length() > 0) {
//                                in = in.substring(0, in.length() - 1);
//                            }
//                        }
//                        sql = "UPDATE t_acceptanceform_list set responsiblehuman='"+mUserId+"', is_complete='是' " +
//                                "where bookorderno = '" + mOrderId + "' and handlingordercode in(" + in + ")";
//
//                        MyLogManager.writeLogtoFile("数据库更新", "更新验收单状态", sql);
//                        count = statement.executeUpdate(sql);
//                        mySqlManager.getConnection().commit();
//                        sql = "SELECT count(*) from t_acceptanceform_list where bookorderno ='" + mOrderId + "' and is_complete != '是';";
//                        MyLogManager.writeLogtoFile("数据库更新", "检查是不是分拣单都完成", sql);
//                        result = statement.executeQuery(sql);
//                        if (result != null && result.next()) {
//                            count = result.getInt(1);
//                            if (count > 0) {
//
//                            }else{
//                                sql = "UPDATE t_acceptanceform set acceptancestate='1' " +
//                                        "where belongorderid = '" + mOrderId + "'";
//                                statement.executeUpdate(sql);
//                                mySqlManager.getConnection().commit();
//
//                            }
//                        }
                        return false;
                    } catch (Exception e) {
//                        try {
//                            mySqlManager.getConnection().rollback();
//                            MyLogManager.writeLogtoFile("数据库更新", "数据回滚", e.toString());
//                        } catch (SQLException e1) {
//                            e1.printStackTrace();
//                        }
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
                    MyLogManager.writeLogtoFile("数据库连接", "失败", "更新分拣单");
                    sendConnectDBErrorMsg();
                }
                return false;
            }

            @Override
            public void onSuccess(Boolean value) {
                if (value) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://47.95.12.49:8084/echuxianshengshop/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    BlogService service = retrofit.create(BlogService.class);
                    Call<ResponseBody> call = service.getBlog(mCode);
                    // 用法和OkHttp的call如出一辙,
                    // 不同的是如果是Android系统回调方法执行在主线程
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dismissLoading();
                            try {
                                Toast.makeText(mActivity, mActivity.getText(R.string.toast_commit_success), Toast.LENGTH_SHORT).show();
                                Log.e("jingo", response.body().string());
                                if(mListener != null){
                                    mListener.onSuccess();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            dismissLoading();
                            Toast.makeText(mActivity, mActivity.getText(R.string.toast_commit_error), Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                }else{


//                    Toast.makeText(mActivity, mActivity.getText(R.string.toast_commit_success), Toast.LENGTH_SHORT).show();
//                } else {
                    Toast.makeText(mActivity, mActivity.getText(R.string.toast_commit_error), Toast.LENGTH_SHORT).show();

                }


            }
        };
        showLoading(R.string.toast_updating, runnable);
        ThreadManager.getInstance().getHandler().post(runnable);
    }

    public interface BlogService {
        @GET("Outputorderb/createOutPutOrderB")
        Call<ResponseBody> getBlog(@Query("acceptanceFormID") String parameter);
    }

}
