package com.peihuo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.peihuo.R;
import com.peihuo.fragment.AcceptanceListFragment;
import com.peihuo.fragment.ProductionListFragment;
import com.peihuo.fragment.SortingListFragment;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.system.SystemConfig;

/**
 * Created by 123 on 2017/8/29.
 * 生产计划和工单
 */

public class PlanFormActivity extends FragmentActivity implements View.OnClickListener {
    private TextView mProduction;//生产单
    private TextView mSorting;//分拣单
    private TextView mAcceptance;//验收单
    private int mLastSelectType = -1;//被选中的栏
    private FragmentManager mFragmentManager;

    //分拣单
    private SortingListFragment mSortingListFragment;
    //生产计划单
    private ProductionListFragment mProductionPlanFragment;
    //验收单
    private AcceptanceListFragment mAcceptanceListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_plan);

            //标题
            TextView title = (TextView) findViewById(R.id.title_text);
            title.setText(getText(R.string.plan_title));

            //用户名
            TextView userName = (TextView) findViewById(R.id.title_username_text);
            userName.setText(SharedConfigHelper.getInstance().getUserName());

            //回退按钮
            ImageView back = (ImageView) findViewById(R.id.title_back_button);
            back.setOnClickListener(this);


            mFragmentManager = getSupportFragmentManager();
            initTitles();
        }
    }

    private void initTitles() {
        mProduction = (TextView) findViewById(R.id.plan_title_production);
        mSorting = (TextView) findViewById(R.id.plan_title_sorting);
        mAcceptance = (TextView) findViewById(R.id.plan_title_acceptance);

        String role = SharedConfigHelper.getInstance().getUserUrole();

        if(TextUtils.equals(role, "分拣员")){
            mSorting.setEnabled(true);
        }

        if(TextUtils.equals(role, "验收员")){
            mAcceptance.setEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            switchFragment(bundle.getInt(SystemConfig.BUNDLE_KEY_BACK_LIST_TYPE, 0));
        }else{
            switchFragment(0);
        }

        mProduction.setOnClickListener(this);
        mSorting.setOnClickListener(this);
        mAcceptance.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_button:
                onBackPressed();
                break;
            case R.id.plan_title_acceptance:
                switchFragment(2);
                break;
            case R.id.plan_title_production:
                switchFragment(0);
                break;
            case R.id.plan_title_sorting:
                switchFragment(1);
                break;
        }
    }

    private void switchFragment(int type) {
        if (type == mLastSelectType)
            return;
        if (mLastSelectType == 0) {
            mProduction.setSelected(false);
        } else if (mLastSelectType == 1) {
            mSorting.setSelected(false);
        } else if (mLastSelectType == 2) {
            mAcceptance.setSelected(false);
        }
        mLastSelectType = type;
        if (type == 0) {
            mProduction.setSelected(true);
            if (mProductionPlanFragment == null) {
                mProductionPlanFragment = new ProductionListFragment();
            }
            showFragment(mProductionPlanFragment);
        } else if (type == 1) {
            mSorting.setSelected(true);
            if (mSortingListFragment == null) {
                mSortingListFragment = new SortingListFragment();
            }
            showFragment(mSortingListFragment);
        } else if (type == 2) {
            mAcceptance.setSelected(true);
            if (mAcceptanceListFragment == null) {
                mAcceptanceListFragment = new AcceptanceListFragment();
            }
            showFragment(mAcceptanceListFragment);
        }
    }

    private void showFragment(Fragment fragment) {
        if (mFragmentManager != null && fragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.plan_fragment, fragment).commitAllowingStateLoss();
        }
    }
}