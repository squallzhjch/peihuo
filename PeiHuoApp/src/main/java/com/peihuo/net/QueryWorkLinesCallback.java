package com.peihuo.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.peihuo.R;
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

public class QueryWorkLinesCallback extends BaseCallback {

    private OnLoadDataListener mListener;

    private String mRepositoryId;

    public interface OnLoadDataListener {
        void onSuccess(ArrayList<WorkLine> list);

        void onError();
    }

    public QueryWorkLinesCallback(Context context, String repositoryId, OnLoadDataListener listener) {
        super(context, repositoryId);
        mListener = listener;
        mRepositoryId = repositoryId;

    }

    @Override
    protected void initData(Object... objects) {
        if (objects != null && objects.length > 0) {
            mRepositoryId = String.valueOf(objects[0]);
        }
    }

    @Override
    protected void loadData() {
        Call<ResponseBody> call = NetManager.getInstance().getNetService().queryWorkLines(mRepositoryId);
        showLoading(call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissLoading();
                try {
                    String res = response.body().string();
                    Log.e("jingo", "返回json=" + res);
                    MyLogManager.writeLogtoFile("返回结果", "成产线", res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.has(SystemConfig.JSON_KEY_CODE)) {
                        String code = jsonObject.getString(SystemConfig.JSON_KEY_CODE);
                        if (TextUtils.equals(code, SystemConfig.JSON_VALUE_CODE_SUCCESS)) {
                            ArrayList<WorkLine> list = null;
                            if (jsonObject.has("data")) {
                                JSONArray array = jsonObject.getJSONArray("data");
                                if (array != null && array.length() > 0) {
                                    list = new ArrayList<>();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        if (object != null) {
                                            WorkLine line = new WorkLine();
                                            if (object.has("id"))
                                                line.setId(object.getString("id"));
                                            if (object.has("pipeline"))
                                                line.setPipeline(object.getString("pipeline"));
                                            if(object.has("holenum"))
                                                line.setHolenum(object.getInt("holenum"));
                                            list.add(line);
                                        }
                                    }
                                    if(mListener != null){
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
