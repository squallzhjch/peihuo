package com.peihuo.activity;

import android.app.Activity;
import android.os.Bundle;

import com.peihuo.R;
import com.peihuo.db.MySqlManager;

/**
 * Created by 123 on 2017/8/28.
 */

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_login);
        }
    }
}
