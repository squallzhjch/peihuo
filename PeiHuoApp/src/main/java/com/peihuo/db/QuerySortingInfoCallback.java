package com.peihuo.db;

import android.content.Context;

import com.peihuo.entity.SortingForm;
import com.peihuo.entity.SortingInfo;
import com.peihuo.thread.ThreadManager;
import com.peihuo.util.MyLogManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/31.
 * 获取分拣单详情
 */

public class QuerySortingInfoCallback extends BaseCallback{

    private OnLoadDataListener mListener;
    private String mCode;
    public interface OnLoadDataListener{
        void onSuccess(ArrayList<SortingInfo> list);
        void onError();
    }

    public QuerySortingInfoCallback(Context context, String code, OnLoadDataListener listener){
        super(context);
        mListener = listener;
        mCode = code;
    }

    @Override
    protected void loadData() {
        showLoading();
        ThreadManager.getInstance().getHandler().post(new ThreadManager.OnDatabaseOperationRunnable<ArrayList<SortingInfo>>() {
            @Override
            public ArrayList<SortingInfo> doInBackground() {
                ArrayList<SortingInfo> list = null;
                if (mySqlManager.openDB()) {
                    Statement statement = null;
                    ResultSet result = null;

                    String sql = "SELECT * FROM t_acceptanceform_list where acceptancecode = "+ mCode +" order by is_suit;";
                    MyLogManager.writeLogtoFile("数据库查询", "登录", sql);
                    try {
                        statement = mySqlManager.getConnection().createStatement();
                        result = statement.executeQuery(sql);
                        if (result != null) {
                            list = new ArrayList<SortingInfo>();
                            int pronameIndex = result.findColumn("proname");//商品名称
                            int productcodeIndex = result.findColumn("productcode");//商品编码
                            int ordercountIndex = result.findColumn("ordercount");//订单数量
                            int is_suitIndex = result.findColumn("is_suit");//是否套装
                            int prouniteIndex = result.findColumn("prounite");   //单位
                            int handlingordercodeIndex = result.findColumn("handlingordercode");
                            if (result.next()){
                                SortingInfo info = new SortingInfo();
                                info.setProName(result.getString(pronameIndex));
                                info.setProductCode(result.getString(productcodeIndex));
                                info.setOrderCount(result.getInt(ordercountIndex));
                                info.setIs_suit(result.getString(is_suitIndex));
                                info.setProUnite(result.getString(prouniteIndex));
                                info.setHandlingOrderCode(result.getString(handlingordercodeIndex));
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
            public void onSuccess(ArrayList<SortingInfo> list) {
                dismissLoading();
                if(mListener != null) {
                    if (list == null){
                        mListener.onError();
                    }else{
                        mListener.onSuccess(list);
                    }

                }
            }
        });
    }

}
