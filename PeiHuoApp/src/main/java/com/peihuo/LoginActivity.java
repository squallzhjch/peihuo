package com.peihuo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by hb on 2017/8/25.
 */

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            setContentView(R.layout.activity_login);
        }
    }
}
