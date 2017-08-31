package com.peihuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.db.MySqlManager;
import com.peihuo.entity.UserInfo;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.db.LoginCallback;

/**
 * Created by hb on 2017/8/25.
 */

public class LoginActivity extends Activity {

    private Button mLoginButton;
    private EditText mUserName;
    private EditText mPassword;
    private CheckBox mSaveCheck;

    private boolean isOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_login);
            MySqlManager.getInstance().init(this);

            mUserName = (EditText) findViewById(R.id.login_user_name);
            mPassword = (EditText) findViewById(R.id.login_password);

            mLoginButton = (Button) findViewById(R.id.login_button);
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mUserName.getText() == null || mUserName.getText().toString().trim().length() == 0) {
                        Toast.makeText(LoginActivity.this, getText(R.string.input_user_num), Toast.LENGTH_SHORT).show();
                    } else if (mPassword.getText() == null || mPassword.getText().toString().trim().length() == 0) {
                        Toast.makeText(LoginActivity.this, getText(R.string.input_password), Toast.LENGTH_SHORT).show();
                    } else {
                        mLoginButton.setEnabled(false);
                        MySqlManager.getInstance().login(mUserName.getText().toString().trim(),
                                mPassword.getText().toString().trim(),
                                new LoginCallback() {
                                    @Override
                                    public void onSuccess(UserInfo value) {
                                        mLoginButton.setEnabled(true);
                                        if(value == null) {
                                            Toast.makeText(LoginActivity.this, getText(R.string.get_userinfo_error), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        SharedConfigHelper.getInstance().setPassword(mPassword.getText().toString().trim());
                                        SharedConfigHelper.getInstance().setUserId(value.getUserId());
                                        SharedConfigHelper.getInstance().setUserName(value.getUserName());
                                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onError() {
                                        mLoginButton.setEnabled(true);
                                        Toast.makeText(LoginActivity.this, getText(R.string.login_error), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });

            mSaveCheck = (CheckBox) findViewById(R.id.login_save_password);

            mUserName.setText(SharedConfigHelper.getInstance().getUserId());
            if(SharedConfigHelper.getInstance().getIsSavePassword()){
                mSaveCheck.setChecked(true);
                mPassword.setText(SharedConfigHelper.getInstance().getPassword());
            }

            mSaveCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    SharedConfigHelper.getInstance().setIsSavePassword(b);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(!isOut){
            Toast.makeText(this,getText(R.string.out_app), Toast.LENGTH_SHORT).show();
            isOut = true;
        }else {
            System.exit(0);
        }
    }
}
