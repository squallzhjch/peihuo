package com.peihuo.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.entity.AcceptanceForm;
import com.peihuo.entity.WorkLine;
import com.peihuo.system.SystemConfig;
import com.peihuo.util.MyLogManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 123 on 2017/8/31.
 */

public class QueryAcceptanceListCallback extends BaseCallback {

    private OnLoadDataListener mListener;
    private int mCount = 10;
    private int mPage = 0;
    private String mLineNo;
    private int mState;
    public void loadData(int count, int page) {
        mCount = count;
        mPage = page;
        loadData();
    }

    public interface OnLoadDataListener {
        void onSuccess(ArrayList<AcceptanceForm> list);
        void onError();
    }

    public QueryAcceptanceListCallback(Context context, String lineNo, int count, int page, int state, OnLoadDataListener listener) {
        super(context, count, page, lineNo, state);
        mListener = listener;
        mCount = count;
        mPage = page;
        mState = state;
        mLineNo = lineNo;
    }

    @Override
    protected void initData(Object... objects) {
        if (objects != null && objects.length > 3) {
            mCount = (int) objects[0];
            mPage = (int) objects[1];
            mLineNo = String.valueOf(objects[2]);
            mState = (int) objects[3];
        }
    }

    @Override
    protected void loadData() {
        Call<ResponseBody> call = NetManager.getInstance().getNetService().queryAcceptanceList(mLineNo, mPage, mCount, mState);
        showLoading(call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissLoading();
                try {
                    String res = response.body().string();
                    Log.e("jingo", "返回json=" + res);
                    MyLogManager.writeLogtoFile("返回结果", "验收单", res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.has(SystemConfig.JSON_KEY_CODE)) {
                        String code = jsonObject.getString(SystemConfig.JSON_KEY_CODE);
                        if (TextUtils.equals(code, SystemConfig.JSON_VALUE_CODE_SUCCESS)) {
                            ArrayList<AcceptanceForm> list = null;
                            if (jsonObject.has("data")) {
                                JSONObject listObject = jsonObject.getJSONObject("data");
                                if (listObject.has("list")) {
                                    JSONArray array = listObject.getJSONArray("list");
                                    if (array != null && array.length() > 0) {
                                        list = new ArrayList<>();
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object = array.getJSONObject(i);
                                            if (object != null) {
                                                AcceptanceForm line = new AcceptanceForm();
                                                if (object.has("pitposition"))
                                                    line.setPitposition(object.getString("pitposition"));
                                                if (object.has("acceptancestate"))
                                                    line.setAcceptanceState(object.getString("acceptancestate"));
                                                if (object.has("belongorderid"))
                                                    line.setBelongorderid(object.getString("belongorderid"));
                                                if (object.has("customerId"))
                                                    line.setCustomerId(object.getString("customerId"));
                                                if (object.has("starttime"))
                                                    line.setStartTime(object.getString("starttime"));
                                                if (object.has("transferpath"))
                                                    line.setTransferPath(object.getString("transferpath"));
                                                if (object.has("batchcount"))
                                                    line.setBatchCount(object.getString("batchcount"));
                                                if (object.has("acceptanceformcode"))
                                                    line.setCode(object.getString("acceptanceformcode"));
                                                if (object.has("suituniteproductcount"))
                                                    line.setSuitUniteProductCount((int) object.getLong("suituniteproductcount"));
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
