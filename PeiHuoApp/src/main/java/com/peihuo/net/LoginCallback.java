package com.peihuo.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.entity.UserInfo;
import com.peihuo.system.SystemConfig;
import com.peihuo.util.MD5Util;
import com.peihuo.util.MyLogManager;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 123 on 2017/8/28.
 */

public class LoginCallback extends BaseCallback{

    private String mAccount;
    private String mPassword;

    private OnLoginCallbackListener mListener;

    public interface OnLoginCallbackListener{
        void onSuccess(UserInfo value);
        void onError();
    }

    public LoginCallback(Context context, String account, final String password, OnLoginCallbackListener listener){
        super(context, account, password);
        mListener = listener;
        mAccount = account;
        mPassword = password;
    }


    @Override
    protected void initData(Object... objects) {
        if(objects != null && objects.length > 1) {
            mAccount = String.valueOf(objects[0]);
            mPassword = String.valueOf(objects[1]);
        }
    }

    @Override
    protected void loadData() {

        if(mAccount == null || mPassword == null){
            return;
        }
        Call<ResponseBody> call = NetManager.getInstance().getNetService().login(mAccount, MD5Util.EncoderByMd5(mPassword));
        showLoading(call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissLoading();
                try {
                    String res = response.body().string();
                    Log.e("jingo","返回json=" + res);
                    MyLogManager.writeLogtoFile("返回结果", "登录", res);
                    JSONObject jsonObject = new JSONObject(res);
                    if(jsonObject.has(SystemConfig.JSON_KEY_CODE)){
                        String code = jsonObject.getString(SystemConfig.JSON_KEY_CODE);
                        if(TextUtils.equals(code, SystemConfig.JSON_VALUE_CODE_SUCCESS)){
                            UserInfo info = new UserInfo();
                            if(jsonObject.has("data")){
                                JSONArray array = jsonObject.getJSONArray("data");
                                if(array != null && array.length() >0){
                                    JSONObject userObject = array.getJSONObject(0);
                                    if(userObject != null){
                                        if(userObject.has("uid")){
                                            info.setUserId(userObject.getString("uid"));
                                        }
                                        if(userObject.has("uname")){
                                            info.setUserName(userObject.getString("uname"));
                                        }
                                        if(userObject.has("urole")){
                                            info.setUrole(userObject.getString("urole"));
                                        }
                                        if(userObject.has("repositoryid")){
                                            info.setRepositoryid(userObject.getString("repositoryid"));
                                        }
                                        info.setAccount(mAccount);
                                    }
                                    if(mListener != null){
                                        mListener.onSuccess(info);
                                    }
                                    return;
                                }
                            }
                            Toast.makeText(mActivity, mActivity.getString(R.string.toast_json_error), Toast.LENGTH_SHORT).show();
                            if(mListener != null){
                                mListener.onError();
                            }
                        }else{
                            if(jsonObject.has(SystemConfig.JSON_KEY_MESSAGE)){
                                String message = jsonObject.getString(SystemConfig.JSON_KEY_MESSAGE);
                                if(!TextUtils.isEmpty(message)){
                                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(mActivity, mActivity.getString(R.string.toast_login_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                            if(mListener != null){
                                mListener.onError();
                            }
                        }
                    }
                } catch (Exception e) {
                    if(mListener != null){
                        mListener.onError();
                    }
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mActivity, mActivity.getText(R.string.toast_net_connect), Toast.LENGTH_SHORT).show();
                dismissLoading();
                if(mListener != null){
                    mListener.onError();
                }
            }
        });
    }

}
