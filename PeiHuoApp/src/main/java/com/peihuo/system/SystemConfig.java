package com.peihuo.system;

import android.os.Environment;

/**
 * Created by 123 on 2017/8/18.
 */

public class SystemConfig {
    public static final String SD_PATH = Environment.getExternalStorageDirectory().getPath() ;
    public static final String SD_ROOT_NAME = SD_PATH + "/Peihuo";
    public static final String SD_LOG_PATH = SD_ROOT_NAME + "/Log/";


    public static final String SP_USER_FILE_NAME = "sp_user_file_name";
    public static final String SP_KEY_USER_NAME = "sp_key_user_name";
    public static final String SP_KEY_USER_ACCOUNT = "sp_key_user_account";
    public static final String SP_KEY_USER_PASSWORD = "sp_key_user_password";
    public static final String SP_KEY_USER_UROLE = "sp_key_user_urole";
    public static final String SP_KEY_USER_REPOSITORY_ID = "sp_key_user_repository_id";
    public static final String SP_KEY_USER_WORKLINE_ID = "sp_key_user_workline_id";
    public static final String SP_KEY_REMEMBER_PASSWORD = "sp_key_remember_password";
    public static final String SP_KEY_USER_ID = "sp_key_user_id";

    public static final String BUNDLE_KEY_SORTING_LIST_INDEX = "bundle_key_sorting_list_index";//分拣单列表所选位置
    public static final String BUNDLE_KEY_SORTING_LIST = "bundle_key_sorting_list";//分拣单列表
    public static final String BUNDLE_KEY_BACK_LIST_TYPE = "bundle_key_back_list_type";//从详情会列表的类型
    public static final String BUNDLE_KEY_ACTIVITY_FROM_LOGIN = "bundle_key_activity_from_login";//是否来自登录页面
    public static final String BUNDLE_KEY_ACCEPTANCE_INFO = "bundle_key_acceptance_info";//验收单
    public static final String BUNDLE_KEY_SORTING_INFO = "bundle_key_sorting_info";//验收单
    public static final String BUNDLE_KEY_ACCEPTANCE_STATE = "bundle_key_acceptance_state";//验收单

    public static final String JSON_KEY_CODE = "code";
    public static final String JSON_KEY_MESSAGE = "message";
    public static final String JSON_VALUE_CODE_SUCCESS = "1111";
    public static final String JSON_VALUE_CODE_FAILED = "0000";


    public static final int ACTIVITY_RESULT_BACK = 0x11;

    public static final int DB_CONNECT_TIME = 5;

    public static final String APP_IP = "http://47.95.12.49:8084/echuxianshengshop/";

    public static final boolean DEBUG_LOG = true;
}
