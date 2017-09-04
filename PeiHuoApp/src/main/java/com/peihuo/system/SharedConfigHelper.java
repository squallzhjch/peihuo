package com.peihuo.system;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户登录 记录 密码用户等状态
 *
 * @author zhjch
 */
public class SharedConfigHelper {

    private Context mContext;
    private SharedPreferences mSharePre;

    private static volatile SharedConfigHelper mInstance;

    public static SharedConfigHelper getInstance() {

        if (mInstance == null) {
            synchronized (SharedConfigHelper.class) {
                if (mInstance == null) {
                    mInstance = new SharedConfigHelper();
                }
            }
        }
        return mInstance;
    }


    public void init(Context context) {
        mContext = context;

        if (context == null)
            return;

        mSharePre = context.getSharedPreferences(SystemConfig.SP_USER_FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取保存的用户名
     *
     * @return
     */
    public String getUserName() {
        if (mSharePre != null) {
            return mSharePre.getString(SystemConfig.SP_KEY_USER_NAME, "");
        }
        return "";
    }

    /**
     * 保存用户名
     *
     * @param username
     */
    public void setUserName(String username) {
        if (mSharePre != null) {
            mSharePre.edit()
                    .putString(SystemConfig.SP_KEY_USER_NAME, username)
                    .commit();
        }
    }


    /**
     * 获取保存的用户名
     *
     * @return
     */
    public String getUserAccount() {
        if (mSharePre != null) {
            return mSharePre.getString(SystemConfig.SP_KEY_USER_ACCOUNT, "");
        }
        return "";
    }

    /**
     * 保存真实用户名
     *
     * @param id
     */
    public void setUserAccount(String id) {
        if (id == null)
            id = "";
        if (mSharePre != null) {
            mSharePre.edit()
                    .putString(SystemConfig.SP_KEY_USER_ACCOUNT, id)
                    .commit();
        }
    }


    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        if (mSharePre != null) {
            return mSharePre.getString(SystemConfig.SP_KEY_USER_PASSWORD, "");
        }
        return "";
    }

    /**
     * 保存密码
     */
    public void setPassword(String password) {
        if (mSharePre != null) {
            mSharePre.edit()
                    .putString(SystemConfig.SP_KEY_USER_PASSWORD, password)
                    .commit();
        }
    }


    /**
     * 是否记住密码
     *
     * @param
     */
    public boolean getIsSavePassword() {
        if (mSharePre != null) {
            return mSharePre.getBoolean(SystemConfig.SP_KEY_REMEMBER_PASSWORD, false);
        }
        return false;
    }

    public void setIsSavePassword(boolean bAuto) {
        if (mSharePre != null) {
            mSharePre.edit()
                    .putBoolean(SystemConfig.SP_KEY_REMEMBER_PASSWORD, bAuto)
                    .commit();
        }
    }

}
