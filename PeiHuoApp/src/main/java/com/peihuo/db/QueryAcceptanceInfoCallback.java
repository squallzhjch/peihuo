package com.peihuo.db;

import android.content.Context;

import com.peihuo.entity.AcceptanceInfo;
import com.peihuo.thread.ThreadManager;
import com.peihuo.thread.ThreadManager.OnDatabaseOperationRunnable;
import com.peihuo.util.MyLogManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/31.
 * 获取检验单详情
 */

public class QueryAcceptanceInfoCallback extends BaseCallback{

    private OnLoadDataListener mListener;
    private String mCode;
    public interface OnLoadDataListener{
        void onSuccess(ArrayList<AcceptanceInfo> list);
        void onError();
    }

    public QueryAcceptanceInfoCallback(Context context, String code, OnLoadDataListener listener){
        super(context);
        mListener = listener;
        mCode = code;
    }

    public void loadData(String code){
        mCode = code;
        loadData();
    }

    @Override
    protected void loadData() {
        OnDatabaseOperationRunnable<ArrayList<AcceptanceInfo>> runnable =  new OnDatabaseOperationRunnable<ArrayList<AcceptanceInfo>>() {
            @Override
            public ArrayList<AcceptanceInfo> doInBackground() {
                ArrayList<AcceptanceInfo> list = null;
                if (mySqlManager.openDB()) {
                    Statement statement = null;
                    ResultSet result = null;

                    String sql = "SELECT " +
                            "t_acceptanceform_list.proname," +
                            "t_acceptanceform_list.productcode," +
                            "t_acceptanceform_list.usecount," +
                            "t_acceptanceform_list.prounite," +
                            "t_acceptanceform_list.is_suit," +
                            "t_acceptanceform_list.handlingordercode" +
                            " FROM " +
                            "t_acceptanceform_list where acceptancecode = '"+ mCode +"' order by is_suit;";
                    MyLogManager.writeLogtoFile("数据库查询", "获取验收单详情", sql);
                    try {
                        statement = mySqlManager.getConnection().createStatement();
                        statement.setQueryTimeout(20);
                        result = statement.executeQuery(sql);
                        if (result != null) {
                            list = new ArrayList<AcceptanceInfo>();
                            int pronameIndex = result.findColumn("proname");//商品名称
                            int productcodeIndex = result.findColumn("productcode");//商品编码
                            int ordercountIndex = result.findColumn("usecount");//使用数据亮
                            int is_suitIndex = result.findColumn("is_suit");//是否套装
                            int prouniteIndex = result.findColumn("prounite");   //单位
                            int handlingordercodeIndex = result.findColumn("handlingordercode");
                            while (result.next()){
                                AcceptanceInfo info = new AcceptanceInfo();
                                info.setProName(result.getString(pronameIndex));
                                info.setProductCode(result.getString(productcodeIndex));
                                info.setUseCount(result.getInt(ordercountIndex));
                                info.setIs_suit(result.getString(is_suitIndex));
                                info.setProUnite(result.getString(prouniteIndex));
                                info.setHandlingOrderCode(result.getString(handlingordercodeIndex));
                                list.add(info);
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
                            MyLogManager.writeLogtoFile("数据库连接", "失败", "获取验收单详情");
                        }
                        mySqlManager.closeDB();
                    }
                }else{
                    sendConnectDBErrorMsg();
                }
                return list;
            }

            @Override
            public void onSuccess(ArrayList<AcceptanceInfo> list) {
                dismissLoading();
                if(mListener != null) {
                    if (list == null){
                        mListener.onError();
                    }else{
                        mListener.onSuccess(list);
                    }

                }
            }
        };
        showLoading(runnable);
        ThreadManager.getInstance().getHandler().post(runnable);
    }

}
