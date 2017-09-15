package com.peihuo.db;

import android.content.Context;

import com.peihuo.entity.SortingForm;
import com.peihuo.entity.SortingInfo;
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

    public void loadData(String code){
        mCode = code;
        loadData();
    }

    @Override
    protected void loadData() {

        OnDatabaseOperationRunnable runnable = new OnDatabaseOperationRunnable<ArrayList<SortingInfo>>() {
            @Override
            public ArrayList<SortingInfo> doInBackground() {
                ArrayList<SortingInfo> list = null;
                if (mySqlManager.openDB()) {
                    Statement statement = null;
                    ResultSet result = null;

                    String sql = "SELECT " +
                            "t_handlingorder.handlingordercode," +
                            "t_handlingorder.pname," +
                            "t_handlingorder.productcode," +
                            "t_handlingorder.usecount," +
                            "t_handlingorder.prounite," +
                            "t_handlingorder.suit_parent_code" +
                            " FROM " +
                            "t_handlingorder where ordercode = '"+ mCode +"' order by suit_parent_code;";
                    MyLogManager.writeLogtoFile("数据库查询", "获取分拣单详情", sql);
                    try {
                        statement = mySqlManager.getConnection().createStatement();
                        statement.setQueryTimeout(SystemConfig.DB_CONNECT_TIME);
                        result = statement.executeQuery(sql);
                        if (result != null) {
                            list = new ArrayList<SortingInfo>();
                            int pronameIndex = result.findColumn("pname");//商品名称
                            int productcodeIndex = result.findColumn("productcode");//商品编码
                            int usecountIndex = result.findColumn("usecount");//使用数据亮
                            int is_suitIndex = result.findColumn("suit_parent_code");//是否套装
                            int prouniteIndex = result.findColumn("prounite");   //单位
                            int handlingordercodeIndex = result.findColumn("handlingordercode");
                            while (result.next()){
                                SortingInfo info = new SortingInfo();
                                info.setProName(result.getString(pronameIndex));
                                info.setProductCode(result.getString(productcodeIndex));
                                info.setUseCount(result.getInt(usecountIndex));
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

                        }
                        mySqlManager.closeDB();
                    }
                }else {
                    MyLogManager.writeLogtoFile("数据库连接", "失败", "获取分拣单详情");
                    sendConnectDBErrorMsg();
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
        };
        showLoading(runnable);
        ThreadManager.getInstance().getHandler().post(runnable);
    }

}
