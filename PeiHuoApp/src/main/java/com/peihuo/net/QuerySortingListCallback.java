package com.peihuo.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.entity.AcceptanceForm;
import com.peihuo.entity.SortingForm;
import com.peihuo.entity.SortingInfo;
import com.peihuo.entity.UserInfo;
import com.peihuo.system.SystemConfig;
import com.peihuo.thread.ThreadManager;
import com.peihuo.thread.ThreadManager.OnDatabaseOperationRunnable;
import com.peihuo.util.MD5Util;
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
 * 获取分拣单列表
 */

public class QuerySortingListCallback extends BaseCallback {

    private OnLoadDataListener mListener;
    private int mCount = 10;
    private int mPage = 0;
    private String mUserId = "";
    private String mLineNo ;
    public interface OnLoadDataListener {
        void onSuccess(ArrayList<SortingForm> list);

        void onError();
    }

    public QuerySortingListCallback(Context context, String userId, String lineNo, int count, int page, OnLoadDataListener listener) {
        super(context, userId,lineNo, count, page);
        mListener = listener;
        mUserId = userId;
        mCount = count;
        mLineNo = lineNo;
        mPage = page;
    }

    public void loadData(String userId, int count, int page) {
        mCount = count;
        mPage = page;
        mUserId = userId;
        loadData();
    }

    @Override
    protected void initData(Object... objects) {
        if (objects != null && objects.length > 3) {
            mUserId = String.valueOf(objects[0]);
            mLineNo = String.valueOf(objects[1]);
            mCount = (Integer) objects[2];
            mPage = (Integer) objects[3];
        }
    }

    @Override
    protected void loadData() {
        Call<ResponseBody> call = NetManager.getInstance().getNetService().querySortingList(mUserId, mLineNo, mPage, mCount);
        showLoading(call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissLoading();
                try {
                    String res = response.body().string();
                    Log.e("jingo", "返回json=" + res);
                    MyLogManager.writeLogtoFile("返回结果", "分拣单列表", res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.has(SystemConfig.JSON_KEY_CODE)) {
                        String code = jsonObject.getString(SystemConfig.JSON_KEY_CODE);
                        if (TextUtils.equals(code, SystemConfig.JSON_VALUE_CODE_SUCCESS)) {
                            ArrayList<SortingForm> list = null;
                            if (jsonObject.has("data")) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                if (dataObject != null) {
                                    JSONArray listArray = dataObject.getJSONArray("list");
                                    if (listArray != null && listArray.length() > 0) {
                                        list = new ArrayList<>();
                                        for (int i = 0; i < listArray.length(); i++) {
                                            JSONObject object = listArray.getJSONObject(i);
                                            if (object != null) {
                                                SortingForm line = new SortingForm();
                                                if (object.has("belongorderid"))
                                                    line.setBelongorderid(object.getString("belongorderid"));
                                                if (object.has("customerId"))
                                                    line.setCustomerId(object.getString("customerId"));
                                                if (object.has("acceptanceformcode"))
                                                    line.setCode(object.getString("acceptanceformcode"));
                                                if (object.has("pitposition"))
                                                    line.setPitposition(object.getString("pitposition"));
                                                if (object.has("assemblelineno"))
                                                    line.setAssemblelineno(object.getString("assemblelineno"));
                                                if (object.has("acceptancestate"))
                                                    line.setAcceptanceState(object.getString("acceptancestate"));
                                                if (object.has("batchcount"))
                                                    line.setBatchCount(object.getString("batchcount"));
                                                list.add(line);
                                            }
                                        }
                                        if (mListener != null) {
                                            mListener.onSuccess(list);
                                        }
                                        return;
                                    }
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
                                    Toast.makeText(mActivity, mActivity.getString(R.string.toast_login_error), Toast.LENGTH_SHORT).show();
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
