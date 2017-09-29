package com.peihuo.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.system.SystemConfig;
import com.peihuo.thread.ThreadManager;
import com.peihuo.util.MyLogManager;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hb on 2017/9/5.
 */

public class UpdateAcceptanceErrorCallback extends BaseCallback {

    private String mCode;
    private String mUserId;
    private OnUpdateDataListener mListener;

    public interface OnUpdateDataListener {
        void onSuccess();
    }


    /**
     * @param activity //@param userId    用户ID
     *                 //@param orderId   订单ID
     *                 //     * @param handcodes 加工好
     */
    public UpdateAcceptanceErrorCallback(Context activity, String code, String userId, OnUpdateDataListener listener) {
        super(activity, code, userId);
        mCode = code;
        mUserId = userId;
        mListener = listener;
    }

    @Override
    protected void initData(Object... objects) {
        if (objects != null && objects.length > 1) {
            mCode = String.valueOf(objects[0]);
            mUserId = String.valueOf(objects[1]);
        }
    }

    @Override
    protected void loadData() {
        Call<ResponseBody> call = NetManager.getInstance().getNetService().updateAcceptanceError(mCode, mUserId);
        showLoading(call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissLoading();
                try {
                    String res = response.body().string();
                    Log.e("jingo", "返回json=" + res);
                    MyLogManager.writeLogtoFile("数据库查询", "验收异常", res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.has(SystemConfig.JSON_KEY_CODE)) {
                        String code = jsonObject.getString(SystemConfig.JSON_KEY_CODE);
                        if (TextUtils.equals(code, SystemConfig.JSON_VALUE_CODE_SUCCESS)) {
                            if (mListener != null) {
                                mListener.onSuccess();
                            }
                            return;
                        }
//                        Toast.makeText(mActivity, mActivity.getString(R.string.toast_json_error), Toast.LENGTH_SHORT).show();
//                            if(mListener != null){
//                                mListener.onError();
//                            }
                    }

                    if (jsonObject.has(SystemConfig.JSON_KEY_MESSAGE)) {
                        String message = jsonObject.getString(SystemConfig.JSON_KEY_MESSAGE);
                        if (!TextUtils.isEmpty(message)) {
                            Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, mActivity.getString(R.string.toast_load_data_error), Toast.LENGTH_SHORT).show();
                        }
                    }
//                            if(mListener != null){
//                                mListener.onError();
//                            }

                } catch (Exception e) {
//                    if(mListener != null){
//                        mListener.onError();
//                    }
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mActivity, mActivity.getText(R.string.toast_net_connect), Toast.LENGTH_SHORT).show();
                dismissLoading();
//                if(mListener != null){
//                    mListener.onError();
//                }
            }
        });
    }

}
