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
import com.peihuo.adapter.SortingInfoAdapter;
import com.peihuo.entity.SortingForm;
import com.peihuo.fragment.SortingInfoFragment;
import com.peihuo.net.QuerySortingListCallback;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.system.SystemConfig;
import com.peihuo.ui.MyViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/8/29.
 * 分拣单详情
 */

public class SortingInfoActivity extends FragmentActivity implements View.OnClickListener {

    private ArrayList<SortingForm> mList;//上个页面的数据
    private int mSelectIndex = 0;// 当前所选分拣单的位置


    private MyViewPager mViewPager;
    private SortingInfoAdapter mAdapter;
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
            leftTitle.setVisibility(View.VISIBLE);

            final ImageView exit = (ImageView)findViewById(R.id.exit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = View.inflate(SortingInfoActivity.this, R.layout.exit_pop_layout, null);
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

            receiveData();
            initViewPager();
        }
    }

    private void initViewPager() {
        mViewPager = (MyViewPager) findViewById(R.id.view_pager);
        mAdapter = new SortingInfoAdapter(getSupportFragmentManager());
        if(mList != null && mList.size() > 0){
            List<Fragment> list = new ArrayList<>();
            for(SortingForm form:mList){
                Bundle bundle = new Bundle();
                bundle.putSerializable(SystemConfig.BUNDLE_KEY_SORTING_INFO, form);
                SortingInfoFragment fragment = new SortingInfoFragment();
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
                        new QuerySortingListCallback(SortingInfoActivity.this, SharedConfigHelper.getInstance().getUserId(), SharedConfigHelper.getInstance().getWorkLineId(), 10, page, new QuerySortingListCallback.OnLoadDataListener() {
                            @Override
                            public void onSuccess(ArrayList<SortingForm> list) {
                                if (list == null || list.size() == 0) {
                                    Toast.makeText(SortingInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
                                }
                                for (SortingForm form : list) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(SystemConfig.BUNDLE_KEY_SORTING_INFO, form);
                                    SortingInfoFragment fragment = new SortingInfoFragment();
                                    fragment.setArguments(bundle);
                                    mAdapter.addFragment(fragment);
                                    mList.add(form);
                                }
                                mAdapter.notifyDataSetChanged();
                                mViewPager.setCurrentItem(mSelectIndex);
                            }


                            @Override
                            public void onError() {
                                Toast.makeText(SortingInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
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

//    private void initData() {
//        if (mList != null && mList.size() > mSelectIndex) {
//            SortingForm order = mList.get(mSelectIndex);
//            mInfoCallback = new QuerySortingInfoCallback(this, order.getBelongorderid(),
//                    SharedConfigHelper.getInstance().getUserId(),
//                    new QuerySortingInfoCallback.OnLoadDataListener() {
//                @Override
//                public void onSuccess(ArrayList<SortingInfo> list) {
//                    mLayout.removeAllViews();
//                    idList.clear();
//                    if (list != null && list.size() > 0) {
//                        for (int i = 0; i < list.size(); i++) {
//                            SortingInfo info = list.get(i);
//                            idList.add(info.getHandlingOrderCode());
//                            View view = View.inflate(SortingInfoActivity.this, R.layout.sorting_info_item, null);
//                            if (info.getProductCode() != null)
//                                ((TextView) view.findViewById(R.id.sorting_info_item_code)).setText(info.getProductCode());
//                            if (info.getProName() != null)
//                                ((TextView) view.findViewById(R.id.sorting_info_item_name)).setText(info.getProName());
//                            ((TextView) view.findViewById(R.id.sorting_info_item_count)).setText(String.valueOf(info.getUseCount()));
//                            if (info.getProUnite() != null)
//                                ((TextView) view.findViewById(R.id.sorting_info_item_unit)).setText(info.getProUnite());
//                            if (info.getHandlingOrderCode() != null)
//                                ((TextView) view.findViewById(R.id.sorting_info_item_handling)).setText(info.getHandlingOrderCode());
//                            if (DataDictionary.getInstance().isSortingSingleOrGroup(info.getIs_suit())) {
//                                ((TextView) view.findViewById(R.id.sorting_info_item_type)).setText( getText(R.string.sorting_single));
//                                if (i % 2 == 0)
//                                    view.setSelected(true);
//                            } else {
//                                ((TextView) view.findViewById(R.id.sorting_info_item_type)).setText( getText(R.string.sorting_group));
//                                view.setEnabled(false);
//                            }
//                            mLayout.addView(view, mLayoutParams);
//                        }
//                    }
//                }
//
//                @Override
//                public void onError() {
//
//                }
//            });
//
//        }
//    }

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
//            case R.id.sorting_info_button_last:
//                if (mList.size() > mSelectIndex && mSelectIndex > 0) {
//                    mSelectIndex--;
//                    SortingForm form = mList.get(mSelectIndex);
//                    mInfoCallback.loadData(form.getBelongorderid());
//                    initTitles();
//                } else {
//                    Toast.makeText(this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.sorting_info_button_next:
//                if (mList.size() > mSelectIndex + 1) {
//                    mSelectIndex++;
//                    SortingForm form = mList.get(mSelectIndex);
//                    mInfoCallback.loadData(form.getBelongorderid());
//                    initTitles();
//                } else {
//                    int page;
//                    int listSize = mList.size();
//                    if (listSize % 10 > 0) {
//                        page = listSize / 10 + 1;
//                    } else {
//                        page = listSize / 10;
//                    }
//                    new QuerySortingListCallback(this,
//                            SharedConfigHelper.getInstance().getUserId(),
//                            10, page,
//                            new QuerySortingListCallback.OnLoadDataListener() {
//                        @Override
//                        public void onSuccess(ArrayList<SortingForm> list) {
//                            if (list == null || list.size() == 0) {
//                                Toast.makeText(SortingInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
//                            }
//                            for (SortingForm form : list) {
//                                mList.add(form);
//                            }
//                            if (mList.size() > mSelectIndex + 1) {
//                                mSelectIndex++;
//                                SortingForm form = mList.get(mSelectIndex);
//                                mInfoCallback.loadData(form.getBelongorderid());
//                                initTitles();
//                            }
//                        }
//
//                        @Override
//                        public void onError() {
//                            Toast.makeText(SortingInfoActivity.this, R.string.toast_noting_data, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }


//                break;
//            case R.id.sorting_info_button_check:
//
//                break;
        }
    }

}
