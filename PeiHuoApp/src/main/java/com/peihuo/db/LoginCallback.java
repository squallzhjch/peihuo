//package com.peihuo.db;
//
//import android.content.Context;
//import android.widget.Toast;
//
//import com.peihuo.entity.UserInfo;
//import com.peihuo.system.SystemConfig;
//import com.peihuo.thread.ThreadManager;
//import com.peihuo.util.MD5Util;
//import com.peihuo.util.MyLogManager;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
///**
// * Created by 123 on 2017/8/28.
// */
//
//public class LoginCallback  extends BaseCallback{
//
//    private Context mContext;
//    private String mAccount;
//    private String mPassword;
//
//    private OnLoginCallbackListener mListener;
//
//    public interface OnLoginCallbackListener{
//        void onSuccess(UserInfo value);
//        void onError();
//    }
//
//    public LoginCallback(Context context, String account, final String password, OnLoginCallbackListener listener){
//        super(context);
//        mAccount = account;
//        mPassword = password;
//        mListener = listener;
//    }
//
//
//    @Override
//    protected void loadData() {
//
//        ThreadManager.OnDatabaseOperationRunnable runnable = new ThreadManager.OnDatabaseOperationRunnable<UserInfo>() {
//            @Override
//            public UserInfo doInBackground() {
//                UserInfo userInfo = null;
//                if (mySqlManager.openDB()) {
//                    Statement statement = null;
//                    ResultSet result = null;
//
//                    String sql = "SELECT t_user.uid," +
//                            "t_user.repositoryid, " +
//                            "t_roletable.rolename," +
//                            "t_user.uname FROM t_user LEFT JOIN t_roletable ON t_user.roleid = t_roletable.roleid " +
//                            "WHERE account = \"" + mAccount + "\" AND upassword = \"" + MD5Util.EncoderByMd5(mPassword) + "\";";
//                    MyLogManager.writeLogtoFile("数据库查询", "登录", sql);
//                    try {
//                        statement = mySqlManager.getConnection().createStatement();
//                        statement.setQueryTimeout(SystemConfig.DB_CONNECT_TIME);
//                        result = statement.executeQuery(sql);
//                        if (result != null && result.first()) {
//                            userInfo = new UserInfo();
//                            int nameIndex = result.findColumn("uname");
//                            int uIdIndex = result.findColumn("uid");
//                            int uroleIndex = result.findColumn("rolename");
//                            int repositoryidIndex = result.findColumn("repositoryid");
//                            userInfo.setUserName(result.getString(nameIndex));
//                            userInfo.setUserId(result.getString(uIdIndex));
//                            userInfo.setAccount(mAccount);
//                            userInfo.setUrole(result.getString(uroleIndex));
//                            userInfo.setRepositoryid(result.getString(repositoryidIndex));
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
//                    sendConnectDBErrorMsg();
//                }
//                return userInfo;
//            }
//
//            @Override
//            public void onSuccess(UserInfo value) {
//                dismissLoading();
//                if (mListener != null) {
//                    if (value == null) {
//                        mListener.onError();
//                    } else {
//                        mListener.onSuccess(value);
//                    }
//                }
//            }
//
//            @Override
//            public void onOperationFailed(Exception e) {
//                super.onOperationFailed(e);
//                dismissLoading();
//            }
//        };
//        showLoading(runnable);
//        ThreadManager.getInstance().getHandler().post(runnable);
//    }
//}
