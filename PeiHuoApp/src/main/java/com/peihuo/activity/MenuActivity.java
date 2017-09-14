package com.peihuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.peihuo.R;
import com.peihuo.db.QueryWorkLinesCallback;
import com.peihuo.entity.WorkLine;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.system.SystemConfig;
import com.peihuo.ui.dialog.WorkLineDialog;

import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/28.
 */

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_menu);

            //标题
            TextView title = (TextView) findViewById(R.id.title_text);
            title.setText(getText(R.string.menu_title));

            //用户名
            TextView userName = (TextView) findViewById(R.id.title_username_text);
            userName.setText(SharedConfigHelper.getInstance().getUserName());

            //回退按钮
            ImageView back = (ImageView) findViewById(R.id.title_back_button);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            final ImageView exit = (ImageView)findViewById(R.id.exit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = View.inflate(MenuActivity.this, R.layout.exit_pop_layout, null);
                    final PopupWindow window =  new PopupWindow(view,
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    window.setContentView(view);
                    window.showAsDropDown(v,  20, 20);
                    view.findViewById(R.id.exit_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            window.dismiss();
                        }
                    });
                    view.findViewById(R.id.exit_commit).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            finish();
                            System.exit(0);
                        }
                    });
                }
            });

            //生产计划和工单
            Button planButton = (Button) findViewById(R.id.menu_plan);
            planButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MenuActivity.this, PlanFormActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });

            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                //是否是从登录界面来的
                boolean bFromLogin = bundle.getBoolean(SystemConfig.BUNDLE_KEY_ACTIVITY_FROM_LOGIN, false);
                if(bFromLogin){
                    String urole = SharedConfigHelper.getInstance().getUserUrole();
                    if(urole.equals("验收员")){
                        new QueryWorkLinesCallback(this, SharedConfigHelper.getInstance().getRepositoryId(),   new QueryWorkLinesCallback.OnLoadDataListener() {
                            @Override
                            public void onSuccess(ArrayList<WorkLine> list) {
                                WorkLineDialog dialog = new WorkLineDialog(MenuActivity.this);
                                dialog.setData(list);
                                dialog.show();
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
