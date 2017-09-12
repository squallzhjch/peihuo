package com.peihuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.db.QuerySortingInfoCallback;
import com.peihuo.db.QuerySortingListCallback;
import com.peihuo.db.UpdateSortingOverCallback;
import com.peihuo.entity.SortingForm;
import com.peihuo.entity.SortingInfo;
import com.peihuo.system.DataDictionary;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.system.SystemConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private QuerySortingInfoCallback mInfoCallback;
    private LinearLayout.LayoutParams mLayoutParams;
    private List<String> idList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_sorting_info);
            mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mLayoutParams.setMargins(0, 0, 0, 1);

            //标题
            TextView title = (TextView) findViewById(R.id.title_text);
            title.setText(getText(R.string.plan_title));

            TextView leftTitle = (TextView) findViewById(R.id.title_text_left);
            leftTitle.setText(getText(R.string.sorting_order_info));
            leftTitle.setVisibility(View.VISIBLE);

            //用户名
            TextView userName = (TextView) findViewById(R.id.title_username_text);
            userName.setText(SharedConfigHelper.getInstance().getUserName());

            //回退按钮
            ImageView back = (ImageView) findViewById(R.id.title_back_button);
            back.setOnClickListener(this);

            mLayout = (LinearLayout) findViewById(R.id.sorting_info_list);

            //上一个按钮
            findViewById(R.id.sorting_info_button_last).setOnClickListener(this);
            findViewById(R.id.sorting_info_button_check).setOnClickListener(this);
            findViewById(R.id.sorting_info_button_next).setOnClickListener(this);


            mCode = (TextView) findViewById(R.id.sorting_info_code);//单号
            mCustomer = (TextView) findViewById(R.id.sorting_info_customer);//用户ID
            mBatch = (TextView) findViewById(R.id.sorting_info_patch);//批次
            mSerial = (TextView) findViewById(R.id.sorting_info_serial);//流水
            position = (TextView) findViewById(R.id.sorting_info_position);//坑位

            receiveData();
            initTitles();
            initData();
        }
    }

    private void initData() {
        if (mList != null && mList.size() > mSelectIndex) {
            SortingForm order = mList.get(mSelectIndex);
            mInfoCallback = new QuerySortingInfoCallback(this, order.getBelongorderid(), new QuerySortingInfoCallback.OnLoadDataListener() {
                @Override
                public void onSuccess(ArrayList<SortingInfo> list) {
                    mLayout.removeAllViews();
                    idList.clear();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            SortingInfo info = list.get(i);
                            idList.add(info.getHandlingOrderCode());
                            View view = View.inflate(SortingInfoActivity.this, R.layout.sorting_info_item, null);
                            if (info.getProductCode() != null)
                                ((TextView) view.findViewById(R.id.sorting_info_item_code)).setText(info.getProductCode());
                            if (info.getProName() != null)
                                ((TextView) view.findViewById(R.id.sorting_info_item_name)).setText(info.getProName());
                            ((TextView) view.findViewById(R.id.sorting_info_item_count)).setText(String.valueOf(info.getUseCount()));
                            if (info.getProUnite() != null)
                                ((TextView) view.findViewById(R.id.sorting_info_item_unit)).setText(info.getProUnite());
                            if (info.getHandlingOrderCode() != null)
                                ((TextView) view.findViewById(R.id.sorting_info_item_handling)).setText(info.getHandlingOrderCode());
                            if (!DataDictionary.getInstance().isSortingSingleOrGroup(info.getIs_suit())) {
                                ((TextView) view.findViewById(R.id.sorting_info_item_type)).setText( getText(R.string.sorting_single));
                                if (i % 2 == 0)
                                    view.setSelected(true);
                            } else {
                                ((TextView) view.findViewById(R.id.sorting_info_item_type)).setText( getText(R.string.sorting_group));
                                view.setEnabled(false);
                            }
                            mLayout.addView(view, mLayoutParams);
                        }
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

        if (mList != null && mList.size() > mSelectIndex) {
            SortingForm order = mList.get(mSelectIndex);
            if (order.getBelongorderid() != null)
                mCode.setText(getString(R.string.format_sorting_sale_number, order.getBelongorderid()));
            if (order.getCustomerId() != null)
                mCustomer.setText(getString(R.string.format_sorting_customer, order.getCustomerId()));
            if (order.getBatchCount() != null)
                mBatch.setText(getString(R.string.format_sorting_batch, order.getBatchCount()));
            if (order.getAssemblelineno() != null)
                mSerial.setText(getString(R.string.format_sorting_serial, order.getAssemblelineno()));
            if (order.getPitposition() != null)
                position.setText(getString(R.string.format_sorting_position, order.getPitposition()));
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PlanFormActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SystemConfig.BUNDLE_KEY_BACK_LIST_TYPE, 1);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_button:
                onBackPressed();
                break;
            case R.id.sorting_info_button_last:
                if (mList.size() > mSelectIndex && mSelectIndex > 0) {
                    mSelectIndex--;
                    SortingForm form = mList.get(mSelectIndex);
                    mInfoCallback.loadData(form.getBelongorderid());
                    initTitles();
                } else {
                    Toast.makeText(this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sorting_info_button_next:
                if (mList.size() > mSelectIndex + 1) {
                    mSelectIndex++;
                    SortingForm form = mList.get(mSelectIndex);
                    mInfoCallback.loadData(form.getBelongorderid());
                    initTitles();
                } else {
                    int page;
                    int listSize = mList.size();
                    if (listSize % 10 > 0) {
                        page = listSize / 10 + 1;
                    } else {
                        page = listSize / 10;
                    }
                    new QuerySortingListCallback(this,
                            SharedConfigHelper.getInstance().getUserId(),
                            10, page,
                            new QuerySortingListCallback.OnLoadDataListener() {
                        @Override
                        public void onSuccess(ArrayList<SortingForm> list) {
                            if (list == null || list.size() == 0) {
                                Toast.makeText(SortingInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
                            }
                            for (SortingForm form : list) {
                                mList.add(form);
                            }
                            if (mList.size() > mSelectIndex + 1) {
                                mSelectIndex++;
                                SortingForm form = mList.get(mSelectIndex);
                                mInfoCallback.loadData(form.getBelongorderid());
                                initTitles();
                            }
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(SortingInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.sorting_info_button_check:
                if (mList!= null && mList.size() > mSelectIndex) {
                    SortingForm form = mList.get(mSelectIndex);
                    new UpdateSortingOverCallback(this, SharedConfigHelper.getInstance().getUserId(), form.getBelongorderid() , idList);
                }
                break;
        }
    }

}
