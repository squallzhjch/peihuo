package com.peihuo.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.entity.AcceptanceInfo;
import com.peihuo.entity.SortingInfo;
import com.peihuo.system.SystemConfig;
import com.peihuo.thread.ThreadManager;
import com.peihuo.thread.ThreadManager.OnDatabaseOperationRunnable;
import com.peihuo.util.MyLogManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 123 on 2017/8/31.
 * 获取检验单详情
 */

public class QueryAcceptanceInfoCallback extends BaseCallback {

    private OnLoadDataListener mListener;
    private String mCode;

    public interface OnLoadDataListener {
        void onSuccess(ArrayList<AcceptanceInfo> list);

        void onError();
    }

    public QueryAcceptanceInfoCallback(Context context, String code, OnLoadDataListener listener) {
        super(context, code);
        mListener = listener;
        mCode = code;
    }

    public void loadData(String code) {
        mCode = code;
        loadData();
    }

    @Override
    protected void initData(Object... objects) {
        if (objects != null && objects.length > 0) {
            mCode = String.valueOf(objects[0]);
        }
    }

    @Override
    protected void loadData() {
        Call<ResponseBody> call = NetManager.getInstance().getNetService().queryAcceptanceInfo(mCode);
        showLoading(call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissLoading();
                try {
                    String res = response.body().string();
                    Log.e("jingo", "返回json=" + res);
                    MyLogManager.writeLogtoFile("数据库查询", "获取验收单详情", res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.has(SystemConfig.JSON_KEY_CODE)) {
                        String code = jsonObject.getString(SystemConfig.JSON_KEY_CODE);
                        if (TextUtils.equals(code, SystemConfig.JSON_VALUE_CODE_SUCCESS)) {
                            ArrayList<AcceptanceInfo> list = null;
                            if (jsonObject.has("data")) {
                                JSONArray array = jsonObject.getJSONArray("data");
                                if (array != null && array.length() > 0) {
                                    list = new ArrayList<>();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject userObject = array.getJSONObject(i);
                                        if (userObject != null) {
                                            AcceptanceInfo info = new AcceptanceInfo();
                                            if (userObject.has("usecount")) {
                                                info.setUseCount((int) userObject.getLong("usecount"));
                                            }
                                            if (userObject.has("handlingordercode")) {
                                                info.setHandlingOrderCode(userObject.getString("handlingordercode"));
                                            }
                                            if (userObject.has("is_suit")) {
                                                info.setIs_suit(userObject.getString("is_suit"));
                                            }
                                            if (userObject.has("productcode")) {
                                                info.setProductCode(userObject.getString("productcode"));
                                            }
                                            if (userObject.has("proname")) {
                                                info.setProName(userObject.getString("proname"));
                                            }
                                            if (userObject.has("prounite")) {
                                                info.setProUnite(userObject.getString("prounite"));
                                            }
                                            if(userObject.has("pname")){
                                                info.setGroupName(userObject.getString("pname"));
                                            }
                                            if(userObject.has("prostandered")){
                                                info.setProstandard(userObject.getString("prostandered"));
                                            }
                                            list.add(info);
                                        }
                                    }


                                    if (mListener != null) {
                                        mListener.onSuccess(list);
                                    }
                                    return;
                                }
                            }
                            Toast.makeText(mActivity, mActivity.getString(R.string.toast_json_error), Toast.LENGTH_SHORT).show();
                            if (mListener != null) {
                                mListener.onError();
                            }
                        } else {
                            if (jsonObject.has(SystemConfig.JSON_KEY_MESSAGE)) {
                                String message = jsonObject.getString(SystemConfig.JSON_KEY_MESSAGE);
                                if (!TextUtils.isEmpty(message)) {
                                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.toast_load_data_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (mListener != null) {
                                mListener.onError();
                            }
                        }
                    }
                } catch (Exception e) {
                    if (mListener != null) {
                        mListener.onError();
                    }
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mActivity, mActivity.getText(R.string.toast_net_connect), Toast.LENGTH_SHORT).show();
                dismissLoading();
                if (mListener != null) {
                    mListener.onError();
                }
            }
        });
    }
}
