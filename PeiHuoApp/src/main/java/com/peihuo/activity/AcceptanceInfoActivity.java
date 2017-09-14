package com.peihuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.db.QueryAcceptanceInfoCallback;
import com.peihuo.db.QueryAcceptanceListCallback;
import com.peihuo.db.UpdateAcceptanceErrorCallback;
import com.peihuo.db.UpdateAcceptancePassCallback;
import com.peihuo.entity.AcceptanceForm;
import com.peihuo.entity.AcceptanceInfo;
import com.peihuo.system.DataDictionary;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.system.SystemConfig;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/29.
 * 验收单详情
 */

public class AcceptanceInfoActivity extends Activity implements View.OnClickListener {
    private TextView mSailCode;//销售单号
    private TextView mPath;//路径
    private TextView mBatch;//批次
    private TextView mAcceptanceCode;//验收单号
    private TextView mTime;//单据时间
    private TextView mTotal;//合计
    private LinearLayout mSingleLayout;//单品容器
    private LinearLayout mGroupLayout;
    private TextView mSingleLabel;
    private TextView mGroupLabel;
    private LinearLayout mSingleTitle;
    private LinearLayout mGroupTitle;
    private ArrayList<AcceptanceForm> mList;//上个页面的数据
    private int mSelectIndex = 0;// 当前所选分拣单的位置
    private QueryAcceptanceInfoCallback mInfoCallback;
    private LinearLayout.LayoutParams mLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_acceptance_info);

            //标题
            TextView title = (TextView) findViewById(R.id.title_text);
            title.setText(getText(R.string.plan_title));

            TextView leftTitle = (TextView) findViewById(R.id.title_text_left);
            leftTitle.setText(getText(R.string.acceptance_order_info));
            leftTitle.setVisibility(View.VISIBLE);

            final ImageView exit = (ImageView)findViewById(R.id.exit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = View.inflate(AcceptanceInfoActivity.this, R.layout.exit_pop_layout, null);
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

            //用户名
            TextView userName = (TextView) findViewById(R.id.title_username_text);
            userName.setText(SharedConfigHelper.getInstance().getUserName());

            //回退按钮
            ImageView back = (ImageView) findViewById(R.id.title_back_button);
            back.setOnClickListener(this);

            mSingleLayout = (LinearLayout) findViewById(R.id.acceptance_info_single_list);
            mGroupLayout = (LinearLayout) findViewById(R.id.acceptance_info_group_list);

            mGroupLabel = (TextView) findViewById(R.id.acceptance_info_group_label);
            mSingleLabel = (TextView) findViewById(R.id.acceptance_info_single_label);

            mGroupTitle = (LinearLayout) findViewById(R.id.acceptance_info_item_group_title);
            mSingleTitle = (LinearLayout) findViewById(R.id.acceptance_info_item_single_title);

            //上一个按钮
            findViewById(R.id.acceptance_info_button_last).setOnClickListener(this);
            findViewById(R.id.acceptance_info_button_error).setOnClickListener(this);
            findViewById(R.id.acceptance_info_button_pass).setOnClickListener(this);
            findViewById(R.id.acceptance_info_button_next).setOnClickListener(this);


            mSailCode = (TextView) findViewById(R.id.acceptance_info_sail_code);
            mPath = (TextView) findViewById(R.id.acceptance_info_path);
            mBatch = (TextView) findViewById(R.id.acceptance_info_batch);
            mAcceptanceCode = (TextView) findViewById(R.id.acceptance_info_acceptance_code);
            mTime = (TextView) findViewById(R.id.acceptance_info_time);
            mTotal = (TextView) findViewById(R.id.acceptance_info_total);



            mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mLayoutParams.setMargins(0, 0, 0, 2);

            receiveData();
            initTitles();
            initData();
        }
    }

    private void initData() {
        if (mList != null && mList.size() > mSelectIndex) {
            final AcceptanceForm order = mList.get(mSelectIndex);
            mInfoCallback = new QueryAcceptanceInfoCallback(this, order.getCode(), new QueryAcceptanceInfoCallback.OnLoadDataListener() {
                @Override
                public void onSuccess(ArrayList<AcceptanceInfo> list) {
                    mSingleLayout.removeAllViews();
                    mGroupLayout.removeAllViews();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            AcceptanceInfo info = list.get(i);
                            if (!DataDictionary.getInstance().isAcceptanceSingleOrGroup(info.getIs_suit())) {
                                View view = View.inflate(AcceptanceInfoActivity.this, R.layout.acceptance_info_single_item, null);
                                if (info.getProductCode() != null)
                                    ((TextView) view.findViewById(R.id.acceptance_info_item_code)).setText(info.getProductCode());
                                if (info.getProName() != null)
                                    ((TextView) view.findViewById(R.id.acceptance_info_item_name)).setText(info.getProName());

                                ((TextView) view.findViewById(R.id.acceptance_info_item_count)).setText(String.valueOf(info.getUseCount()));
                                if (info.getProUnite() != null)
                                    ((TextView) view.findViewById(R.id.acceptance_info_item_unit)).setText(info.getProUnite());

                                mSingleLayout.addView(view, mLayoutParams);
                            } else {
                                View view = View.inflate(AcceptanceInfoActivity.this, R.layout.acceptance_info_group_item, null);
                                if (info.getProName() != null)
                                    ((TextView) view.findViewById(R.id.acceptance_info_item_group_name)).setText(info.getProName());
                                if (info.getProductCode() != null)
                                    ((TextView) view.findViewById(R.id.acceptance_info_item_code)).setText(info.getProductCode());
                                if (info.getProName() != null)
                                    ((TextView) view.findViewById(R.id.acceptance_info_item_name)).setText(info.getProName());
                                ((TextView) view.findViewById(R.id.acceptance_info_item_count)).setText(String.valueOf(info.getUseCount()));
                                if (info.getProUnite() != null)
                                    ((TextView) view.findViewById(R.id.acceptance_info_item_unit)).setText(info.getProUnite());
                                mGroupLayout.addView(view, mLayoutParams);
                            }
                        }
                        if (mSingleLayout.getChildCount() > 0) {
                            mSingleLabel.setText(getString(R.string.format_acceptance_item_single_title, String.valueOf(mSingleLayout.getChildCount())));
                            mSingleLabel.setVisibility(View.VISIBLE);
                            mSingleLayout.setVisibility(View.VISIBLE);
                            mSingleTitle.setVisibility(View.VISIBLE);
                        } else {
                            mSingleLabel.setVisibility(View.GONE);
                            mSingleLayout.setVisibility(View.GONE);
                            mSingleTitle.setVisibility(View.GONE);
                        }
                        if (mGroupLayout.getChildCount() > 0) {
                            mGroupLabel.setText(getString(R.string.format_acceptance_item_group_title, String.valueOf(mGroupLayout.getChildCount())));
                            mGroupLabel.setVisibility(View.VISIBLE);
                            mGroupLayout.setVisibility(View.VISIBLE);
                            mGroupTitle.setVisibility(View.VISIBLE);
                        } else {
                            mGroupLabel.setVisibility(View.GONE);
                            mGroupLayout.setVisibility(View.GONE);
                            mGroupTitle.setVisibility(View.GONE);
                        }
                    } else {
                        mGroupLabel.setVisibility(View.GONE);
                        mGroupLayout.setVisibility(View.GONE);
                        mGroupTitle.setVisibility(View.GONE);

                        mSingleLabel.setVisibility(View.GONE);
                        mSingleLayout.setVisibility(View.GONE);
                        mSingleTitle.setVisibility(View.GONE);
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
                mList = (ArrayList<AcceptanceForm>) serializable;
            }
            mSelectIndex = bundle.getInt(SystemConfig.BUNDLE_KEY_SORTING_LIST_INDEX, 0);
        }
    }

    private void initTitles() {

        if (mList != null && mList.size() > mSelectIndex) {
            AcceptanceForm order = mList.get(mSelectIndex);
            if (order.getBelongorderid() != null)
                mSailCode.setText(getString(R.string.format_sorting_sale_number, order.getBelongorderid()));
            if (order.getCode() != null)
                mAcceptanceCode.setText(getString(R.string.format_acceptance_accptance_code, order.getCode()));
            if (order.getBatchCount() != null)
                mBatch.setText(getString(R.string.format_sorting_batch, order.getBatchCount()));
            if (order.getStartTime() != null)
                mTime.setText(getString(R.string.format_acceptance_order_time, order.getStartTime().substring(0,16)));
            mTotal.setText(getString(R.string.format_acceptance_total, String.valueOf(order.getSuitUniteProductCount())));
            if (order.getTransferPath() != null)
                mPath.setText(getString(R.string.format_acceptance_path, order.getTransferPath()));
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PlanFormActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SystemConfig.BUNDLE_KEY_BACK_LIST_TYPE, 2);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_button:
                onBackPressed();
                break;
            case R.id.acceptance_info_button_last:
                if (mList.size() > mSelectIndex && mSelectIndex > 0) {
                    mSelectIndex--;
                    AcceptanceForm form = mList.get(mSelectIndex);
                    mInfoCallback.loadData(form.getCode());
                    initTitles();
                } else {
                    Toast.makeText(this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.acceptance_info_button_next:
                if (mList.size() > mSelectIndex + 1) {
                    mSelectIndex++;
                    AcceptanceForm form = mList.get(mSelectIndex);
                    mInfoCallback.loadData(form.getCode());
                    initTitles();
                } else {
                    int page;
                    int listSize = mList.size();
                    if (listSize % 10 > 0) {
                        page = listSize / 10 + 1;
                    } else {
                        page = listSize / 10;
                    }
                    new QueryAcceptanceListCallback(this,  SharedConfigHelper.getInstance().getWorkLineId(), 10, page, new QueryAcceptanceListCallback.OnLoadDataListener() {
                        @Override
                        public void onSuccess(ArrayList<AcceptanceForm> list) {
                            if (list == null || list.size() == 0) {
                                Toast.makeText(AcceptanceInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
                            }
                            for (AcceptanceForm form : list) {
                                mList.add(form);
                            }
                            if (mList.size() > mSelectIndex + 1) {
                                mSelectIndex++;
                                AcceptanceForm form = mList.get(mSelectIndex);
                                mInfoCallback.loadData(form.getCode());
                                initTitles();
                            }
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(AcceptanceInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.acceptance_info_button_pass:
                AcceptanceForm order = mList.get(mSelectIndex);
                new UpdateAcceptancePassCallback(this, order.getCode(), new UpdateAcceptancePassCallback.OnUpdateDataListener() {
                    @Override
                    public void onSuccess() {
                        onClick(findViewById(R.id.acceptance_info_button_next));
                    }
                });
                break;
            case R.id.acceptance_info_button_error:
                order = mList.get(mSelectIndex);
                new UpdateAcceptanceErrorCallback(this, order.getCode(), order.getBelongorderid(), new UpdateAcceptanceErrorCallback.OnUpdateDataListener() {
                    @Override
                    public void onSuccess() {
                        onClick(findViewById(R.id.acceptance_info_button_next));
                    }
                });
                break;
        }
    }

}
