package com.peihuo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peihuo.R;
import com.peihuo.db.QuerySortingInfoCallback;
import com.peihuo.entity.SortingForm;
import com.peihuo.entity.SortingInfo;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.system.SystemConfig;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/29.
 * 分拣单详情
 */

public class SortingInfoActivity extends Activity implements View.OnClickListener {
    private TextView mCode;//单号
    private TextView mCustomer;//用户ID
    private TextView mBatch;//批次
    private TextView mSerial;//流水
    private TextView position;//坑位
    private LinearLayout mLayout;//数据容器
    private ArrayList<SortingForm> mList;//上个页面的数据
    private int mSelectIndex = 0;// 当前所选分拣单的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_sorting_info);

            //标题
            TextView title = (TextView) findViewById(R.id.title_text);
            title.setText(getText(R.string.plan_title));

            TextView leftTitle = (TextView) findViewById(R.id.title_text_left);
            leftTitle.setText(getText(R.string.sorting_order_info));

            //用户名
            TextView userName = (TextView) findViewById(R.id.title_username_text);
            userName.setText(SharedConfigHelper.getInstance().getUserName());

            //回退按钮
            ImageView back = (ImageView) findViewById(R.id.title_back_button);
            back.setOnClickListener(this);

            mLayout = (LinearLayout) findViewById(R.id.sorting_info_list);

            receiveData();
            initTitles();
            initData();
        }
    }

    private void initData() {
        if (mList != null && mList.size() > mSelectIndex) {
            SortingForm order = mList.get(mSelectIndex);
            QuerySortingInfoCallback infoCallback = new QuerySortingInfoCallback(this, order.getCode(), new QuerySortingInfoCallback.OnLoadDataListener() {
                @Override
                public void onSuccess(ArrayList<SortingInfo> list) {
                    mLayout.removeAllViews();
                    if (list != null && list.size() > 0) {
                        View view = View.inflate(SortingInfoActivity.this, R.layout.sorting_info_item, mLayout);
                    }
                }

                @Override
                public void onError() {

                }
            });

        }
    }

    /**
     * 接收数据
     */
    private void receiveData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable(SystemConfig.BUNDLE_KEY_SORTING_LIST);
            if (serializable != null && serializable instanceof ArrayList) {
                mList = (ArrayList<SortingForm>) serializable;
            }
            mSelectIndex = bundle.getInt(SystemConfig.BUNDLE_KEY_SORTING_LIST_INDEX, 0);
        }
    }

    private void initTitles() {
        mCode = (TextView) findViewById(R.id.sorting_info_code);//单号
        mCustomer = (TextView) findViewById(R.id.sorting_info_customer);//用户ID
        mBatch = (TextView) findViewById(R.id.sorting_info_patch);//批次
        mSerial = (TextView) findViewById(R.id.sorting_info_serial);//流水
        position = (TextView) findViewById(R.id.sorting_info_position);//坑位

        if (mList != null && mList.size() > mSelectIndex) {
            SortingForm order = mList.get(mSelectIndex);
            mCode.setText(getString(R.string.format_sorting_sale_number, order.getCode()));
            mCustomer.setText(getString(R.string.format_sorting_customer, "客户id"));
            mBatch.setText(getString(R.string.format_sorting_batch, order.getBatchCount()));
            mSerial.setText(getString(R.string.format_sorting_serial, "流水Id"));
            position.setText(getString(R.string.format_sorting_position, "坑位id"));
        }
    }


    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this, MenuActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

}
