package com.peihuo.db;

import android.content.Context;

import com.peihuo.entity.SortingForm;
import com.peihuo.thread.ThreadManager;
import com.peihuo.thread.ThreadManager.OnDatabaseOperationRunnable;
import com.peihuo.util.MyLogManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/31.
 * 获取分拣单列表
 */

public class QuerySortingListCallback extends BaseCallback{

    private OnLoadDataListener mListener;
    private int mCount = 10;
    private int mPage = 0;

    public interface OnLoadDataListener{
        void onSuccess(ArrayList<SortingForm> list);
        void onError();
    }

    public QuerySortingListCallback(Context context, int count, int page, OnLoadDataListener listener){
        super(context);
        mListener = listener;
        mCount = count;
        mPage = page;
    }

    public void loadData(int count, int page){
        mCount = count;
        mPage = page;
        loadData();
    }

    @Override
    protected void loadData() {

        OnDatabaseOperationRunnable runnable = new OnDatabaseOperationRunnable<ArrayList<SortingForm>>() {
            @Override
            public ArrayList<SortingForm> doInBackground() {
                ArrayList<SortingForm> list = null;
                if (mySqlManager.openDB()) {
                    Statement statement = null;
                    ResultSet result = null;
                    String sql = "SELECT " +
                            "t_acceptanceform.acceptanceformcode," +
                            "t_acceptanceform.batchcount," +
                            "t_acceptanceform.assemblelineno," +
                            "t_acceptanceform.pitposition," +
                            "t_acceptanceform.acceptancestate," +
                            "t_acceptanceform.belongorderid," +
                            "t_orders.customerId " +
                            " FROM " +
                            "t_acceptanceform LEFT JOIN t_orders ON t_acceptanceform.belongorderid = t_orders.ordersId " +
                            " where t_acceptanceform.acceptancestate = '0' " +
                            " limit "+ mPage * mCount + ", " + mCount + ";";
                    MyLogManager.writeLogtoFile("数据库查询", "登录", sql);
                    try {
                        statement = mySqlManager.getConnection().createStatement();
                        statement.setQueryTimeout(20);
                        result = statement.executeQuery(sql);
                        if (result != null) {
                            list = new ArrayList<>();
                            int codeIndex = result.findColumn("acceptanceformcode");//编号
                            int batchCountIndex = result.findColumn("batchcount");//批次
                            int assemblelinenoIndex = result.findColumn("assemblelineno");//流水
                            int pitpositionIndex = result.findColumn("pitposition");//坑位
                            int acceptancestateIndex = result.findColumn("acceptancestate");//状态
                            int customerIdIndex = result.findColumn("customerId");    //订单ID
                            int belongorderidIndex = result.findColumn("belongorderid");//订单Id
                            while (result.next()){
                                SortingForm order = new SortingForm();
                                order.setCode(result.getString(codeIndex));
                                order.setAcceptanceState(result.getString(acceptancestateIndex));
                                order.setAssemblelineno(result.getString(assemblelinenoIndex));
                                order.setPitposition(result.getString(pitpositionIndex));
                                order.setBatchCount(result.getString(batchCountIndex));
                                order.setCustomerId(result.getString(customerIdIndex));
                                order.setBelongorderid(result.getString(belongorderidIndex));
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
                }
                return list;
            }

            @Override
            public void onSuccess(ArrayList<SortingForm> value) {
                dismissLoading();
                if(mListener != null) {
                    if (value == null){
                        mListener.onError();
                    }else{
                        mListener.onSuccess(value);
                    }

                }
            }
        };
        showLoading(runnable);
        ThreadManager.getInstance().getHandler().post(runnable);
    }

}
