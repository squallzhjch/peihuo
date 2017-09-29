//package com.peihuo.db;
//
//import android.content.Context;
//
//import com.peihuo.entity.AcceptanceForm;
//import com.peihuo.system.SystemConfig;
//import com.peihuo.thread.ThreadManager;
//import com.peihuo.thread.ThreadManager.OnDatabaseOperationRunnable;
//import com.peihuo.util.MyLogManager;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by 123 on 2017/8/31.
// */
//
//public class QueryAcceptanceListCallback extends BaseCallback{
//
//    private OnLoadDataListener mListener;
//    private int mCount = 10;
//    private int mPage = 0;
//    private String mLineNo;
//    public void loadData(int count, int page) {
//        mCount = count;
//        mPage = page;
//        loadData();
//    }
//
//    public interface OnLoadDataListener{
//        void onSuccess(ArrayList<AcceptanceForm> list);
//        void onError();
//    }
//
//    public QueryAcceptanceListCallback(Context context,String lineNo, int count, int page, OnLoadDataListener listener){
//        super(context);
//        mListener = listener;
//        mCount = count;
//        mPage = page;
//        mLineNo = lineNo;
//    }
//
//    @Override
//    protected void loadData() {
//
//        OnDatabaseOperationRunnable runnable = new OnDatabaseOperationRunnable<ArrayList<AcceptanceForm>>() {
//            @Override
//            public ArrayList<AcceptanceForm> doInBackground() {
//                ArrayList<AcceptanceForm> list = null;
//                if (mySqlManager.openDB()) {
//                    Statement statement = null;
//                    ResultSet result = null;
//
//                    String sql = "SELECT " +
//                            "t_acceptanceform.transferpath," +
//                            "t_acceptanceform.starttime," +
//                            "t_acceptanceform.suituniteproductcount," +
//                            "t_acceptanceform.acceptanceformcode," +
//                            "t_orders.customerId," +
//                            "t_acceptanceform.batchcount," +
//                            "t_acceptanceform.acceptancestate, " +
//                            "t_acceptanceform.belongorderid, " +
//                            "t_acceptanceform.pitposition" +
//                            " FROM " +
//                            " t_acceptanceform " +
//                            "LEFT JOIN t_orders ON t_acceptanceform.belongorderid = t_orders.ordersNo " +
//                            " where t_acceptanceform.acceptancestate != '0' and assemblelineno = '" + mLineNo +"' " +
//                            " limit "+ mPage * mCount + ", " + mCount + ";";
//                    MyLogManager.writeLogtoFile("数据库查询", "获取验收单列表", sql);
//                    try {
//                        statement = mySqlManager.getConnection().createStatement();
//                        statement.setQueryTimeout(SystemConfig.DB_CONNECT_TIME);
//                        result = statement.executeQuery(sql);
//                        if (result != null) {
//                            list = new ArrayList<>();
//                            int codeIndex = result.findColumn("acceptanceformcode");
//                            int customerIdIndex = result.findColumn("customerId");
//                            int startTimeIndex = result.findColumn("starttime");//开单日期时间
//                            int acceptanceStateIndex = result.findColumn("acceptancestate");//验收状态
//                            int suitUniteProductCountIndex = result.findColumn("suituniteproductcount");//合计验收数量
//                            int batchCountIndex = result.findColumn("batchcount");//批次
//                            int transferpathIndex = result.findColumn("transferpath");
//                            int belongorderidIndex = result.findColumn("belongorderid");
//                            int pitpositionIndex = result.findColumn("pitposition");
//                            while (result.next()){
//                                AcceptanceForm order = new AcceptanceForm();
//                                order.setCode(result.getString(codeIndex));
//                                order.setStartTime(result.getString(startTimeIndex));
//                                order.setAcceptanceState(result.getString(acceptanceStateIndex));
//                                order.setSuitUniteProductCount(result.getInt(suitUniteProductCountIndex));
//                                order.setBatchCount(result.getString(batchCountIndex));
//                                order.setTransferPath(result.getString(transferpathIndex));
//                                order.setCustomerId(result.getString(customerIdIndex));
//                                order.setBelongorderid(result.getString(belongorderidIndex));
//                                order.setPitposition(result.getString(pitpositionIndex));
//                                list.add(order);
//                            }
//                        }
//                    } catch (SQLException e) {
//                        MyLogManager.writeLogtoFile("数据库查询", "失败", e.toString());
//                        e.printStackTrace();
//                    } finally {
//                        try {
//                            if (result != null) {
//                                result.close();
//                                result = null;
//                            }
//                            if (statement != null) {
//                                statement.close();
//                                statement = null;
//                            }
//                        } catch (SQLException sqle) {
//
//                        }
//                        mySqlManager.closeDB();
//                    }
//                }else{
//                    MyLogManager.writeLogtoFile("数据库连接", "失败", "获取验收单列表");
//                    sendConnectDBErrorMsg();
//                }
//                return list;
//            }
//
//            @Override
//            public void onSuccess(ArrayList<AcceptanceForm> value) {
//                dismissLoading();
//                if(mListener != null) {
//                    if (value == null){
//                        mListener.onError();
//                    }else{
//                        mListener.onSuccess(value);
//                    }
//
//                }
//            }
//        };
//        showLoading(runnable);
//        ThreadManager.getInstance().getHandler().post(runnable);
//    }
//
//}


