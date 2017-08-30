package com.peihuo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.peihuo.R;
import com.peihuo.fragment.AcceptanceListFragment;
import com.peihuo.fragment.ProductionListFragment;
import com.peihuo.fragment.SortingListFragment;
import com.peihuo.system.SharedConfigHelper;

/**
 * Created by 123 on 2017/8/29.
 * 生产计划和工单
 */

public class PlanSheetActivity extends FragmentActivity implements View.OnClickListener{
    private TextView mProduction;//生产单
    private TextView mSorting;//分拣单
    private TextView mAcceptance;//验收单
    private View mLastSelectView;//被选中的栏
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

       switchFragment(mProduction);

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
        switch (view.getId()){
            case R.id.title_back_button:
                Intent intent = new Intent(PlanSheetActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.plan_title_acceptance:
                switchFragment(view);
                break;
            case R.id.plan_title_production:
                switchFragment(view);
                break;
            case R.id.plan_title_sorting:
                switchFragment(view);
                break;
        }
    }

    private void switchFragment(View view) {
        if(view ==null || view.isSelected())
            return;
        if(mLastSelectView != null){
            mLastSelectView.setSelected(false);
        }
        view.setSelected(true);
        mLastSelectView = view;
        if(view == mProduction){
            if(mProductionPlanFragment == null){
                mProductionPlanFragment = new ProductionListFragment();
            }
            showFragment(mProductionPlanFragment);
        }else if(view == mSorting){
            if(mSortingListFragment == null){
                mSortingListFragment = new SortingListFragment();
            }
            showFragment(mSortingListFragment);
        }else if(view == mAcceptance){
            if(mAcceptanceListFragment == null){
                mAcceptanceListFragment = new AcceptanceListFragment();
            }
            showFragment(mAcceptanceListFragment);
        }
    }

    private void showFragment(Fragment fragment){
        if(mFragmentManager != null && fragment != null){
            mFragmentManager.beginTransaction().replace(R.id.plan_fragment, fragment).commitAllowingStateLoss();
        }
    }
}
