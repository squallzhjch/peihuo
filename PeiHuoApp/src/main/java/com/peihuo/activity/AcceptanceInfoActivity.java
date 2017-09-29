package com.peihuo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.peihuo.R;
import com.peihuo.adapter.AcceptanceInfoAdapter;
import com.peihuo.entity.AcceptanceForm;
import com.peihuo.fragment.AcceptanceInfoFragment;
import com.peihuo.net.QueryAcceptanceListCallback;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.system.SystemConfig;
import com.peihuo.ui.MyViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/8/29.
 * 验收单详情
 */

public class AcceptanceInfoActivity extends FragmentActivity {

    private int mSelectIndex = 0;// 当前所选分拣单的位置
    private ArrayList<AcceptanceForm> mList;//上个页面的数据
    private MyViewPager mViewPager;
    private AcceptanceInfoAdapter mAdapter;
    private int mState;
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

            final ImageView exit = (ImageView) findViewById(R.id.exit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = View.inflate(AcceptanceInfoActivity.this, R.layout.exit_pop_layout, null);
                    final PopupWindow window = new PopupWindow(view,
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    window.setContentView(view);
                    window.showAsDropDown(v, 20, 20);
                    view.findViewById(R.id.exit_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            window.dismiss();
                        }
                    });
                    view.findViewById(R.id.exit_commit).setOnClickListener(new View.OnClickListener() {
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
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });


            receiveData();
            initViewPager();
        }
    }

    private void initViewPager() {
        mViewPager = (MyViewPager) findViewById(R.id.view_pager);
        mAdapter = new AcceptanceInfoAdapter(getSupportFragmentManager());
        if (mList != null && mList.size() > 0) {
            List<Fragment> list = new ArrayList<>();
            for (AcceptanceForm form : mList) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(SystemConfig.BUNDLE_KEY_ACCEPTANCE_INFO, form);
                AcceptanceInfoFragment fragment = new AcceptanceInfoFragment();
                fragment.setArguments(bundle);
                list.add(fragment);
            }
            mViewPager.setAdapter(mAdapter);
            mAdapter.setData(list);
            mAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(mSelectIndex);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                    Log.e("jingo", "onPageScrolled " + position + " "+ positionOffset+" " + positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    mSelectIndex = position;
                    if (mList.size() == position + 1) {
                        int page;
                        int listSize = mList.size();
                        if (listSize % 10 > 0) {
                            page = listSize / 10 + 1;
                        } else {
                            page = listSize / 10;
                        }
                        new QueryAcceptanceListCallback(AcceptanceInfoActivity.this, SharedConfigHelper.getInstance().getWorkLineId(), 10, page,mState, new QueryAcceptanceListCallback.OnLoadDataListener() {
                            @Override
                            public void onSuccess(ArrayList<AcceptanceForm> list) {
                                if (list == null || list.size() == 0) {
                                    Toast.makeText(AcceptanceInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
                                }
                                for (AcceptanceForm form : list) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(SystemConfig.BUNDLE_KEY_ACCEPTANCE_INFO, form);
                                    AcceptanceInfoFragment fragment = new AcceptanceInfoFragment();
                                    fragment.setArguments(bundle);
                                    mAdapter.addFragment(fragment);
                                    mList.add(form);
                                }
                                mAdapter.notifyDataSetChanged();
                                mViewPager.setCurrentItem(mSelectIndex);
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(AcceptanceInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
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
            mState = bundle.getInt(SystemConfig.BUNDLE_KEY_ACCEPTANCE_STATE, 1);
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

    //    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.title_back_button:
//                onBackPressed();
//                break;
//            case R.id.acceptance_info_button_last:
//                if (mList.size() > mSelectIndex && mSelectIndex > 0) {
//                    mSelectIndex--;
//                    AcceptanceForm form = mList.get(mSelectIndex);
////                    mInfoCallback.loadData(form.getCode());
////                    initTitles();
//                } else {
//                    Toast.makeText(this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.acceptance_info_button_next:
//                if (mList.size() > mSelectIndex + 1) {
//                    mSelectIndex++;
//                    AcceptanceForm form = mList.get(mSelectIndex);
////                    mInfoCallback.loadData(form.getCode());
////                    initTitles();
//                } else {
//                    int page;
//                    int listSize = mList.size();
//                    if (listSize % 10 > 0) {
//                        page = listSize / 10 + 1;
//                    } else {
//                        page = listSize / 10;
//                    }
//                    new QueryAcceptanceListCallback(this,  SharedConfigHelper.getInstance().getWorkLineId(), 10, page, new QueryAcceptanceListCallback.OnLoadDataListener() {
//                        @Override
//                        public void onSuccess(ArrayList<AcceptanceForm> list) {
//                            if (list == null || list.size() == 0) {
//                                Toast.makeText(AcceptanceInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
//                            }
//                            for (AcceptanceForm form : list) {
//                                mList.add(form);
//                            }
//                            if (mList.size() > mSelectIndex + 1) {
//                                mSelectIndex++;
//                                AcceptanceForm form = mList.get(mSelectIndex);
////                                mInfoCallback.loadData(form.getCode());
////                                initTitles();
//                            }
//                        }
//
//                        @Override
//                        public void onError() {
//                            Toast.makeText(AcceptanceInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                break;
//            case R.id.acceptance_info_button_pass:
//                final AcceptanceForm order = mList.get(mSelectIndex);
//                new UpdateAcceptancePassCallback(this, order.getCode(), new UpdateAcceptancePassCallback.OnUpdateDataListener() {
//                    @Override
//                    public void onSuccess() {
//                        order.setAcceptanceState("2");
//                        initStatus(order, true);
////                        onClick(findViewById(R.id.acceptance_info_button_next));
//                    }
//                });
//                break;
//            case R.id.acceptance_info_button_error:
//                final AcceptanceForm order2 = mList.get(mSelectIndex);
//                new UpdateAcceptanceErrorCallback(this, order2.getCode(), order2.getBelongorderid(), new UpdateAcceptanceErrorCallback.OnUpdateDataListener() {
//                    @Override
//                    public void onSuccess() {
//                        order2.setAcceptanceState("3");
//                        initStatus(order2, true);
////                        onClick(findViewById(R.id.acceptance_info_button_next));
//                    }
//                });
//                break;
//        }
//    }

}
