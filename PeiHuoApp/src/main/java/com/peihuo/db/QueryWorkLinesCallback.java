package com.peihuo.db;

import android.content.Context;

import com.peihuo.entity.AcceptanceForm;
import com.peihuo.entity.WorkLine;
import com.peihuo.system.SystemConfig;
import com.peihuo.thread.ThreadManager;
import com.peihuo.thread.ThreadManager.OnDatabaseOperationRunnable;
import com.peihuo.util.MyLogManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/31.
 */

public class QueryWorkLinesCallback extends BaseCallback {

    private OnLoadDataListener mListener;

    //    public void loadData() {
//    }
    private String mRepositoryId;

    public interface OnLoadDataListener {
        void onSuccess(ArrayList<WorkLine> list);

        void onError();
    }

    public QueryWorkLinesCallback(Context context, String repositoryId, OnLoadDataListener listener) {
        super(context);
        mListener = listener;
        mRepositoryId = repositoryId;
    }

    @Override
    protected void loadData() {

        OnDatabaseOperationRunnable runnable = new OnDatabaseOperationRunnable<ArrayList<WorkLine>>() {
            @Override
            public ArrayList<WorkLine> doInBackground() {
                ArrayList<WorkLine> list = null;
                if (mySqlManager.openDB()) {
                    Statement statement = null;
                    ResultSet result = null;

                    String sql = "SELECT *  FROM  t_repositoryPipeline where repCode = '"+mRepositoryId+"'";
                    MyLogManager.writeLogtoFile("数据库查询", "获取生产线", sql);
                    try {
                        statement = mySqlManager.getConnection().createStatement();
                        statement.setQueryTimeout(SystemConfig.DB_CONNECT_TIME);
                        result = statement.executeQuery(sql);
                        if (result != null) {
                            list = new ArrayList<>();
                            int idIndex = result.findColumn("id");
                            int pipelineIndex = result.findColumn("pipeline");
                            while (result.next()) {
                                WorkLine order = new WorkLine();
                                order.setId(result.getString(idIndex));
                                order.setPipeline(result.getString(pipelineIndex));
                                list.add(order);
                            }
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
                } else {
                    MyLogManager.writeLogtoFile("数据库连接", "失败", "获取生产线");
                    sendConnectDBErrorMsg();
                }
                return list;
            }

            @Override
            public void onSuccess(ArrayList<WorkLine> value) {
                dismissLoading();
                if (mListener != null) {
                    if (value == null) {
                        mListener.onError();
                    } else {
                        mListener.onSuccess(value);
                    }

                }
            }
        };
        showLoading(runnable);
        ThreadManager.getInstance().getHandler().post(runnable);
    }

}
