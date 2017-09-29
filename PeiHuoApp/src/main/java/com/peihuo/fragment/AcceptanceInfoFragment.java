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
import com.peihuo.entity.AcceptanceForm;
import com.peihuo.entity.AcceptanceInfo;
import com.peihuo.net.QueryAcceptanceInfoCallback;
import com.peihuo.net.UpdateAcceptanceErrorCallback;
import com.peihuo.net.UpdateAcceptancePassCallback;
import com.peihuo.system.DataDictionary;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.system.SystemConfig;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hb on 2017/9/19.
 */

public class AcceptanceInfoFragment extends Fragment implements View.OnClickListener {


    private TextView mSailCode;//销售单号
    private TextView mPath;//路径
    private TextView mBatch;//批次
    private TextView mAcceptanceCode;//验收单号
    private TextView mTime;//单据时间
    private TextView mTotal;//合计
    private TextView mPitposition;//坑位
    private LinearLayout mSingleLayout;//单品容器
    private LinearLayout mGroupLayout;
    private TextView mSingleLabel;
    private TextView mGroupLabel;
    private LinearLayout mSingleTitle;
    private LinearLayout mGroupTitle;
    private View fragmentV;

    private QueryAcceptanceInfoCallback mInfoCallback;
    private LinearLayout.LayoutParams mLayoutParams;
    private LinearLayout mButtonsLayout;
    private ImageView mImageView;
    private ImageView mBg;

