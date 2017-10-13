package com.peihuo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peihuo.R;
import com.peihuo.entity.SortingForm;
import com.peihuo.entity.SortingInfo;
import com.peihuo.net.QuerySortingInfoCallback;
import com.peihuo.net.UpdateSortingOverCallback;
import com.peihuo.system.DataDictionary;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.system.SystemConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 2017/9/19.
 */

public class SortingInfoFragment extends Fragment implements View.OnClickListener {


    private TextView mCode;//单号
    private TextView mCustomer;//用户ID
    private TextView mBatch;//批次
    private TextView mSerial;//流水
    private TextView position;//坑位
    private LinearLayout mLayout;//数据容器

    private QuerySortingInfoCallback mInfoCallback;
    private LinearLayout.LayoutParams mLayoutParams;
    private List<String> idList = new ArrayList<>();
    private View fragmentV;
    private Button mPass;
    private SortingForm mForm;
    private ImageView mImageView;
    private ImageView mBg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (fragmentV == null) {
            fragmentV = inflater.inflate(R.layout.fragment_sorting_info, null);

            initView(fragmentV);
            onDataChange();
        }
        ViewParent parent = fragmentV.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(fragmentV);
        }
        return fragmentV;
    }

    private void onDataChange() {
        Log.e("jingo", "onDataChange");
        Bundle bundle = getArguments();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable(SystemConfig.BUNDLE_KEY_SORTING_INFO);
            if (serializable != null && serializable instanceof SortingForm) {
                mForm = (SortingForm) serializable;
                initTitles();
                mInfoCallback = new QuerySortingInfoCallback(getContext(), mForm.getBelongorderid(),
                        SharedConfigHelper.getInstance().getUserId(),
                        new QuerySortingInfoCallback.OnLoadDataListener() {
                            @Override
                            public void onSuccess(ArrayList<SortingInfo> list) {
                                mLayout.removeAllViews();
                                idList.clear();
                                if (list != null && list.size() > 0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        SortingInfo info = list.get(i);
                                        idList.add(info.getHandlingOrderCode());
                                        View view = View.inflate(getContext(), R.layout.sorting_info_item, null);
                                        if (info.getProductCode() != null)
                                            ((TextView) view.findViewById(R.id.sorting_info_item_code)).setText(info.getProductCode());
                                        if (info.getProName() != null)
                                            ((TextView) view.findViewById(R.id.sorting_info_item_name)).setText(info.getProName());
                                        ((TextView) view.findViewById(R.id.sorting_info_item_count)).setText(String.valueOf(info.getUseCount()));
                                        if (info.getProUnite() != null)
                                            ((TextView) view.findViewById(R.id.sorting_info_item_unit)).setText(info.getProUnite());
                                        if(info.getProstandard() != null){
                                            ((TextView) view.findViewById(R.id.sorting_info_item_prostandard)).setText(info.getProstandard());
                                        }
                                        if (info.getHandlingOrderCode() != null)
                                            ((TextView) view.findViewById(R.id.sorting_info_item_handling)).setText(info.getHandlingOrderCode());
                                        if (DataDictionary.getInstance().isSortingSingleOrGroup(info.getIs_suit())) {
                                            ((TextView) view.findViewById(R.id.sorting_info_item_type)).setText(getText(R.string.sorting_single));
                                            if (i % 2 == 0)
                                                view.setSelected(true);
                                        } else {
                                            ((TextView) view.findViewById(R.id.sorting_info_item_type)).setText(getText(R.string.sorting_group));
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
    }

    public void initView(View view) {
        mBg = (ImageView) view.findViewById(R.id.sorting_info_pitposition_bg);
        mLayout = (LinearLayout) view.findViewById(R.id.sorting_info_list);

        //上一个按钮
//        findViewById(R.id.sorting_info_button_last).setOnClickListener(this);
        mPass = (Button) view.findViewById(R.id.sorting_info_button_check);
        mPass.setOnClickListener(this);
//        findViewById(R.id.sorting_info_button_next).setOnClickListener(this);


        mCode = (TextView) view.findViewById(R.id.sorting_info_code);//单号
        mCustomer = (TextView) view.findViewById(R.id.sorting_info_customer);//用户ID
        mBatch = (TextView) view.findViewById(R.id.sorting_info_patch);//批次
        mSerial = (TextView) view.findViewById(R.id.sorting_info_serial);//流水
        position = (TextView) view.findViewById(R.id.sorting_info_position);//坑位

        mImageView = (ImageView) view.findViewById(R.id.sorting_info_laber_pass);

        mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutParams.setMargins(0, 0, 0, 1);
    }


    private void initTitles() {
        SortingForm order = mForm;
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
        initStatus(mForm, false);

        int resId;
        resId = DataDictionary.getInstance().getPitPostionBg(mForm.getPitposition());
        if(resId > 0)
            mBg.setImageResource(resId);
    }

    @Override
    public void onClick(View v) {
        if (v == mPass) {
            if (mForm!= null) {
                new UpdateSortingOverCallback(getContext(), SharedConfigHelper.getInstance().getUserId(), mForm.getBelongorderid(), idList, new UpdateSortingOverCallback.OnUpdateDataListener() {
                    @Override
                    public void onSuccess() {
                        mForm.setAcceptanceState("1");
                        initStatus(mForm, true);
//                        onClick(findViewById(R.id.sorting_info_button_next));
                    }
                });
            }
        }
    }

    private void initStatus(SortingForm order, boolean bAnim) {
        if (TextUtils.equals(order.getAcceptanceState(), "1")) {
            mImageView.setImageResource(R.mipmap.laber_accepetance_pass);
            mImageView.setVisibility(View.VISIBLE);
            if (bAnim) {
                startAnim();
            }
            mPass.setVisibility(View.INVISIBLE);
        }  else {
            mImageView.setVisibility(View.GONE);
            mPass.setVisibility(View.VISIBLE);
        }

    }

    private View getRootView() {
        return ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
    }

    private void startAnim() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.gaizhang);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.gaizhang2);
                getRootView().startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.startAnimation(animation);
    }

}
