package com.peihuo.db;

import android.content.Context;

import com.peihuo.entity.SortingOrder;
import com.peihuo.entity.UserInfo;
import com.peihuo.thread.ThreadManager;
import com.peihuo.util.LogManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/8/31.
 */

public class GetSortingListCallback extends BaseCallback{

    private OnLoadDataListener mListener;
    private int mCount = 10;
    private int mPage = 0;

    public interface OnLoadDataListener{
        void onSuccess(List<SortingOrder> list);
        void onError();
    }

    public GetSortingListCallback(Context context, int count, int page, OnLoadDataListener listener){
        super(context);
        mListener = listener;
        mCount = count;
        mPage = page;
    }

    @Override
    protected void loadData() {
        showLoading();
        ThreadManager.getInstance().getHandler().post(new ThreadManager.OnDatabaseOperationRunnable<List<SortingOrder>>() {
            @Override
            public List<SortingOrder> doInBackground() {
                List<SortingOrder> list = null;
                if (mySqlManager.openDB()) {
                    Statement statement = null;
                    ResultSet result = null;

                    String sql = "SELECT * FROM t_acceptanceform ;";
                    LogManager.writeLogtoFile("数据库查询", "登录", sql);
                    try {
                        statement = mySqlManager.getConnection().createStatement();
                        result = statement.executeQuery(sql);
                        if (result != null) {
                            list = new ArrayList<>();
                            int codeIndex = result.findColumn("acceptanceformcode");
                            int idIndex = result.findColumn("belongorderid");
                            int startTimeIndex = result.findColumn("starttime");//开单日期时间
                            int endTimeIndex = result.findColumn("endtime");//结束日期时间
                            int totalProductsIndex = result.findColumn("totalproducts");//货品总数
                            int unitProductCountIndex = result.findColumn("uniteproductcount");//单品数量
                            int suitCountIndex = result.findColumn("suitcount");//套装菜数量
                            int acceptanceStateIndex = result.findColumn("acceptancestate");//验收状态
                            int suitUniteProductCountIndex = result.findColumn("suituniteproductcount");//合计验收数量
                            int acceptanceHuManIndex = result.findColumn("acceptancehuman");//验收人
                            int batchCountIndex = result.findColumn("batchcount");//批次
                            while (result.next()){
                                SortingOrder order = new SortingOrder();
                                order.setCode(result.getString(codeIndex));
                                order.setId(result.getString(idIndex));
                                order.setStartTime(result.getString(startTimeIndex));
                                order.setEndTime(result.getString(endTimeIndex));
                                order.setTotalProducts(result.getInt(totalProductsIndex));
                                order.setUnitProductCount(result.getInt(unitProductCountIndex));
                                order.setSuitCount(result.getInt(suitCountIndex));
                                order.setAcceptanceHuMan(result.getString(acceptanceHuManIndex));
                                order.setAcceptanceState(result.getString(acceptanceStateIndex));
                                order.setSuitUniteProductCount(result.getInt(suitUniteProductCountIndex));
                                order.setBatchCount(result.getString(batchCountIndex));
                                list.add(order);
                            }
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
                        mySqlManager.closeDB();
                    }
                }
                return list;
            }

            @Override
            public void onSuccess(List<SortingOrder> value) {
                dismissLoading();
                if(mListener != null) {
                    if (value == null){
                        mListener.onError();
                    }else{
                        mListener.onSuccess(value);
                    }

                }
            }
        });
    }

}