    private AcceptanceForm mForm;
    private Button mPass;
    private Button mError;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (fragmentV == null) {
            fragmentV = inflater.inflate(R.layout.fragment_acceptance_info, null);
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
            Serializable serializable = bundle.getSerializable(SystemConfig.BUNDLE_KEY_ACCEPTANCE_INFO);
            if (serializable != null && serializable instanceof AcceptanceForm) {
                mForm = (AcceptanceForm) serializable;
                initTitles();
                int resId;
                resId = DataDictionary.getInstance().getPitPostionBg(mForm.getPitposition());
                if(resId > 0)
                    mBg.setImageResource(resId);

                mInfoCallback = new QueryAcceptanceInfoCallback(getActivity(), mForm.getCode(),
                        new QueryAcceptanceInfoCallback.OnLoadDataListener() {
                    @Override
                    public void onSuccess(ArrayList<AcceptanceInfo> list) {
                        mSingleLayout.removeAllViews();
                        mGroupLayout.removeAllViews();
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                AcceptanceInfo info = list.get(i);
                                if (!DataDictionary.getInstance().isAcceptanceSingleOrGroup(info.getIs_suit())) {
                                    View view = View.inflate(getContext(), R.layout.acceptance_info_single_item, null);
                                    if (info.getProductCode() != null)
                                        ((TextView) view.findViewById(R.id.acceptance_info_item_code)).setText(info.getProductCode());
                                    if (info.getProName() != null)
                                        ((TextView) view.findViewById(R.id.acceptance_info_item_name)).setText(info.getProName());

                                    ((TextView) view.findViewById(R.id.acceptance_info_item_count)).setText(String.valueOf(info.getUseCount()));
                                    if (info.getProUnite() != null)
                                        ((TextView) view.findViewById(R.id.acceptance_info_item_unit)).setText(info.getProUnite());

                                    mSingleLayout.addView(view, mLayoutParams);
                                } else {
                                    View view = View.inflate(getContext(), R.layout.acceptance_info_group_item, null);
                                    if (info.getProName() != null)
                                        ((TextView) view.findViewById(R.id.acceptance_info_item_group_name)).setText(info.getGroupName());
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
    }

    public void initView(View view) {
        mBg = (ImageView) view.findViewById(R.id.acceptance_info_pitposition_bg);
        mSingleLayout = (LinearLayout) view.findViewById(R.id.acceptance_info_single_list);
        mGroupLayout = (LinearLayout) view.findViewById(R.id.acceptance_info_group_list);

        mGroupLabel = (TextView) view.findViewById(R.id.acceptance_info_group_label);
        mSingleLabel = (TextView) view.findViewById(R.id.acceptance_info_single_label);

        mGroupTitle = (LinearLayout) view.findViewById(R.id.acceptance_info_item_group_title);
        mSingleTitle = (LinearLayout) view.findViewById(R.id.acceptance_info_item_single_title);

        mButtonsLayout = (LinearLayout) view.findViewById(R.id.acceptance_info_buttons_layout);
        mImageView = (ImageView) view.findViewById(R.id.acceptance_info_laber_pass);
        //上一个按钮
//        view.findViewById(R.id.acceptance_info_button_last).setOnClickListener(this);
        mError = (Button) view.findViewById(R.id.acceptance_info_button_error);
        mError.setOnClickListener(this);
        mPass = (Button) view.findViewById(R.id.acceptance_info_button_pass);
        mPass.setOnClickListener(this);
//        view.findViewById(R.id.acceptance_info_button_next).setOnClickListener(this);


        mSailCode = (TextView) view.findViewById(R.id.acceptance_info_sail_code);
        mPath = (TextView) view.findViewById(R.id.acceptance_info_path);
        mBatch = (TextView) view.findViewById(R.id.acceptance_info_batch);
        mAcceptanceCode = (TextView) view.findViewById(R.id.acceptance_info_acceptance_code);
        mTime = (TextView) view.findViewById(R.id.acceptance_info_time);
        mTotal = (TextView) view.findViewById(R.id.acceptance_info_total);
        mPitposition = (TextView) view.findViewById(R.id.acceptance_info_pitposition);

        mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(0, 0, 0, 2);
    }


    private void initTitles() {

        if (mForm.getBelongorderid() != null)
            mSailCode.setText(getString(R.string.format_sorting_sale_number, mForm.getBelongorderid()));
        if (mForm.getCode() != null)
            mAcceptanceCode.setText(getString(R.string.format_acceptance_accptance_code, mForm.getCode()));
        if (mForm.getBatchCount() != null)
            mBatch.setText(getString(R.string.format_sorting_batch, mForm.getBatchCount()));
        if (mForm.getStartTime() != null) {
            if(mForm.getStartTime().length() > 16)
                mTime.setText(getString(R.string.format_acceptance_order_time, mForm.getStartTime().substring(0, 16)));
            else
                mTime.setText(getString(R.string.format_acceptance_order_time, mForm.getStartTime()));

        }
        mTotal.setText(getString(R.string.format_acceptance_total, String.valueOf(mForm.getSuitUniteProductCount())));
        if (mForm.getTransferPath() != null)
            mPath.setText(getString(R.string.format_acceptance_path, mForm.getTransferPath()));
        if(mForm.getPitposition() != null){
            mPitposition.setText(getString(R.string.format_acceptance_pitposition, mForm.getPitposition()));
        }
        initStatus(mForm, false);
    }

    @Override
    public void onClick(View v) {
        if(v == mPass){
            new UpdateAcceptancePassCallback(getActivity(),
                    mForm.getCode(),
                    SharedConfigHelper.getInstance().getUserId(),
                    new UpdateAcceptancePassCallback.OnUpdateDataListener() {
                    @Override
                    public void onSuccess() {
                        mForm.setAcceptanceState("2");
                        initStatus(mForm, true);
//                        onClick(findViewById(R.id.acceptance_info_button_next));
                    }
                });
        }else if(v == mError){
            new UpdateAcceptanceErrorCallback(getActivity(), mForm.getCode(), mForm.getBelongorderid(), new UpdateAcceptanceErrorCallback.OnUpdateDataListener() {
                    @Override
                    public void onSuccess() {
                        mForm.setAcceptanceState("3");
                        initStatus(mForm, true);
//                        onClick(findViewById(R.id.acceptance_info_button_next));
                    }
                });
        }
    }

    private void initStatus(AcceptanceForm order, boolean bAnim) {
        if (TextUtils.equals(order.getAcceptanceState(), "2")) {
            mImageView.setImageResource(R.mipmap.laber_accepetance_pass);
            mImageView.setVisibility(View.VISIBLE);
            if (bAnim) {
                startAnim();
            }
            mButtonsLayout.setVisibility(View.INVISIBLE);
        } else if (TextUtils.equals(order.getAcceptanceState(), "3")) {
            mImageView.setImageResource(R.mipmap.laber_accepetance_error);
            mImageView.setVisibility(View.VISIBLE);
            if (bAnim) {
                startAnim();
            }
            mButtonsLayout.setVisibility(View.INVISIBLE);
        } else {
            mImageView.setVisibility(View.GONE);
            mButtonsLayout.setVisibility(View.VISIBLE);
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
