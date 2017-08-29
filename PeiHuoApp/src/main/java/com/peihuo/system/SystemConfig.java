package com.peihuo.system;

import android.os.Environment;

/**
 * Created by 123 on 2017/8/18.
 */

public class SystemConfig {
    public static final String SD_PATH = Environment.getExternalStorageDirectory().toString();
    public static final String SD_ROOT_NAME = SD_PATH + "/peihuo";
    public static final String SD_LOG_PATH = SD_ROOT_NAME + "/Log";


    public static final String SP_USER_FILE_NAME = "sp_user_file_name";
    public static final String SP_KEY_USER_NAME = "sp_key_user_name";
    public static final String SP_KEY_USER_ID = "sp_key_user_id";
    public static final String SP_KEY_USER_PASSWORD = "sp_key_user_password";
    public static final String SP_KEY_REMEMBER_PASSWORD = "sp_key_remember_password";
}
