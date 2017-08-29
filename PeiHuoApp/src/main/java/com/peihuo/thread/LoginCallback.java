package com.peihuo.thread;

import com.peihuo.entity.UserInfo;

/**
 * Created by 123 on 2017/8/28.
 */

public interface LoginCallback  {
    void onSuccess(UserInfo value);
    void onError();
}
